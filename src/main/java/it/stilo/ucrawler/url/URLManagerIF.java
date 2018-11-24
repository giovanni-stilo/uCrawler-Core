package it.stilo.uCrawler.url;

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
import java.net.URI;

import java.util.List;

/**
 *
 * @author stilo
 */
public interface URLManagerIF{
       
    public Page getNext();
    public void addUrlSeeds(List<URI> urls);
    public void addPageSeeds(List<Page> pages);
    public void add(URI url);
    public void add(URI url,Page parent);
    public void add(Page parent);
    public void reque(Page page);
    public int getSize();
   
}
