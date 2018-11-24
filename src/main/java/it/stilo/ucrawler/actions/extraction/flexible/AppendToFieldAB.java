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

import java.util.Map;

import it.stilo.ucrawler.page.Page;

/**
 *
 * @author stilo
 */
public abstract class AppendToFieldAB extends WhereAB {

	protected Object post;
	protected Object pre;


	/**
	 * @return the post
	 */
	public Object getPost() {
		return post;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost(Object post) {
		this.post = post;
	}

	/**
	 * @return the pre
	 */
	public Object getPre() {
		return pre;
	}

	/**
	 * @param pre the pre to set
	 */
	public void setPre(Object pre) {
		this.pre = pre;
	}


    @Override
    public boolean putSomewhere(Object obj, Page p) {
        Map<String, Object> map = this.getFromContextAsHashMap(p);
        Object appended = append(obj, map.get(where), p);
        map.put(where, appended);
        return true;
    }

    public abstract Object append(Object obj, Object previous, Page p);
}
