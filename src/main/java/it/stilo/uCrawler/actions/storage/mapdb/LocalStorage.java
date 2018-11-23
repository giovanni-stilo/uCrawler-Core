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


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.Serializer;

/**
 *
 * @author metelli & musolino & stilo
 */
public class LocalStorage implements ActionIF {

    private String mapName = "LocalStoragePage";
    private DB db;
    private BTreeMap<String, String> mapDB;
    private long tick = 0;
    private long compressTime = 10000;

    private boolean update = false;

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public LocalStorage(MapDBInstance dbInst) {
        db = dbInst.getDb();
    }

    public void setCompressTime(long compressTime) {
        this.compressTime = compressTime;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @PostConstruct
    public void init() {

        mapDB = db.createTreeMap(mapName)
                .keySerializer(BTreeKeySerializer.STRING)
                .valueSerializer(Serializer.STRING)
                .valuesStoredOutsideNodes(true).makeOrGet();

    }

    private String createJson(Page page) {
        JsonObject valuesJson = new JsonObject();
        valuesJson.addProperty("revTime", page.getRevTime());
        valuesJson.addProperty("htmlPage", StringEscapeUtils.escapeHtml(page.getData()));

        return gson.toJson(valuesJson);
    }

    public synchronized void store(String k, String v) {
        if (!mapDB.containsKey(k) || update) {
            mapDB.put(k, v);
            Logger.getLogger(this.getClass()).info("Written " + k);

        }

        if (++this.tick == this.compressTime) {
            db.commit();
            //db.compact();

            this.tick = 0;
        }
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        store(page.getUri().toString(), createJson(page));

        return true;
    }
}
