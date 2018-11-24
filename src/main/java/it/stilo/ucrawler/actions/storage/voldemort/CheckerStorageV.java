package it.stilo.ucrawler.actions.storage.voldemort;

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

import it.stilo.ucrawler.page.Page;
import it.stilo.ucrawler.storage.StorageCheckerIF;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

/**
 *
 * @author stilo
 */
public class CheckerStorageV implements StorageCheckerIF {
    
    StoreClientFactory factory;
    String storeName;

    private StoreClient<String, HashMap<String, Object>> client;

    public void setFactory(StoreClientFactory factory) {
        this.factory = factory;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    
    @PostConstruct
    public void init() {
        client = factory.getStoreClient(storeName);
    }

    @Override
    public long getCachedData(Page page) throws MalformedURLException {
        String key = page.getUri().toURL().toString();
        Versioned<HashMap<String, Object>> res = client.get(key);
        if (res != null) 
            return reconstructPage(res, page);
        else
            return -1L;
            

    }    
    
    private long reconstructPage(Versioned<HashMap<String, Object>> res, Page page) throws MalformedURLException {
        
        
        page.setData((String) res.getValue().get("page"),(long) res.getValue().get("revtime"));
        

        Logger.getLogger(this.getClass()).info("Ricostruita Page "
                + page.getUri().toURL().toString()
                + " con revtime "
                + (long) res.getValue().get("revtime"));
        
        return Long.parseLong(res.getValue().get("revtime").toString());
    }

    @Override
    public boolean isInCache(Page page) throws MalformedURLException {
        String key = page.getUri().toURL().toString();
        return client.getValue(key)!=null;
    }
    @Override
    public Set<String> getKeySet() 
    {
          return new HashSet<String>();
    }
}
