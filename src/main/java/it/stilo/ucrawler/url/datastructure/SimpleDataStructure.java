package it.stilo.uCrawler.url.datastructure;

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

import it.stilo.uCrawler.page.Page;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author stilo
 */
public class SimpleDataStructure implements URLManagerDataStructureIF{
    
    private Queue<Page> urlQueue = new ConcurrentLinkedQueue<Page>();//new PriorityBlockingQueue<Page>(100, Page.getBFSComparator()); // BFS Crawling
    private Map<String,String> urlMap = new ConcurrentHashMap<String,String>();

    @Override
    public boolean isInMap(Page p) {
        return urlMap.containsKey(p.getUri().toString());
    }

    @Override
    public boolean isInQueue(Page p) {
        return urlQueue.contains(p);
    }

    @Override
    public void add(Page p) {    
            urlMap.put(p.getUri().toString(),p.getUri().toString());
            urlQueue.add(p);
    }

    @Override
    public int getSize() {
        return this.urlQueue.size();
    }

    @Override
    public Page getNext() {
        if (!urlQueue.isEmpty()) {
            return urlQueue.poll();
        } else {
            return null;
        }  
    } 
}
