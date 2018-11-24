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

import it.stilo.ucrawler.core.actions.ActionIF;
import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.page.Page;
import java.net.MalformedURLException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;


import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Version;

/**
 *
 * @author stilo
 */
public class DistributedStorage implements ActionIF{

    private String store;
    private StoreClient<String, Object> client;
    private StoreClientFactory factory;

    public StoreClientFactory getFactory() {
        return factory;
    }

    public void setFactory(StoreClientFactory factory) {
        this.factory = factory;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass()).info("Iniziato il client");
        client = factory.getStoreClient(store);

    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        
        String key;
        
        try {
            key = page.getUri().toURL().toString();
            //Formato store: {"revtime":"int64", "page":"string"}

            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("revtime", page.getRevTime());
            map.put("page", page.getData());

            Version vv =  client.put(key, map);

            Logger.getLogger(this.getClass()).info("Store "
                    + key);

        } catch (MalformedURLException ex) {
            Logger.getLogger(DistributedStorage.class).debug(ex);
        }

        return true;
    }

}