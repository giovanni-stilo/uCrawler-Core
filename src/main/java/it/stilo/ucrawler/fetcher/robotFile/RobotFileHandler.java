package it.stilo.ucrawler.fetcher.robotFile;

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
public class RobotFileHandler {

	//Key: nome host
	//value: file robot
	private Map<String, Object> robotFilesMap;
	private HttpClient httpClient;

	public RobotFileHandler() {
		robotFilesMap = new HashMap<String, Object>();
	}

	//controlla se un url deve esser scaricato
	public boolean isAllow(URI url){

		try{
		String host = url.getHost().toLowerCase();
		String path = url.getPath().toLowerCase();

		if(!robotFilesMap.containsKey(host)){
			//robots.txt non ancora scaricato, lo scarico
			Logger.getLogger(this.getClass()).info("Scarico RobotFile di: "+host);
			fetchRobotFile(url);
		}
		else{
			//robots.txt già scaricato per quell'host
			Logger.getLogger(this.getClass()).info("RobotFile già scaricato per -> "+host);
		}

		//recupero robots salvato
		RobotFileRules rr = (RobotFileRules) robotFilesMap.get(host);
		
		/*for(String t : ts){
			Logger.getLogger(this.getClass()).info("Disallow: = "+t);

		}*/
		
		//controllo path
		if(rr.allows(path)){
			Logger.getLogger(this.getClass()).info("Path "+path+" permesso");
			return true;

		}
		Logger.getLogger(this.getClass()).info("Path "+path+" non permesso");		
		}
		catch(NullPointerException e){
			Logger.getLogger(this.getClass()).info("RobotHandler isAllow exception page: "+url.toString());
		}
		return false;
	}

	//scarica robot file
	private void fetchRobotFile(URI url) {

		URI robotUrl = null;
		String host = url.getHost().toLowerCase();
            try {
                robotUrl = new URI("http://" + host + "/robots.txt");
            } catch (URISyntaxException ex) {
                Logger.getLogger(RobotFileHandler.class).info( ex);
            }
		


		RobotFileRules rules = null;

		HttpGet httpGet = new HttpGet(robotUrl.toString());

		try{
			Logger.getLogger(this.getClass()).info("Connection to... "+robotUrl);

			ResponseHandler<String> handler = new ResponseHandler<String>() {
				public String handleResponse(
						HttpResponse response) throws ClientProtocolException, IOException {

					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();

					//controllo status code
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
						if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {

							Logger.getLogger(this.getClass()).info("Connection failure code: "+ response.getStatusLine().toString());
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
			Logger.getLogger(this.getClass()).info("RobotFile scaricato: \n"+ response);
			
			//parso il contenuto per creare RobotFileRules
			try {
				//TODO: recuperare lo user agent che si sta attualmente usando
				rules = RobotFileParser.parse(response, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0");
			} catch (Exception e) {
				Logger.getLogger(this.getClass()).info("Error parsing: " + e.getMessage());
			}
		
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).info("Error fetching: " + e.getMessage());
		}

		//se ancora null lo creo vuoto
		if (rules == null) {
			rules = new RobotFileRules();
		}

		robotFilesMap.put(host, rules);
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
