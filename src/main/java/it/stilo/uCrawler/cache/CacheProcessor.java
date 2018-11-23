package it.stilo.uCrawler.cache;

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

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.storage.StorageCheckerIF;

/**
 *
 * @author stilo
 */
public class CacheProcessor {

	private final ActionIF action;
	private final StorageCheckerIF ck;

	public CacheProcessor(ActionIF action, StorageCheckerIF ck) {
		this.action = action;
		this.ck = ck;
	}

	public void read() throws ActionsException, MalformedURLException,
			URISyntaxException {

		for (String uri : ck.getKeySet()) {
			Page p = new Page(uri);
			long rev = ck.getCachedData(p);
			if (rev > 0) {
				p.setRevTime(rev);
				this.action.doSomething(p);
			}
		}
	}

}
