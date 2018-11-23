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

import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author stilo
 */
public class MapDBInstance {

    private String dataDir = "./storage/local/MapDB-LocalStoragePage";
    private String storeName = "/CSI-LocalStoragePage";
    private DB db;
    private int cacheSize = 1000;

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @PostConstruct
    public void init() {
        if (!new File(dataDir).exists()) {

            try {
                FileUtils.forceMkdir(new File(dataDir));
            } catch (IOException ex) {
                Logger.getLogger(LocalStorage.class.getName()).log(Level.FATAL, null, ex);
            }
        }

        File envHome = new File(dataDir + storeName);

        db = DBMaker.newFileDB(envHome)
                .cacheSize(this.cacheSize)
                .cacheLRUEnable()
                .compressionEnable()
                .make();
    }

    public DB getDb() {
        return db;
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        synchronized (db) {
            if (db != null && !db.isClosed()) {
                db.commit();
                db.close();
            }
        }

    }

    public void destroy() throws Throwable {

        this.finalize();
    }
}
