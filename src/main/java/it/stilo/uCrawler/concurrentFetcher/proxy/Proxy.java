package it.stilo.uCrawler.concurrentFetcher.proxy;

/*
 * #%L
 * uCrawler
 * %%
 * Copyright (C) 2012 - 2018 Giovanni Stilo
 * %%
 * uCrawler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/lgpl-3.0.txt>.
 * #L%
 */

import it.stilo.uCrawler.concurrentFetcher.downloader.Downloader;
import it.stilo.uCrawler.concurrentFetcher.downloader.DownloaderBuilderAB;
import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.utils.NamedConcurrentLinkedQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class Proxy extends Thread {

    //Map normale, perchè solo lui ci lavora
    //key: dominio
    //value: queue di page
    private Map<String, NamedConcurrentLinkedQueue<Page>> domainMap;
    //coda di code dominio da processate e da processare
    private Queue<NamedConcurrentLinkedQueue<Page>> toWorkDomainQueue;
    private Queue<NamedConcurrentLinkedQueue<Page>> emptyDomainQueue;
    //TODO: fare setter - getter di numberOfDownloader
    private int numberOfDownloader;
    private Integer aliveDownloader = 0;
    //coda generale di pagine da fetchare, bloccante
    private BlockingQueue<Page> toFetchUrls;
    private DownloaderBuilderAB downloaderBuilder;
    private Integer cleanCounter;
    private int TIME_TO_CLEAN;
    private ProxyAddressProviderIF proxyAddressProvider;
    private ProxyAddress proxyAddress;
    private boolean stop;
    private int proxyRetry = 0;
    private int maxProxyRetry = 10;//Più basso == Più chance

    public Proxy(BlockingQueue<Page> toFetchUrls,
            DownloaderBuilderAB downloaderBuilder, int numberOfDownloader,
            ProxyAddressProviderIF proxyAddressProvider) {
        this.toFetchUrls = toFetchUrls;
        this.downloaderBuilder = downloaderBuilder;
        this.numberOfDownloader = numberOfDownloader;
        this.proxyAddressProvider = proxyAddressProvider;
        if(proxyAddressProvider != null)
        	this.proxyAddress = proxyAddressProvider.getProxyAddress();
        init();
    }

    private void init() {
        this.stop = false;

        Logger.getLogger(this.getClass()).info(this.getName() + " init");
        TIME_TO_CLEAN = 50; //TODO: media pagine dominio * numero di dowloader;
        cleanCounter = 0;
        domainMap = new HashMap<>();

        toWorkDomainQueue = new ConcurrentLinkedQueue<>();
        emptyDomainQueue = new ConcurrentLinkedQueue<>();

        createDownloader(numberOfDownloader);
    }

    //TODO: Contantori decrementali
    public void run() {
        Logger.getLogger(this.getClass()).info(this.getName() + " PARTITO");

        while (!stop) {
            try {
                Logger.getLogger(this.getClass()).info(this.getName() + " run loop");
                //se lista è vuota thread si blocca in attesa che venga popolata
                Page page = toFetchUrls.take();
                //Logger.getLogger(this.getClass()).info("Thread["+this.getName()+" ] connessione verso: " + page.getUrl().toString());
                handlePage(page);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass()).info(this.getName() + " exception" + e.getLocalizedMessage());
            }
        }
        
        Logger.getLogger(this.getClass()).info(this.getName() + " TERMINATO");

    }

    /*Smista le pagine su diverse code dominio
     * */
    private void handlePage(Page page) {

        //recupero dominio
        if(page.getUri().getHost() == null) 
        	return;
        
    	String domain = page.getUri().getHost().toLowerCase();
        NamedConcurrentLinkedQueue<Page> tempDomainQueue = null;

        //se coda dominio esiste la recupero altrimenti la creo
        if (domainMap.containsKey(domain)) {
            Logger.getLogger(this.getClass()).info(this.getName() + " recupero coda dominio da map: " + domain);
            tempDomainQueue = domainMap.get(domain);
            //inserisco pagina in coda
            tempDomainQueue.add(page);
        } else {
            Logger.getLogger(this.getClass()).info(this.getName() + " creo coda dominio in map: " + domain);
            tempDomainQueue = new NamedConcurrentLinkedQueue<Page>(domain);
            //inserisco pagina in coda
            tempDomainQueue.add(page);
            //inserisco nuovo nodo nella map
            domainMap.put(domain, tempDomainQueue);

            //inserisco coda dominio nella coda principale
            toWorkDomainQueue.add(tempDomainQueue);
        }
    }

    public NamedConcurrentLinkedQueue<Page> getDomainQueue(NamedConcurrentLinkedQueue<Page> oldDomainQueue) {

        /*Lancia il clean ogni tot di iterazioni. Viene controllato qui perchè se fatto nel run del Proxy 
         * esso può rimanere bloccato in attesa di ottenere un elemento dalla coda principale.
         * In questo caso il clean non verrà mai lanciato e le strutture dati non saranno mai riorganizzate.
         * */
        this.clean();

        //DEBUG LOG
        Logger.getLogger(this.getClass()).info(this.getName() + " ***** start GetDomain, counter_clean = " + cleanCounter + " **********");
        Logger.getLogger(this.getClass()).info(this.getName() + " size della coda delle code da processare = " + toWorkDomainQueue.size());
        Logger.getLogger(this.getClass()).info(this.getName() + " domini presenti al suo interno");
        for (NamedConcurrentLinkedQueue<Page> q : toWorkDomainQueue) {
            Logger.getLogger(this.getClass()).info(this.getName() + " dominio: " + q.getName());
        }
        //

        //controllo coda ritornata da un downloader e la inserisco in una coda opportuna
        this.insertQueue(oldDomainQueue);

        //estraggo una coda, ritorna null se coda è vuota
        NamedConcurrentLinkedQueue<Page> tempQueue = toWorkDomainQueue.poll();

        //DEBUG LOG
        if (tempQueue != null) {
            Logger.getLogger(this.getClass()).info(this.getName() + " coda dominio estratta = " + tempQueue.getName());
        }
        Logger.getLogger(this.getClass()).info(this.getName() + " ***** end GetDomain **********");
        //

        this.rebuildDownloader(tempQueue);

        return tempQueue;
    }

    private void insertQueue(NamedConcurrentLinkedQueue<Page> queue) {
        if (queue != null) {
            if (queue.isEmpty()) {
                //se vuota la inserisco nella coda delle processate
                emptyDomainQueue.add(queue);
            } else {
                //altrimenti la rimetto nella coda di quelle da processare
                toWorkDomainQueue.add(queue);
            }
        }
    }

    //Riorganizza le code interne
    private void clean() {

        synchronized (cleanCounter) {
            ++cleanCounter;
            if (cleanCounter >= TIME_TO_CLEAN) {
                Logger.getLogger(this.getClass()).info(this.getName() + " Clean data structures");

                for (int i = 0; i < emptyDomainQueue.size(); i++) {

                    //rimuovo la testa della coda dei domini
                    NamedConcurrentLinkedQueue<Page> q = emptyDomainQueue.remove();
                    if (q.isEmpty()) {
                        //rimuovo coda da mappa
                        domainMap.remove(q.getName());
                    } else {
                        Logger.getLogger(this.getClass()).info(this.getName() + " aggiungo coda non più vuota a toWorkQueue");
                        //inserisco in coda di domini da processere
                        toWorkDomainQueue.add(q);
                    }
                }

                cleanCounter = 0;
            }
        }
    }

    private void createDownloader(int numberOfDownloader) {
        aliveDownloader += numberOfDownloader;

        //Creo e lancio thread downloader
        for (int i = 0; i < numberOfDownloader; i++) {
            Downloader d = downloaderBuilder.create(this);
            d.start();
        }
    }

    public void changeProxyAddress(NamedConcurrentLinkedQueue<Page> queue) {
        this.insertQueue(queue);

        if (proxyAddressProvider != null) {
            synchronized (aliveDownloader) {
                --aliveDownloader;
                if (aliveDownloader <= 0) {
                    ProxyAddress p = proxyAddressProvider.getProxyAddress();
                    if (p == null) {
                        stop = true;
                    } else {
                        proxyAddress = p;
                        createDownloader(this.numberOfDownloader);
                    }
                }
            }
        }
    }

    private void rebuildDownloader(NamedConcurrentLinkedQueue<Page> tempQueue) {
        if (tempQueue != null && aliveDownloader != numberOfDownloader) {
            this.proxyRetry++;
            if (this.proxyRetry > this.maxProxyRetry) {
                this.createDownloader(numberOfDownloader - aliveDownloader);
                this.proxyRetry = 0;                
            }
        }
    }

    public ProxyAddress getProxyAddress() {
        return proxyAddress;
    }
}