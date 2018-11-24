package it.stilo.ucrawler.crawler.apps;

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

import it.stilo.ucrawler.crawler.Crawler;
import it.stilo.ucrawler.fetcher.FetcherAB;
import it.stilo.ucrawler.page.Page;
import it.stilo.ucrawler.url.URLManagerIF;

import org.apache.log4j.Logger;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author stilo
 */
public class HashTwubs {

    private URLManagerIF urlManager;
    private FetcherAB fetcher;
    private long sleepTime = 1000;

    public URLManagerIF getManager() {
        return (URLManagerIF) urlManager;
    }

    public void setManager(URLManagerIF manager) {
        this.urlManager = manager;
    }

    public FetcherAB getFetcher() {
        return (FetcherAB) fetcher;
    }

    public void setFetcher(FetcherAB fetcher) {
        this.fetcher = fetcher;
    }

    public void setSleepTime(long a) {
        this.sleepTime = a;
    }

    public void startCrawling() throws Throwable {
        Page p = null;
        for (;;) {
            if ((p = urlManager.getNext()) != null) {
                Logger.getLogger(this.getClass()).info("next url  = " + p.getUri());
                fetcher.fetchPage(p);
                Logger.getLogger(this.getClass()).info("Current size of queue: " + urlManager.getSize());
            } else {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                    Logger.getLogger(this.getClass()).info("No more URLS; sleep for: " + this.sleepTime);
                }

            }
        }
    }
    

    public static void main(String[] args) throws Throwable {
        // create and configure beans 
        AbstractApplicationContext context = new FileSystemXmlApplicationContext(new String[]{"conf/hashtag.xml"});
        // retrieve configured instance
        context.registerShutdownHook();
        Crawler c = (Crawler) context.getBean("crawler");
        c.startCrawling();

    }
}
