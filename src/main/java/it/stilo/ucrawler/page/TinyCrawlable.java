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

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

/**
 *
 * @author stilo
 */
public class TinyCrawlable implements Serializable {

    private URI uri;
    private long remainingDeepth = 0;

    public TinyCrawlable(Page p) throws URISyntaxException {
        this.uri = p.getUri();
        this.remainingDeepth = p.getRemainingDepth();
    }

    public Page createFullPage() {
        return new Page(this.uri, this.remainingDeepth);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TinyCrawlable) {
            return ((TinyCrawlable) obj).uri.equals(this.uri);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }
    
    @Override
    public String toString(){
        return this.uri.toString();
    }

    private static class BFSComparator implements Comparator<TinyCrawlable> {
        @Override
        public int compare(TinyCrawlable o1, TinyCrawlable o2) {
            return (int) ((o2.remainingDeepth - o1.remainingDeepth) % 2);
        }
    }
    
    public static Comparator<TinyCrawlable> getBFSComparator(){
        return new BFSComparator();
    }
    
    private static class DFSComparator implements Comparator<TinyCrawlable> {
        @Override
        public int compare(TinyCrawlable o1, TinyCrawlable o2) {
            return (int) ((o1.remainingDeepth - o2.remainingDeepth) % 2);
        }
    }
    
    public static Comparator<TinyCrawlable> getDFSComparator(){
        return new DFSComparator();
    }
}
