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


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Required;

import it.stilo.ucrawler.page.Page;

/**
 *
 * @author stilo
 */
public class PutToListField extends WhereAB {
	
	private String fieldName;

	@Required
	@Override
	public void setWhere(String where) {
		this.where = where;
	}
	
	@Required
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public boolean putSomewhere(Object obj, Page p) {
		ArrayList<Object> list;
		HashMap<String, Object> contextMap = this.getFromContextAsHashMap(p);
		HashMap<String, Object> listElement;

		if (contextMap.get(this.getClass().getCanonicalName() + "." + where) == null || !contextMap.get(this.getClass().getCanonicalName() + "." + where).equals(p)) {
			list = new ArrayList<Object>();
			contextMap.put(this.getClass().getCanonicalName() + "." + where, p);
			contextMap.put(where, list);
		} else {
			list = (ArrayList<Object>) contextMap.get(where);
		}

		if(!list.isEmpty()) {
			listElement = (HashMap<String, Object>)list.get(list.size() - 1);
			if(listElement.get(fieldName) != null) {
				//Logger.getLogger(this.getClass()).info(p.getUri() + " " + listElement + " " + Integer.toString(list.size() - 1));
				listElement = new HashMap<String, Object>();
				listElement.put(fieldName, obj);
				list.add(listElement);
			} else {
				listElement.put(fieldName, obj);
			}
		} else {
			listElement = new HashMap<String, Object>();
			listElement.put(fieldName, obj);
			list.add(listElement);
		}

		return true;
	}

}
