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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class UserAgentHandler {


	private String userAgentFilePath;
	private List<String> userAgents;
	private String defaultUserAgent;

	@PostConstruct
	private void init(){
		userAgents = new ArrayList<String>();
		readUserAgents();
	}

	public String getUserAgent(){	
		//TODO: rendere dinamico
		return userAgents.get(0);
	}

	private void readUserAgents() {
		try {
			userAgents = Files.readAllLines(Paths.get(userAgentFilePath), StandardCharsets.UTF_8);
			/*for(int i = 0; i < userAgents.size(); i++){
				System.out.println("USER -> "+userAgents.get(i));
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Required
	public void setUserAgentFilePath(String userAgentFilePath) {
		this.userAgentFilePath = userAgentFilePath;
	}

	@Required
	public void setDefaultUserAgent(String defaultUserAgent) {
		this.defaultUserAgent = defaultUserAgent;
	}

	public String getDefaultUserAgent() {
		return defaultUserAgent;
	}

}
