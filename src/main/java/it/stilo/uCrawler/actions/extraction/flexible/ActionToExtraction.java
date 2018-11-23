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

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author stilo
 */
public class ActionToExtraction implements ActionIF {

    private ExtractionIF action = null;

    public ExtractionIF getAction() {
        return action;
    }

    public void setAction(ExtractionIF action) {
        this.action = action;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {

        if (page.getData() != null) {

            Document doc = Jsoup.parse(page.getData(), page.getUri().toString());
            try {
                this.action.extraction(doc, page);
            } catch (ExtractionException ex) {
                throw new ActionsException(ex);
            }
            return true;
        }

        return false;
    }
}
