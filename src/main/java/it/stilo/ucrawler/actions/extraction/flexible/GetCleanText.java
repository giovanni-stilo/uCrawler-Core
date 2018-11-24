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


import it.stilo.ucrawler.page.Page;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 * @author stilo
 */
public class GetCleanText extends GetStringByRegexAB {

	private String toRemove;
	
	@Required
    public void setToRemove(String toRemove) {
        this.toRemove = toRemove;
    }

	@Override
	protected String getText(Element element, Page p) throws ExtractionException {
		Elements els = element.children();
		els.select(toRemove).remove();
		return element.text();
	}
	
}