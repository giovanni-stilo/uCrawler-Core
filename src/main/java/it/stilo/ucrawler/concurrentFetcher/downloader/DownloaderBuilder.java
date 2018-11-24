package it.stilo.uCrawler.concurrentFetcher.downloader;

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

import it.stilo.uCrawler.concurrentFetcher.proxy.Proxy;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class DownloaderBuilder extends DownloaderBuilderAB {

	private HttpClientConnectionManager connManager;
	
	@Override
	public Downloader create(Proxy proxy) {
				
    	Logger.getLogger(this.getClass()).info("Create downloader invocato");

    	Downloader d = new Downloader(proxy, connManager, action, maxConnections, connectionDelay.clone(), maxPageForDomain,
				 userAgent);
  
		return d;
	}
	
	@Required
	public void setConnManager(HttpClientConnectionManager connMan) {
		this.connManager = connMan;
	}

}
