package it.stilo.uCrawler.concurrentFetcher.configuration;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class UserAgent implements UserAgentIF{


	private String userAgentFilePath;
	private Queue<String> userAgents;
	private String defaultUserAgent;

        public UserAgent(){
            userAgents = new ConcurrentLinkedQueue<String>();
        }
        
	@PostConstruct
	private void init(){		
		readUserAgents();
	}

	//ritorna ad ogni client un user agent diverso
	public String nextUserAgent(){
		
		/*Per ora ritorna un user agent diverso in maniera circolare.
		 * Rendere casuale?
		 * */
		//rimuovo da testa
		String userAgent = userAgents.poll();
		//reinserisco in coda
		userAgents.add(userAgent);
	    //Logger.getLogger(this.getClass()).info("USER AGENT ESTRATTO =  "+userAgent);
    	return userAgent;
	}

	private void readUserAgents() {
		try{
			BufferedReader reader = Files.newBufferedReader(Paths.get(userAgentFilePath), StandardCharsets.UTF_8);
			String lineFromFile = "";
			//Logger.getLogger(this.getClass()).info("The contents of file are: ");
			while((lineFromFile = reader.readLine()) != null){
				//Logger.getLogger(this.getClass()).info(lineFromFile);
                            if(!lineFromFile.contains("#"))
				userAgents.add(lineFromFile);
			}

		}catch(IOException exception){
			Logger.getLogger(this.getClass()).info("Error while reading file");
			userAgents.add(defaultUserAgent);
		}
	}

	@Required
	public void setUserAgentFilePath(String userAgentFilePath) {
		this.userAgentFilePath = userAgentFilePath;
	}

	@Required
	public void setDefaultUserAgent(String defaultUserAgent) {
		this.defaultUserAgent = defaultUserAgent;
                userAgents.add(defaultUserAgent);
	}

	public String getDefaultUserAgent() {
		return defaultUserAgent;
	}

}
