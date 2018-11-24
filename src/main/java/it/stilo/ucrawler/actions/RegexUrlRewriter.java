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


import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class RegexUrlRewriter implements ActionIF {

    private Pattern regexToReplace;
    private String replacement = "";

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        if (page == null) {
            return false;
        }

        if (page.getFilteredLinks() != null) {
            List<URI> rewrittenUrls = rewriteUrlsByRegex(page.getFilteredLinks());
            page.setFilteredLinks(rewrittenUrls);
        }

        return true;
    }

    private List<URI> rewriteUrlsByRegex(List<URI> urls) throws ActionsException {
        for (int x = 0; x < urls.size(); x++) {
        	
        	Logger.getLogger(this.getClass()).info("old " + urls.get(x));

            try {
				urls.add(x, new URI(regexToReplace.matcher(urls.remove(x).toString()).replaceAll(replacement)));
			} catch (URISyntaxException e) {
				throw new ActionsException(e);
			}
            
            Logger.getLogger(this.getClass()).info("new " + urls.get(x));
            
        }

        return urls;
    }

    @Required
	public void setRegexToReplace(String regex) {
		this.regexToReplace = Pattern.compile(regex);
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

}
