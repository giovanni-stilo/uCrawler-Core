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

import org.springframework.beans.factory.annotation.Required;

import it.stilo.uCrawler.concurrentFetcher.configuration.NumericConstraintIF;
import it.stilo.uCrawler.concurrentFetcher.configuration.UserAgentIF;
import it.stilo.uCrawler.concurrentFetcher.proxy.Proxy;
import it.stilo.uCrawler.core.actions.ActionIF;

/**
 *
 * @author stilo
 */
public abstract class DownloaderBuilderAB {

    protected ActionIF action;
    protected UserAgentIF userAgent;
    //numero di connessioni dopo il quale ricreare il client
    protected NumericConstraintIF maxConnections;
    //tempo tra una connessione ad una pagina e un'altra
    protected NumericConstraintIF connectionDelay;
    //numero di pagine prima di cambiare dominio
    protected NumericConstraintIF maxPageForDomain;


	public abstract Downloader create(Proxy proxy);
	
    @Required
    public void setAction(ActionIF action) {
        this.action=action;
    }
    
    @Required
	public void setmaxConnections(NumericConstraintIF maxConnections) {
		this.maxConnections = maxConnections;
	}
   
    @Required
	public void setConnectionDelay(NumericConstraintIF connectionDelay) {
		this.connectionDelay = connectionDelay;
	}	
    
    @Required
	public void setUserAgent(UserAgentIF userAgent) {
		this.userAgent = userAgent;
	}

    @Required
	public void setMaxPageForDomain(NumericConstraintIF maxPageForDomain) {
		this.maxPageForDomain = maxPageForDomain;
	}

	public ActionIF getAction() {
		return action;
	}
}
