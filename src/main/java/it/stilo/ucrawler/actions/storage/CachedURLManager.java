package it.stilo.uCrawler.actions.storage;

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

import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.storage.StorageCheckerIF;
import it.stilo.uCrawler.url.URLManager;
import it.stilo.uCrawler.url.datastructure.URLManagerDataStructureIF;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class CachedURLManager extends URLManager {

    private StorageCheckerIF ck;

    public StorageCheckerIF getCk() {
        return ck;
    }

    public void setCk(StorageCheckerIF ck) {
        this.ck = ck;
    }

    public CachedURLManager(URLManagerDataStructureIF structure) {
        super(structure);
    }

    @Override
    public synchronized void add(URI url, Page parent) {
        //Se pagina già presente in db non inserire
        try {
            if (!ck.isInCache(parent)) {
                super.add(url, parent);
                Logger.getLogger(this.getClass()).info("page ");
            }
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(CachedURLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
