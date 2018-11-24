package it.stilo.ucrawler.url.mapstore;

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

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStore;

import it.stilo.ucrawler.url.datastructure.HazelcastDataStructure;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author metelli & musolino & stilo
 */
public class MapDBStore implements MapLoader<String, String>, MapStore<String, String> {

    private static final String dataDir = "./conf/MapDB-HZL2Cache";
    private static final String storeName = "/CSI-HZL2Cache";
    private static final String mapName = "HZL2Cache";
    private DB db;
    private Map<Integer, Integer> mapDB;
    private long tick = 0;
    private long compressTime = 10000;

    public MapDBStore() throws IOException {
        FileUtils.deleteDirectory(new File(dataDir));
        FileUtils.forceMkdir(new File(dataDir));
        File envHome = new File(dataDir + storeName);

        db = DBMaker.newFileDB(envHome)
                .cacheSize(500000)
                .closeOnJvmShutdown()
                .cacheLRUEnable()
                .transactionDisable()
                .make();
        
        mapDB = db.createTreeMap(mapName).keySerializer(BTreeKeySerializer.ZERO_OR_POSITIVE_INT).make();
    }

    @Override
    public synchronized String load(String k) {
        Logger.getLogger(HazelcastDataStructure.class).info("@load from MapDB: " + k);
        if (mapDB.containsKey(k.hashCode())) {
            return "" + mapDB.get(k.hashCode());
        }
        return null;
    }

    @Override
    public Map<String, String> loadAll(Collection<String> clctn) {
        return new HashMap<>();
    }

    @Override
    public Set<String> loadAllKeys() {
        return new HashSet<>();
    }

    @Override
    public synchronized void store(String k, String v) {
        Logger.getLogger(HazelcastDataStructure.class).info("@store in MapDB: " + k);
        mapDB.put(k.hashCode(), 1);        
        if (++this.tick == this.compressTime) {
            db.commit();
            db.compact();
            this.tick = 0;
        }
    }

    @Override
    public void storeAll(Map<String, String> map) {
        for (String t : map.keySet()) {
            mapDB.put(t.hashCode(), 1);
        }
        db.commit();
        db.compact();
    }

    @Override
    public void delete(String k) {
        mapDB.remove(k);        
        if (++this.tick == this.compressTime) {
            db.commit();
            db.compact();
            this.tick = 0;
        }
    }

    @Override
    public void deleteAll(Collection<String> clctn) {
        for (String t : clctn) {
            mapDB.remove(t);
        }
        db.commit();
        db.compact();
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (db != null) {
            db.close();
        }
        FileUtils.deleteDirectory(new File(dataDir));
    }
}
