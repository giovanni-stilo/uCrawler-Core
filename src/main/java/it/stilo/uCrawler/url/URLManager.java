package it.stilo.uCrawler.url;

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

import it.stilo.uCrawler.url.datastructure.URLManagerDataStructureIF;
import it.stilo.uCrawler.page.Page;
import java.net.URI;
import java.util.List;

/**
 *
 * @author stilo & metelli & musolino
 */
public class URLManager implements URLManagerIF{
    
    private URLManagerDataStructureIF dataStructure;
    
    public URLManager(URLManagerDataStructureIF structure){
        this.dataStructure=structure;
    }

    @Override
    public void addPageSeeds(List<Page> pages) {    	
        if (pages != null) {
            for (Page p : pages) {
                reque(p);
            }
        }        
    }

    @Override
    public void addUrlSeeds(List<URI> urls) {    	
        for (URI url : urls) {
            reque(new Page(url));
        }        
    }

    @Override
    public synchronized void add(URI url) {    	
        this.add(url, null);        
    }
    
    @Override
    public synchronized void add(Page parent) {    	
        if (parent != null && parent.getFilteredLinks() != null) {
            List<URI> urls=parent.getFilteredLinks();
            for (int i=0;i<urls.size();i++ ) {
                add(urls.get(i), parent);
            }
        }        
    }
    
    @Override
    public synchronized Page getNext() {
        return dataStructure.getNext();    
    }   

    @Override
    public synchronized int getSize() {    	
        return dataStructure.getSize();
    }

    @Override
    public synchronized void add(URI url, Page parent) {    	
        Page p = new Page(url);
        p.unsafeIneheritContext(parent);
        
        if(!dataStructure.isInMap(p)){
            dataStructure.add(p);
        }     
    }    
 
    @Override
    public synchronized void reque(Page page) {
        if(!dataStructure.isInQueue(page)){
            dataStructure.add(page);
        }               
    } 
}
