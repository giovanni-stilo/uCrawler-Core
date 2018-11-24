package it.stilo.ucrawler.fetcher;

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
import it.stilo.ucrawler.page.Page;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public abstract class FetcherAB {

    protected ActionIF action;

    @Required
    public void setAction(ActionIF action) {
        this.action = action;
    }

    public abstract void fetchPage(Page page) throws Throwable;
}
