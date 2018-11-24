package it.stilo.uCrawler.actions;

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

import org.springframework.beans.factory.annotation.Required;

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;

/**
 *
 * @author stilo
 */
public class SetInContext implements ActionIF {

	private Class<?> where;
	private Object what = null;

	@Override
	public boolean doSomething(Page page) throws ActionsException {
		page.setInContext(where, what);
		return true;
	}

	/**
	 * @return the where
	 */
	public Class<?> getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	@Required
	public void setWhere(Class<?> where) {
		this.where = where;
	}

	/**
	 * @return the what
	 */
	public Object getWhat() {
		return what;
	}

	/**
	 * @param what the what to set
	 */
	public void setWhat(Object what) {
		this.what = what;
	}

}
