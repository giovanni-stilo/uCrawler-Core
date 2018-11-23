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

import it.stilo.uCrawler.core.actions.ActionFilterAB;
import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.page.Page;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class UrlRegexBasedFilter extends ActionFilterAB {

    private Pattern filters;
    private boolean comply = false;

    public UrlRegexBasedFilter(ActionIF a) {
        super(a);
    }

    @Override
    public boolean check(Page page) {
        boolean filter = filters.matcher(page.getUri().toString()).find();

        if (filter) {
            if (!comply) {
                return false;


            }
        } else {
            if (comply) {
                return false;
            }
        }
        return true;
    }

    @Required
    public void setRegex(String regex) {
        filters = Pattern.compile(regex);
    }

    public void setComply(boolean comply) {
        this.comply = comply;
    }
}
