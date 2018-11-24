package it.stilo.ucrawler.url.datastructure;

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
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.io.FileUtils;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author stilo
 */
public class MapDBDataStructure implements URLManagerDataStructureIF {

    private int cacheSize = 1000;
    private String workingPath = "tmp/map-db-url-manager/";
    private String storeName = "defaultStoreName";

    private Map<String, String> urlMap;
    private Queue<Page> urlQueue = null;

    private URLManagerDataStructureIF queueDataStructure = null;

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public URLManagerDataStructureIF getQueueDataStructure() {
        return queueDataStructure;
    }

    public void setQueueDataStructure(URLManagerDataStructureIF queueDataStructure) {
        this.queueDataStructure = queueDataStructure;
    }

    public void init() throws IOException {
        FileUtils.deleteDirectory(new File(workingPath));
        FileUtils.forceMkdir(new File(workingPath));
        File envHome = new File(workingPath + storeName);

        DB db = DBMaker.newFileDB(envHome)
                .cacheSize(cacheSize)
                .closeOnJvmShutdown()
                .cacheLRUEnable()
                .transactionDisable()
                .make();

        urlMap = db.createTreeMap("uri-map").keySerializer(BTreeKeySerializer.STRING).make();

        if (queueDataStructure == null) {
            urlQueue = new ConcurrentLinkedQueue<Page>();
        }
    }

    @Override
    public boolean isInMap(Page p) {
        boolean ret = false;
        try {
            ret = urlMap.containsKey(p.getUri().toString());
        } catch (NullPointerException ex) {
            return false;
        }
        return ret;
    }

    @Override
    public boolean isInQueue(Page p) {
        if (queueDataStructure != null) {
            return queueDataStructure.isInQueue(p);
        }
        return urlQueue.contains(p);
    }

    @Override
    public void add(Page p) {
        urlMap.put(p.getUri().toString(), p.getUri().toString());

        if (queueDataStructure != null) {
            queueDataStructure.add(p);
        } else {
            urlQueue.add(p);
        }
    }

    @Override
    public int getSize() {
        if (queueDataStructure != null) {
            return queueDataStructure.getSize();
        }
        return this.urlQueue.size();
    }

    @Override
    public Page getNext() {
        if (queueDataStructure != null) {
            return queueDataStructure.getNext();
        }

        if (!urlQueue.isEmpty()) {
            return urlQueue.poll();
        } else {
            return null;
        }
    }

}
