package it.stilo.ucrawler.concurrentFetcher.downloader;

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

import it.stilo.ucrawler.concurrentFetcher.configuration.NumericConstraintIF;
import it.stilo.ucrawler.concurrentFetcher.configuration.UserAgentIF;
import it.stilo.ucrawler.concurrentFetcher.proxy.Proxy;
import it.stilo.ucrawler.concurrentFetcher.proxy.ProxyAddress;
import it.stilo.ucrawler.core.actions.ActionIF;
import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.page.Page;
import it.stilo.ucrawler.utils.NamedConcurrentLinkedQueue;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class Downloader extends Thread {

    private HttpClientBuilder builder;
    private HttpClient httpClient;
    private HttpClientConnectionManager connManager;
    private ActionIF action;
    private DownloaderResponseHandler responseHandler;
    private HttpClientContext localContext;
    //coda thread safe non bloccante
    private NamedConcurrentLinkedQueue<Page> toFetchUrls;
    private Proxy proxy;
    private boolean stop;
    //configurazioni del client
    private NumericConstraintIF maxConnectionsPerClient;
    private int counterConnectionsPerClient;
    private NumericConstraintIF maxPageForDomain;
    private int counterPageForDomain;
    private NumericConstraintIF connectionDelay;
    private UserAgentIF userAgent;
    //numero di tentativi per scaricare una pagina
    private final int MAX_FETCH_ERROR_FOR_PAGE = 3;
    //percentuale di errori di fetching tollerati
    private float PAGE_QUALITY_THRESHOLD = 0.4f;
    private float PROXY_QUALITY_THRESHOLD = 0.6f;
    private long goodPage = 1;
    private long totalPage = 1;
    private long socketConnectionError = 1;
    private long socketTotalConnection = 1;

    public Downloader(Proxy proxy, HttpClientConnectionManager connManager, ActionIF action,
            NumericConstraintIF maxConnections, NumericConstraintIF connectionDelay,
            NumericConstraintIF maxPageForDomain, UserAgentIF userAgent) {

        this.userAgent = userAgent;
        this.action = action;
        this.connManager = connManager;
        this.proxy = proxy;
        this.maxConnectionsPerClient = maxConnections;
        this.connectionDelay = connectionDelay;
        this.maxPageForDomain = maxPageForDomain;

        init();
    }

    private void init() {
        Logger.getLogger(this.getClass()).info("Init");

        stop = false;

        //configurazione del builder per httpClient
        builder = HttpClientBuilder.create();
        builder.setConnectionManager(connManager);
        Builder requestBuilder = RequestConfig.custom();
        //connection timeout
        requestBuilder.setConnectTimeout(60000);//TODO SPRINGIZE
        requestBuilder.setConnectionRequestTimeout(60000);
        requestBuilder.setExpectContinueEnabled(true);
        requestBuilder.setSocketTimeout(60000);
        //proxy
        ProxyAddress proxyAddress = proxy.getProxyAddress();
        if (proxyAddress != null) {
        	PAGE_QUALITY_THRESHOLD = proxyAddress.getPageQualityThreshold();
        	PROXY_QUALITY_THRESHOLD = proxyAddress.getProxyQualityThreshold();
            requestBuilder.setProxy(proxyAddress.getHttpHost());
        }
        RequestConfig rc = requestBuilder.build();
        Logger.getLogger(this.getClass()).info("Proxy settato -> " + rc.getProxy());
        builder.setDefaultRequestConfig(rc);

        responseHandler = new DownloaderResponseHandler();
        localContext = createContext();

        httpClient = createHttpClient();

        if (this.maxConnectionsPerClient != null) {
            this.counterConnectionsPerClient = (int) this.maxConnectionsPerClient.nextValue();
        }

        if (this.maxPageForDomain != null) {
            this.counterPageForDomain = (int) this.maxPageForDomain.nextValue();
        }
    }

    public void run() {
        Logger.getLogger(this.getClass()).info(this.getName() + " partito");

        //startup
        waitForQueue();

        while (!stop) {

            checkPageForDomain();
            checkConnectionPerClient();


            try {
                Page page = toFetchUrls.remove();
                Logger.getLogger(this.getClass()).info(this.getName() + " connessione verso: " + page.getUri().toString());
                fetch(page);
                this.counterPageForDomain--;
                this.counterConnectionsPerClient--;

            } catch (NoSuchElementException e) {
                Logger.getLogger(this.getClass()).info(this.getName() + " run loop exception" + e.getLocalizedMessage());
            }

            if(proxy.getProxyAddress() != null){
            	this.checkProxyQuality();
            }
            
            //fermo il thread per tot tempo prima della prossima connessione
            try {
                double delay = connectionDelay.nextValue();
                Logger.getLogger(this.getClass()).info(this.getName() + " Aspetto " + delay + "ms prima della prossima connessione");
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger(this.getClass()).info(this.getName() + " THREAD TERMINATO");

    }

    private void checkPageForDomain() {

        if (this.counterPageForDomain <= 0 || toFetchUrls.isEmpty()) {
            //forzo cambio dominio se ho raggiunto il limite di pagine da scaricare
            Logger.getLogger(this.getClass()).info(this.getName() + " \n -----> maxPage raggiunto per: " + toFetchUrls.getName()
                    + " = " + counterPageForDomain);
            waitForQueue();
            createHttpClient();
            this.counterPageForDomain = (int) this.maxPageForDomain.nextValue();

            this.counterConnectionsPerClient = (int) this.maxConnectionsPerClient.nextValue();

            Logger.getLogger(this.getClass()).info(this.getName() + " \n -----> nuovo dominio: " + toFetchUrls.getName() + "\n");
        }

    }

    private void checkConnectionPerClient() {
        if (this.counterConnectionsPerClient <= 0) {
            createHttpClient();
            this.counterConnectionsPerClient = (int) this.maxConnectionsPerClient.nextValue();
        }
    }

    private void waitForQueue() {
        //Logger.getLogger(this.getClass()).info("calcola coda");

        while ((toFetchUrls = proxy.getDomainQueue(toFetchUrls)) == null) {
            Logger.getLogger(this.getClass()).info(this.getName() + " CalculateQueue: null");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger(this.getClass()).info(this.getName() + " CalculateQueue: ottenuta " + toFetchUrls.peek().getUri().getHost());
        Logger.getLogger(this.getClass()).info(this.getName() + " size " + toFetchUrls.size());
    }

    private void fetch(Page page) {

        //numero di tentativi per scaricare la pagina
        long localFetchingError = 0;


        //boolean isProxyError = false;
        this.totalPage++;

        HttpGet httpGet = new HttpGet(page.getUri().toString());

        do {
            try {
                Logger.getLogger(this.getClass()).info(this.getName() + " Tentativo: " + localFetchingError);

                //TODO: ad header auth proxy
                String response = httpClient.execute(httpGet, responseHandler, localContext);
                this.goodPage++;
                page.setRevTime(System.currentTimeMillis());
                processPage(page, response);
                break;
            } catch (ClientProtocolException e) {
                Logger.getLogger(this.getClass()).info(this.getName() + " Fetch client protocol exception: " + e.getMessage());
                ++localFetchingError;
            } catch (IOException e) {
                Logger.getLogger(this.getClass()).info(this.getName() + " Fetch IOException: " + e.getMessage());
                ++localFetchingError;
                //Eccezioni ricevute
                //1) Connect to 190.37.32.42:8080 [/190.37.32.42] failed: connect timed out
                //2) aaaaeeeee: nodename nor servname provided, or not known
                //3) Premature end of Content-Length delimited message body (expected: 39860; received: 9638
                //4) The target server failed to respond
                //5) Connect to 219.243.221.77:8080 [/219.243.221.77] failed: Connection refused
                //TODO: capire se un proxy non funziona per niente o se fallisce temporaneamente.
            }
        } while (localFetchingError < MAX_FETCH_ERROR_FOR_PAGE);

        //controllo qualità solo se il proxyAddress è settato
        if(proxy.getProxyAddress() != null){
        	//Controlli sulla metrica di qualità socketConnectionError/socketTotalConnection 1 pessima qualità 0 ottima 
        	// alla prima connessione buona viene resettata come buona eventualmente dopo degrada.
        	if (localFetchingError < MAX_FETCH_ERROR_FOR_PAGE && localFetchingError > 0) {
        		this.socketTotalConnection += MAX_FETCH_ERROR_FOR_PAGE;
        		this.socketConnectionError += localFetchingError;
        	}

        	if (localFetchingError >= MAX_FETCH_ERROR_FOR_PAGE) {
        		if (((double) this.socketConnectionError / this.socketTotalConnection) > PAGE_QUALITY_THRESHOLD) {
        			this.toFetchUrls.add(page);
        		}
        	}

        	//qualità fine
        	if (totalPage % 1000 == 999) {
        		//reimposto come ottimo
        		this.socketTotalConnection = 1;
        		this.socketConnectionError = 0;
        	}

        	//qualità raw
        	if (totalPage % 1000000 == 999999) {
        		//reimposto come ottimo
        		this.goodPage = 1;
        		this.totalPage = 1;            
        	}
        }
    }

    //qualità raw: 1 ottimo, 0 pessimo
    private void checkProxyQuality() {
        if (((double) this.goodPage / this.totalPage) < PROXY_QUALITY_THRESHOLD) {
            Logger.getLogger(this.getClass()).info(this.getName() + " Richiedo nuovo proxy a PROXY -> rapporto grezzo: "+((double) this.goodPage / this.totalPage));
            this.proxy.changeProxyAddress(toFetchUrls);
            this.stop = true;
        }
    }

    private void processPage(Page page, String htmlString) {
        Logger.getLogger(this.getClass()).info(this.getName() + " Processo pagina: " + page.getUri().toString());
        try {
            //setto pagina
            page.setData(htmlString);
            
            page.setInContext(NumericConstraintIF.class, connectionDelay);
            //processo la pagina scaricata
            action.doSomething(page);
        } catch (ActionsException e) {
            Logger.getLogger(this.getClass()).info("Action exception: " + e.getLocalizedMessage());
        }
    }

    //crea un HttpClient e lo configura
    private HttpClient createHttpClient() {
        Logger.getLogger(this.getClass()).info(this.getName() + " CREO NUOVO CLIENT");

        //Setto user agent 
        builder.setUserAgent(userAgent.nextUserAgent());

        //setto autenticazione proxy
        //http://httpcomponents.10934.n7.nabble.com/Basic-proxy-authentication-on-HttpClient-4-3-td19348.html
        if(proxy.getProxyAddress() != null){
        	builder.setDefaultCredentialsProvider(proxy.getProxyAddress().getCredentialsProvider());
        }
        
        //cancello eventuale sessione passata
        clearSession();
        
        return builder.build();
    }

    private HttpClientContext createContext() {
        // Create a local instance of cookie store
        CookieStore cookieStore = new BasicCookieStore();
        // Create local HTTP context
        localContext = new HttpClientContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        return localContext;
    }

    private void clearSession() {
        Logger.getLogger(this.getClass()).info(this.getName() + " Cancello sessione");
        CookieStore cookieStore = localContext.getCookieStore();
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }

    //TODO: cambiare il tipo ritornato dall'handler quando useremo gli stream
    private class DownloaderResponseHandler implements ResponseHandler<String> {

        @Override
        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();


            //controllo status code
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {

                    Logger.getLogger(this.getClass()).info("Downloader connection failure code: " + response.getStatusLine().toString());
                }
                //devo consumare l'entity anche se non la uso!
                EntityUtils.consume(entity);

                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }

            if (response.getEntity() == null) {
                throw new ClientProtocolException("HttpResponse contains no content");
            }
            //TODO: per ora così, migliorare usando gli stream
            String htmlString = EntityUtils.toString(entity);

            return htmlString;
        }
    }
}
