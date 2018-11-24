package it.stilo.uCrawler.actions.storage;

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

import it.stilo.uCrawler.fetcher.FetcherAB;
import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.storage.StorageCheckerIF;


/**
 *
 * @author stilo & telesa
 */
public class CachedFetcher extends FetcherAB {

    private FetcherAB fetcher;
    private StorageCheckerIF ck;

    public StorageCheckerIF getCk() {
        return ck;
    }

    public void setCk(StorageCheckerIF ck) {
        this.ck = ck;
    }
    
    
    public CachedFetcher(FetcherAB fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void fetchPage(Page page) throws Throwable {
        long rev=ck.getCachedData(page);
        
        if ( rev < 0 ) {
            if(fetcher!=null)
                fetcher.fetchPage(page);
        } else {
            page.setRevTime(rev);
            this.action.doSomething(page);
        }
    }
}