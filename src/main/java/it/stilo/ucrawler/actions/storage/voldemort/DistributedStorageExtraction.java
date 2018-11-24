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

import it.stilo.ucrawler.actions.extraction.flexible.ExtractionException;
import it.stilo.ucrawler.actions.extraction.flexible.ExtractionIF;
import it.stilo.ucrawler.page.Page;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Version;

/**
 *
 * @author stilo
 */
public class DistributedStorageExtraction implements ExtractionIF {

    private String store;
    private StoreClient<String, Object> client;
    private StoreClientFactory factory;
    
    private String patternKey= "map.purl";
//     private String patternKey = "page.uri.host+'/'+map.pid.t+'/'+map.uid";

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
    public void extraction(Element element, Page page) throws ExtractionException {


        String key;
        key = page.getUri().getHost() /*+pid*/;

        //HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        //for(Classes){
        Object field = page.getFromContext(it.stilo.ucrawler.actions.extraction.flexible.PutToField.class);

        try {
            map.putAll((Map<String, Object>) field);
        } catch (ClassCastException ex) {
            map.put(it.stilo.ucrawler.actions.extraction.flexible.PutToField.class.getCanonicalName(), field);
        }

        Object obj = page;
       
        try {
            //"page.uri.host+'/'+map.pid+'/'+map.uid";
            obj=BeanUtils.getProperty(page, "uri");
//            obj=BeanUtils.getProperty(page, "host");
            
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DistributedStorageExtraction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(DistributedStorageExtraction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            java.util.logging.Logger.getLogger(DistributedStorageExtraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String stringToPut = new JSONObject(map).toString();
//        Logger.getLogger(this.getClass()).info(stringToPut);
        Version vv = client.put(key, stringToPut);
        Logger.getLogger(this.getClass()).info("Store "
                + key);

    }
}
