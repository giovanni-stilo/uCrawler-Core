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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

/**
 *
 * @author stilo
 */
public class DistributedStorageLoadTest implements ActionIF {

    private StoreClient<String, HashMap<String, Object>> client;
    private String store;
    private StoreClientFactory factory;

    
    @PostConstruct
    public void init() {
        org.apache.log4j.Logger.getLogger(this.getClass()).info("Iniziato il client");
        client = factory.getStoreClient(store);

    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setFactory(StoreClientFactory factory) {
        this.factory = factory;
    }
    
    

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        String key;
        try {
            key = page.getUri().toURL().toString();
            Versioned<HashMap<String, Object>> res = client.get(key);


            org.apache.log4j.Logger.getLogger(this.getClass()).info("Test data integrity for: "
                    + key +" Exact matching: " +
                    res.getValue().get("page").equals(page.getData()));

        } catch (MalformedURLException ex) {
            Logger.getLogger(DistributedStorageLoadTest.class.getName()).log(Level.SEVERE, null, ex);
        }


        return true;


    }
}
