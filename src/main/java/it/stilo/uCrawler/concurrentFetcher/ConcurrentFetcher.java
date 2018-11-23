package it.stilo.uCrawler.concurrentFetcher;

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

import it.stilo.uCrawler.concurrentFetcher.downloader.DownloaderBuilderAB;
import it.stilo.uCrawler.concurrentFetcher.proxy.Proxy;
import it.stilo.uCrawler.concurrentFetcher.proxy.ProxyAddress;
import it.stilo.uCrawler.concurrentFetcher.proxy.ProxyAddressProviderIF;
import it.stilo.uCrawler.fetcher.FetcherAB;
import it.stilo.uCrawler.fetcher.fetcherTask.IdleConnectionMonitorThread;
import it.stilo.uCrawler.fetcher.robotFile.RobotFileHandler;
import it.stilo.uCrawler.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 * @author stilo
 */
public class ConcurrentFetcher extends FetcherAB{

	//private Configuration config;
	//http://hc.apache.org/httpcomponents-client-dev/tutorial/html/connmgmt.html#d5e652
	private IdleConnectionMonitorThread connectionMonitorThread;

	private BlockingQueue<Page> toFetchUrls = new LinkedBlockingQueue<Page>();
	private List<Proxy> proxyThreadList;
	private int numberOfProxy;
	private RobotFileHandler robotHandler;
	private DownloaderBuilderAB downloaderBuilder;
	private int numberOfDownloader;
	private ConcurrentLinkedQueue<ProxyAddress> proxies;
	private ProxyAddressProviderIF proxyAddressProvider;
	private boolean hasLocalProxy;

	@PostConstruct
	private void init(){
		Logger.getLogger(this.getClass()).info(" init");
		
		proxyThreadList = new ArrayList<Proxy>(numberOfProxy);		

		//creo e lancio thread con indirizzo proxy settato
		for(int i = 0; i < numberOfProxy; i++){
			Proxy p = new Proxy(toFetchUrls, downloaderBuilder, numberOfDownloader, proxyAddressProvider);
			proxyThreadList.add(p);
			p.start();
		}
		
		if(numberOfProxy == 0 || hasLocalProxy){
			//creo un solo thread Proxy "liscio" senza indirizzo proxy
			Proxy p = new Proxy(toFetchUrls, downloaderBuilder, numberOfDownloader,null);
			
			proxyThreadList.add(p);
			p.start();
		}
		
		

		//TODO: lanciare un thread monitor per monitorare stato dei thread e mitigare problemi
	}

	public void fetchPage(Page page){	
	
		if(robotHandler.isAllow(page.getUri())){
			//se url è permesso, 
			//aggiunge la richiesta ad una coda
			try {
				toFetchUrls.put(page);
			} catch (InterruptedException e) {
				Logger.getLogger(this.getClass()).info("FetchPage() exception "+e.getLocalizedMessage());
			}
		}
		else{
			//altrimenti setto stringa html di default
			page.setData(blankHtml);
		}
	}
	
	//TODO: pensare ai metodi per il rilascio risorse e terminazione thread
	public void pause(){
	}

	public void resume(){
	}

	public void stop(){
	}
	
	@Required
	public void setNumberOfProxy(int numberOfProxy) {
		this.numberOfProxy = numberOfProxy;
	}

	//@Required
	public void setRobotHandler(RobotFileHandler robotHandler) {
		this.robotHandler = robotHandler;
	}
	
	@Required
	public void setDownloaderBuilder(DownloaderBuilderAB downloaderBuilder) {
		this.downloaderBuilder = downloaderBuilder;
	}
	
	@Required
	public void setNumberOfDownloader(int numberOfDownloader) {
		this.numberOfDownloader = numberOfDownloader;
	}

	public void setProxies(ConcurrentLinkedQueue<ProxyAddress> proxies) {
		this.proxies = proxies;
	}

	public void setProxyAddressProvider(ProxyAddressProviderIF proxyAddressProvider) {
		this.proxyAddressProvider = proxyAddressProvider;
	}

	public void setHasLocalProxy(boolean hasLocalProxy) {
		this.hasLocalProxy = hasLocalProxy;
	}

	private final String blankHtml = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML LEVEL 1//EN\"><HTML>"
			+ "<HEAD><TITLE>Blank HTML Level 1 Page</TITLE>"
			+ "<META HTTP-EQUIV=\"Content-Type\" "
			+ "CONTENT=\"text/html; charset=utf-8\"></HEAD>"
			+ "<BODY></BODY></HTML>";
			
}
