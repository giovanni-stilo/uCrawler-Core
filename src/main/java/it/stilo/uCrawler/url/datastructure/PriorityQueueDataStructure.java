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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author stilo
 */
public class PriorityQueueDataStructure implements URLManagerDataStructureIF {
    
   private Queue<Page> urlQueue = new ConcurrentLinkedQueue<Page>();//new PriorityBlockingQueue<Page>(100, Page.getBFSComparator()); // BFS Crawling
    private Queue<Page> specialQueue = new ConcurrentLinkedQueue<Page>();
    private String pattern = "users/show.json";
    private boolean normal = true;
    private int defSwitch = 500;
    private Integer cSwitch = 500;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getSwitch() {
        return defSwitch;
    }

    public void setSwitch(int defSwitch) {
        this.defSwitch = defSwitch;
    }
    
    @Override
    public boolean isInMap(Page p) {
        return false;
    }

    @Override
    public boolean isInQueue(Page p) {
        return urlQueue.contains(p) || specialQueue.contains(p);
    }

    @Override
    public void add(Page p) {
        String url = p.getUri().toString();
        if (url.contains(pattern)) {
            specialQueue.add(p);
        } else {
            urlQueue.add(p);
        }
    }

    @Override
    public int getSize() {
        return this.urlQueue.size() + this.specialQueue.size();
    }

    @Override
    public Page getNext() {
        if (normal) {
            synchronized (cSwitch) {
                if (cSwitch > 0) {
                    cSwitch--;
                }else{
                    cSwitch = this.defSwitch;
                    normal=false;
                }
            }

            if (!specialQueue.isEmpty()) {
                return specialQueue.poll();
            }
            if (!urlQueue.isEmpty()) {
                return urlQueue.poll();
            } else {
                return null;
            }
        } else {
            normal = true;
            if (!urlQueue.isEmpty()) {
                return specialQueue.poll();
            }
            if (!specialQueue.isEmpty()) {
                return urlQueue.poll();
            } else {
                return null;
            }
        }
    }
}

