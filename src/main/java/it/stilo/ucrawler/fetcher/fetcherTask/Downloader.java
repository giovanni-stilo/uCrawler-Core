package it.stilo.uCrawler.fetcher.fetcherTask;

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

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
 
/**
 *
 * @author stilo
 */
public class Downloader extends Thread {
 
    private HttpClient httpClient;
    private ActionIF action;
    private BlockingQueue<Page> toFetchUrls;
    
    public Downloader(HttpClient httpClient, ActionIF action, BlockingQueue<Page> toFetchUrls) {
    	this.httpClient = httpClient;
    	this.action = action;
    	this.toFetchUrls = toFetchUrls;
    }

    public void run() {    	
    	Logger.getLogger(this.getClass()).info("Thread["+this.getName()+" ] partito");
    
    	while(true){
    		try {
    	    	//se lista è vuota si blocca in attesa che venga popolata
				Page page = toFetchUrls.take();
				Logger.getLogger(this.getClass()).info("Thread["+this.getName()+" ] connessione verso: " + page.getUri().toString());
				fetch(page);
				
			} catch (InterruptedException e) {
				Logger.getLogger(this.getClass()).info("Thread["+this.getName()+" ] exception"+ e.getLocalizedMessage());
			}
    	}
    }
    private void fetch(Page page){
    	HttpGet httpGet = new HttpGet(page.getUri().toString());
    	
    	try{
    		ResponseHandler<String> handler = new ResponseHandler<String>() {
    			public String handleResponse(
    					HttpResponse response) throws ClientProtocolException, IOException {

    				StatusLine statusLine = response.getStatusLine();
    				HttpEntity entity = response.getEntity();

    				//controllo status code
    				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
    					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {

    						Logger.getLogger(this.getClass()).info("Downloader connection failure code: "+ response.getStatusLine().toString());
    					}
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
    		};

    		String response = httpClient.execute(httpGet, handler);
    		processPage(page, response);

    	} catch (Exception e) {
    		Logger.getLogger(this.getClass()).info("Error fetching: " + e.getMessage());
    	}
    }
       
    private void processPage(Page page, String htmlString){
    	Logger.getLogger(this.getClass()).info("Thread["+this.getName()+" ] Processo pagina: "+page.getUri().toString());
    	try {
    		//setto pagina
			page.setData(htmlString);	
	    	//processo la pagina scaricata
			action.doSomething(page);
		}
    	catch (ActionsException e) {
			Logger.getLogger(this.getClass()).info("Action exception: " +e.getLocalizedMessage());
		}
    	
    	//System.out.println(page.getHtmlString());
    }
}

