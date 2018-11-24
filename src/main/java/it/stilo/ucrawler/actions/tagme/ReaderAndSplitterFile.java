package it.stilo.ucrawler.actions.tagme;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author ditommaso & stilo
 */
public class ReaderAndSplitterFile implements ActionIF {

    DB db;
    ConcurrentNavigableMap<String, String> map;

    private String fileName = ("/Users/ditommaso/Code/gdelt-tsv/filteredNews");
    private String mapName = ("/Users/ditommaso/Code/gdelt-tsv/testdb");

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {

        String[] line = map.get(page.getUri().toString()).split("\n");
        ArrayList<String[]> l = new ArrayList<String[]>();

        for (String s : line) {
            l.add(s.split("\t"));
        }

        page.setInContext(this.getClass(), l);
        return true;
    }

    public void init() throws URISyntaxException {

        FileReader in = null;
        try {
            in = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReaderAndSplitterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        db = DBMaker.newFileDB(new File(mapName))
                .cacheLRUEnable()
                .compressionEnable()
                .make();

        map = db.createTreeMap("collection")
                .valuesStoredOutsideNodes(true).makeOrGet();

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

}
