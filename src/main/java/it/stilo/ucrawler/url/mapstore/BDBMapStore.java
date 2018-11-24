package it.stilo.uCrawler.url.mapstore;

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
import com.sleepycat.persist.PrimaryIndex;
import it.stilo.uCrawler.url.datastructure.HazelcastDataStructure;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class BDBMapStore implements MapLoader<String, String>, MapStore<String, String> {

    private final PrimaryIndex<java.lang.Integer, URIEntity> uriIndex;
    private static final String dataDir= "./conf/HZL2Cache";
    private static final String storeName= "CSI-HZL2Cache";
    private EnvBDB env;
    private long tick=0;
    private long compressTime= 10000;

    public BDBMapStore() throws IOException {
    	FileUtils.deleteDirectory(new File(dataDir));
        FileUtils.forceMkdir(new File(dataDir));
        env = new EnvBDB();
        env.setup(dataDir, storeName, false);
        uriIndex = env.getEntityStore().getPrimaryIndex(java.lang.Integer.class, URIEntity.class);
    }

    @Override
    public synchronized String load(String k) {
        Logger.getLogger(HazelcastDataStructure.class).info("@load from BDB: "+k);
        if(uriIndex.contains(k.hashCode()))
            return uriIndex.get(k.hashCode()).getUri();
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
        Logger.getLogger(HazelcastDataStructure.class).info("@store in BDB: "+k);
        uriIndex.put(new URIEntity(k));
        env.getEntityStore().sync();
        if(++this.tick==this.compressTime)
        {
            env.getEnv().compress();
            this.tick=0;
        }
    }

    @Override
    public void storeAll(Map<String, String> map) {
        for (String t : map.keySet()) {
            uriIndex.put(new URIEntity(t));
        }
        env.getEntityStore().sync();
        env.getEnv().compress();
    }

    @Override
    public void delete(String k) {
        this.uriIndex.delete(k.hashCode());
        env.getEntityStore().sync();
        if(++this.tick==this.compressTime)
        {
            env.getEnv().compress();
            this.tick=0;
        }
    }

    @Override
    public void deleteAll(Collection<String> clctn) {
        for (String t : clctn) {
            this.uriIndex.delete(t.hashCode());
        }
        env.getEntityStore().sync();
        env.getEnv().compress();
    }
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        env.close();
        FileUtils.deleteDirectory(new File(dataDir));
    }
}
