package it.stilo.uCrawler.actions.extraction.flexible;

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
import java.util.HashMap;

/**
 *
 * @author stilo
 */
public abstract class WhereAB {

    public abstract boolean putSomewhere(Object obj, Page p);

    protected HashMap<String, Object> getFromContextAsHashMap(Page p) {
        HashMap<String, Object> contextMap;
        if ((contextMap = (HashMap<String, Object>) p.getFromContext(this.getClass())) == null) {
            contextMap = new HashMap<>();
            contextMap.put(this.getClass().getCanonicalName(), p);
        } else {
            if (contextMap.get(this.getClass().getCanonicalName()) != p) {
                contextMap = (HashMap<String, Object>) contextMap.clone();
                contextMap.put(this.getClass().getCanonicalName(), p);
            }
        }
        p.setInContext(this.getClass(), contextMap);
        return contextMap;
    }
    protected String where = null;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}