package it.stilo.ucrawler.fetcher.fetcherTask;

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

import javax.annotation.PostConstruct;


/**
 *
 * @author stilo
 */
@Deprecated
public class Configuration {

	/*!!!!!!!!!!*/
	//TODO: da completare la logica per la scelta dello user agent
	
	private int maxTotalConnection;
	private int maxTotalPerRoute;
	private int connectionTimeout;
	private String defaultUserAgent;
	private String userAgent;
	//private boolean useDefaultUserAgent;
	
	@PostConstruct
	private void init(){
		defaultUserAgent = "crawler-sapienza-ie";
	}
	
	public int getMaxTotalConnection() {
		return maxTotalConnection;
	}
	public void setMaxTotalConnection(int maxTotalConnection) {
		this.maxTotalConnection = maxTotalConnection;
	}
	public int getMaxTotalPerRoute() {
		return maxTotalPerRoute;
	}
	public void setMaxTotalPerRoute(int maxTotalPerRoute) {
		this.maxTotalPerRoute = maxTotalPerRoute;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public String getDefaultUserAgent() {
		return defaultUserAgent;
	}
	public void setDefaultUserAgent(String userAgent) {
		this.defaultUserAgent = userAgent;
	}
	
	/*public boolean isUseDefaultUserAgent() {
		return useDefaultUserAgent;
	}
	public void setUseDefaultUserAgent(boolean useDefaultUserAgent) {
		this.useDefaultUserAgent = useDefaultUserAgent;
	}*/
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String toString(){
		
		String result = "Configuration: \n"
				+ "maxTotalConnection = "+maxTotalConnection+"\n"
				+ "maxTotalPerRoute = " + maxTotalPerRoute+"\n"
				+ "connectionTimeout = " + connectionTimeout+"\n"
				+ "defaultUserAgent = " + defaultUserAgent+"\n";
		return result;
	}
}
