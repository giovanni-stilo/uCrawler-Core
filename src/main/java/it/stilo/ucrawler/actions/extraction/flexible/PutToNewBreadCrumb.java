package it.stilo.ucrawler.actions.extraction.flexible;

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


import java.util.ArrayDeque;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import it.stilo.ucrawler.page.Page;

/**
 *
 * @author stilo
 */
public class PutToNewBreadCrumb extends WhereAB {

	//private String splitter = null;

	@Required
	@Override
	public void setWhere(String where) {
		this.where = where;
	}

	/*public void setSplitter(String splitter) {
		this.splitter = splitter;
	}*/

	@Override
	public boolean putSomewhere(Object obj, Page p) {
		HashMap<String, Object> contextMap = this.getFromContextAsHashMap(p);
                ArrayDeque<Object> breadcrumb;
                
                if(contextMap.get(this.getClass().getCanonicalName()+"."+where)==null || !contextMap.get(this.getClass().getCanonicalName()+"."+where).equals(p)){
                    breadcrumb = new ArrayDeque<Object>();
                    contextMap.put(this.getClass().getCanonicalName()+"."+where, p);
                    contextMap.put(where, breadcrumb);
                }
                else
                    breadcrumb = (ArrayDeque<Object>)contextMap.get(where);
                
                breadcrumb.add(obj);
		
		Logger.getLogger(this.getClass()).info(p.getUri() + " - " + breadcrumb);

		return true;
	}

}
