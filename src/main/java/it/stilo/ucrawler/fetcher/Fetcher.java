package it.stilo.ucrawler.fetcher;

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

import it.stilo.ucrawler.fetcher.fetcherTask.Downloader;
import it.stilo.ucrawler.fetcher.fetcherTask.IdleConnectionMonitorThread;
import it.stilo.ucrawler.fetcher.fetcherTask.UserAgentHandler;
import it.stilo.ucrawler.fetcher.robotFile.RobotFileHandler;
import it.stilo.ucrawler.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 * @author stilo
 */
public class Fetcher extends FetcherAB{

	private HttpClient httpClient;
	private HttpClientConnectionManager connManager;
	private Builder requestBuilder;
	//private Configuration config;
	//http://hc.apache.org/httpcomponents-client-dev/tutorial/html/connmgmt.html#d5e652
	private IdleConnectionMonitorThread connectionMonitorThread;

	private BlockingQueue<Page> toFetchUrls = new LinkedBlockingQueue<Page>();
	private List<Downloader> downloaderThreadList;
	private int numberOfDownloaderWorker;
	private UserAgentHandler userAgentHandler;
	private RobotFileHandler robotHandler;


	@Required
	public void setConnManager(HttpClientConnectionManager connMan) {
		this.connManager = connMan;
	}

	@Required
	public void setRequestBuilder(Builder requestBuilder) {
		this.requestBuilder = requestBuilder;
	}

	@PostConstruct
	private void init(){
		Logger.getLogger(this.getClass()).info("Fetcher init");

		HttpClientBuilder builder = HttpClientBuilder.create();  
		builder.setConnectionManager(connManager);

		RequestConfig rc = requestBuilder.build();  
		builder.setDefaultRequestConfig(rc);

		//Setto user agent di default 
                builder.setUserAgent(userAgentHandler.getDefaultUserAgent());

		httpClient =  builder.build();   
		
		connectionMonitorThread = new IdleConnectionMonitorThread(connManager);
		connectionMonitorThread.start();        

		downloaderThreadList = new ArrayList<Downloader>(numberOfDownloaderWorker);		
			
		robotHandler.setHttpClient(httpClient);
		
		//creo e lancio thread
		for(int i = 0; i < numberOfDownloaderWorker; i++){
			Downloader d = new Downloader(httpClient, action, toFetchUrls);
			downloaderThreadList.add(d);
			d.start();
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
	public void setNumberOfDownloaderWorker(int numberOfDownloaderWorker) {
		this.numberOfDownloaderWorker = numberOfDownloaderWorker;
	}
	
	@Required
	public void setUserAgentHandler(UserAgentHandler userAgentHandler) {
		this.userAgentHandler = userAgentHandler;
	}
	
	@Required
	public void setRobotHandler(RobotFileHandler robotHandler) {
		this.robotHandler = robotHandler;
	}

	private final String blankHtml = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML LEVEL 1//EN\"><HTML>"
			+ "<HEAD><TITLE>Blank HTML Level 1 Page</TITLE>"
			+ "<META HTTP-EQUIV=\"Content-Type\" "
			+ "CONTENT=\"text/html; charset=utf-8\"></HEAD>"
			+ "<BODY></BODY></HTML>";
			
}
