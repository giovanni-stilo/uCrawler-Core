package it.stilo.ucrawler.actions;

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
import java.net.URI;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class RegexUrlCleaner implements ActionIF {

    private Pattern filters; /*=  Pattern.compile((".*(\\.(css|xml|jpe?g|png|php|ico))$"));*/

    private boolean comply = false;

    /**
     * Accetta una pagina e rimuove gli url filtrati cioè che soddisfano la
     * regex
     *
     * @param page
     * @return
     */
    @Override
    public boolean doSomething(Page page) {
        if (page == null) {
            return false;
        }

        if (page.getFilteredLinks() != null) {
            List<URI> purgedUrls = purgeUrlsByRegex(page.getFilteredLinks());
            page.setFilteredLinks(purgedUrls);
        }

        return true;
    }

    private List<URI> purgeUrlsByRegex(List<URI> urls) {
        for (int x = 0; x < urls.size(); x++) {
            boolean filter = filters.matcher(urls.get(x).toString()).find();

            if (filter) {
                if (!comply) {
                    Logger.getLogger(this.getClass()).info(urls.remove(x));
                    x--;

                }
            } else {
                if (comply) {
                    Logger.getLogger(this.getClass()).info(urls.remove(x));
                    x--;
                }
            }
        }

        return urls;
    }

    @Required
    public void setRegex(String regex) {
        filters = Pattern.compile(regex);
    }

    public void setComply(boolean comply) {
        this.comply = comply;
    }
}
