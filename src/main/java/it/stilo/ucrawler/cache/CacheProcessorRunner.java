package it.stilo.ucrawler.cache;

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

import it.stilo.ucrawler.core.actions.ActionsException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author stilo
 */
public class CacheProcessorRunner {

	private List<CacheProcessor> processors;

	public CacheProcessorRunner(CacheProcessor processor) {
		processors = new ArrayList<CacheProcessor>(1);
		processors.add(processor);
	}

	public CacheProcessorRunner(List<CacheProcessor> processors) {
		this.processors = processors;
	}
	
	public void run() throws MalformedURLException, ActionsException, URISyntaxException {
		for(CacheProcessor proc : this.processors)
			proc.read();
	}
	
    public static void main(String[] arg) throws ActionsException, MalformedURLException, URISyntaxException {
        // create and configure beans
        AbstractApplicationContext context = new FileSystemXmlApplicationContext(new String[]{"conf/giant/main_cache.xml"});
        context.registerShutdownHook();
        
        // retrieve configured instance
        CacheProcessorRunner c = (CacheProcessorRunner) context.getBean("runner");
        c.run();
    }

}
