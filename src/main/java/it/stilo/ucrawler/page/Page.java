package it.stilo.ucrawler.page;

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

import it.stilo.ucrawler.fetcher.FetcherAB;
import it.stilo.ucrawler.utils.UrlEncoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author stilo
 */
public class Page {

    //url della pagina
    private URI uri;
    //
    private long revTime;
    private long remainingDepth = 0;
    //contenuto della pagina
    private String data;
    private HashMap<Class, Object> context = new HashMap<Class, Object>();
    private static HashMap<Class, Object> shared = new HashMap<Class, Object>();
    private List<URI> filteredLinks;

    public void setFilteredLinks(List<URI> filteredLinks) {
        this.filteredLinks = filteredLinks;
    }

    public List<URI> getFilteredLinks() {
        return this.filteredLinks;
    }

    public Page(URI url) throws NullPointerException {
        if (url != null) {
            this.uri = url;
        } else {
            throw new NullPointerException("Url can't be null.");
        }
    }

    public Page(String urlString) throws URISyntaxException {
        uri = new URI(UrlEncoder.recover(urlString));
    }

    public Page(URI url, long depth) throws NullPointerException {
        if (url != null) {
            this.uri = url;
            this.remainingDepth = depth;
        } else {
            throw new NullPointerException("Uri can't be null.");
        }
    }

    public Page(String urlString, long depth) throws URISyntaxException {
        uri = new URI(UrlEncoder.recover(urlString));
        this.remainingDepth = depth;
    }

    public void setInContext(Class key, Object value) {
        context.put(key, value);
    }

    public Object getFromContext(Class key) {
        return context.get(key);
    }

    public String getData() {
        return data;
    }

    public void setData(String htmlPage) {    
        if (this.data == null) {
            this.revTime = System.currentTimeMillis();
            this.data = htmlPage;
        }
    }
    
    public void setData(String htmlPage,long revTime) {    
        if (this.data == null) {
            this.revTime = revTime;
            this.data = htmlPage;
        }
    }
    

    public URI getUri() {
        return uri;
    }

    public long getRevTime() {
        return revTime;
    }

    /**
     * Setter for revision time; work only if the invoker is a subclass of FetcherAB.
     * @param revTime 
     */
    public void setRevTime(long revTime) {
        try{
        sun.reflect.Reflection.getCallerClass(2).asSubclass(FetcherAB.class);
        this.revTime = revTime;
       }catch(Throwable tw){;}
    }

    public long getRemainingDepth() {
        return remainingDepth;
    }

    public void unsafeIneheritContext(Page parent) {
        if (parent != null) {
            for (Class cls : parent.context.keySet()) {
                this.context.put(cls, parent.context.get(cls));
            }
            this.remainingDepth = parent.remainingDepth - 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Page) {
            return ((Page) obj).uri.equals(this.uri);
        }
        return false;
    }

    @Override
    public Page clone() {
        Page p = new Page(this.uri);
        p.unsafeIneheritContext(this);
        p.remainingDepth = this.remainingDepth;
        p.filteredLinks = this.filteredLinks;
        p.data = this.data;
        p.revTime = this.revTime;
        return p;
    }

    private static class BFSComparator implements Comparator<Page> {

        @Override
        public int compare(Page o1, Page o2) {
            return (int) ((o2.remainingDepth - o1.remainingDepth) % 2);
        }
    }

    public static Comparator<Page> getBFSComparator() {
        return new BFSComparator();
    }

    private static class DFSComparator implements Comparator<Page> {

        @Override
        public int compare(Page o1, Page o2) {
            return (int) ((o1.remainingDepth - o2.remainingDepth) % 2);
        }
    }

    public static Comparator<Page> getDFSComparator() {
        return new DFSComparator();
    }
    
}
