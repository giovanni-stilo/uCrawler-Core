package it.stilo.ucrawler.actions.storage.mapdb;

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
import it.stilo.ucrawler.core.actions.ActionIF;
import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.page.Page;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author metelli & musolino & stilo
 */
public class LocalStorageTestLoad implements ActionIF {

   
    private String mapName = "LocalStoragePage";
    private DB db;
    private Map<String, String> mapDB;
    private JsonParser parser = new JsonParser();

    public LocalStorageTestLoad(MapDBInstance dbInst){
        db= dbInst.getDb();       
    }
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @PostConstruct
    public void init() {     
        mapDB = db.getTreeMap(mapName);
    }

    public synchronized JsonObject load(String k) {
        if (mapDB.containsKey(k)) {
            return (JsonObject)parser.parse(mapDB.get(k));
        }
        return null;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        String k;
     
        k = page.getUri().toString();
        JsonObject loaded=load(k);
        
        Logger.getLogger(this.getClass()).info("Test data integrity for: "+page.getUri().toString()+" Exact matching: "
                +loaded.get("htmlPage").getAsString().equals(StringEscapeUtils.escapeHtml(page.getData())));
        return true;
    }
}
