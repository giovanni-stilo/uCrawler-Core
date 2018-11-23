package it.stilo.uCrawler.actions.storage.mapdb;

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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.storage.StorageCheckerIF;
import java.net.MalformedURLException;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.Serializer;

/**
 *
 * @author metelli & musolino & stilo
 */
public class LocalStorageLoad implements StorageCheckerIF{

   
    private String mapName = "LocalStoragePage";
    private DB db;
    private Map<String, String> mapDB;
    private JsonParser parser = new JsonParser();

    public LocalStorageLoad(MapDBInstance dbInst){
        db= dbInst.getDb();       
    }
    public void setMapName(String mapName) {
        this.mapName = mapName;
        
        mapDB=db.createTreeMap(mapName)
                .keySerializer(BTreeKeySerializer.STRING)
                .valueSerializer(Serializer.STRING)
                .valuesStoredOutsideNodes(true).makeOrGet();       
    }
   

    public synchronized JsonObject load(String k) {
        if (mapDB.containsKey(k)) {
            return (JsonObject)parser.parse(mapDB.get(k));
        }
        return null;
    }

    /*
    public boolean doSomething(Page page) throws ActionsException {
        String k;
     
        k = page.getUri().toString();
        JsonObject loaded=load(k);
        
        Logger.getLogger(this.getClass()).info("Test data integrity for: "+page.getUri().toString()+" Exact matching: "
                +loaded.get("htmlPage").getAsString().equals(StringEscapeUtils.escapeHtml(page.getData())));
        return true;
    }*/

    @Override
    public long getCachedData(Page page) throws MalformedURLException {
        String k;
        
     
        k = page.getUri().toString();
        JsonObject loaded=load(k);   
        
        if(loaded!=null){
            Logger.getLogger(this.getClass()).info("Loaded: "+k);
            
            page.setData(StringEscapeUtils.unescapeHtml(loaded.get("htmlPage").getAsString()),loaded.get("revTime").getAsLong());
            return loaded.get("revTime").getAsLong();
        }
        return -1L;
    }

    @Override
    public boolean isInCache(Page page) throws MalformedURLException {
        return  mapDB.containsKey(page.getUri().toString());
    }

    @Override
    public Set<String> getKeySet() 
    {
          return mapDB.keySet();
    }
}
