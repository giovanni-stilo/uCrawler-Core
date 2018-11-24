package it.stilo.ucrawler.concurrentFetcher.proxy;

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

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;


/**
 *
 * @author stilo
 */
public class ProxyAddress {

	private HttpHost httpHost;
	private UsernamePasswordCredentials credentials;
	private float pageQualityThreshold;
    private float proxyQualityThreshold; 
    private CredentialsProvider credentialsProvider;

    @PostConstruct
    private void init(){
    	credentialsProvider = new BasicCredentialsProvider();
    	credentialsProvider.setCredentials(
                new AuthScope(httpHost.getHostName(), httpHost.getPort()),
        		credentials);
    }
    
	public void setHttpHost(HttpHost httpHost) {
		this.httpHost = httpHost;
	}
	
	public HttpHost getHttpHost() {
		return httpHost;
	}

	public float getPageQualityThreshold() {
		return pageQualityThreshold;
	}

	public void setPageQualityThreshold(float pageQualityThreshold) {
		this.pageQualityThreshold = pageQualityThreshold;
	}

	public float getProxyQualityThreshold() {
		return proxyQualityThreshold;
	}

	public void setProxyQualityThreshold(float proxyQualityThreshold) {
		this.proxyQualityThreshold = proxyQualityThreshold;
	}

	public void setCredentials(UsernamePasswordCredentials credentials) {
		this.credentials = credentials;
	}

	public CredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}
}
