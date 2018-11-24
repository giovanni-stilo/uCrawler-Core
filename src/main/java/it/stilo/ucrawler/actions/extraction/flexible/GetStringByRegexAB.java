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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;

/**
 *
 * @author stilo
 */
public abstract class GetStringByRegexAB implements DoIF {

    private Pattern filters;

    public void setRegex(String regex) {
        filters = Pattern.compile(regex);
    }

    protected String extract(String input) {
        if (filters != null) {
            Matcher matcher = filters.matcher(input);

            if (matcher.find()) {
                return matcher.group();                
            }
        }
        return input;
    }

    public Object get(Element element, Page p) throws ExtractionException {
    	if(element != null)
    		return this.extract(this.getText(element, p));
    	
    	return null;
    }

    protected abstract String getText(Element element, Page p) throws ExtractionException;
}
