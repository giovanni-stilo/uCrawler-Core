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


import java.util.ArrayDeque;
import java.util.HashMap;

import org.apache.log4j.Logger;

import it.stilo.ucrawler.page.Page;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class PutToBreadCrumbUniq extends WhereAB {

    @Required
    @Override
    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public boolean putSomewhere(Object obj, Page p) {

        HashMap<String, Object> contextMap = this.getFromContextAsHashMap(p);

        ArrayDeque<Object> breadcrumb;

        if ((breadcrumb = (ArrayDeque<Object>) contextMap.get(where)) == null) {
            breadcrumb = new ArrayDeque<>();
            breadcrumb.add(obj);
            contextMap.put(where, breadcrumb);
        } else if (!breadcrumb.contains(obj)) {
            breadcrumb = breadcrumb.clone();
            breadcrumb.add(obj);
            contextMap.put(where, breadcrumb);
        } 
        
        Logger.getLogger(this.getClass()).info("Page breadcrumb: " + buildBreadcrumbString(breadcrumb));
        
        return true;
    }

    /**
     * Costruisce una stringa che mostra la posizione della pagina all'interno
     * del forum.
     *
     * @param breadcrumb l'oggetto di classe ArrayDeque presente nel contesto di
     * una pagina di un forum
     * @return la stringa che rappresenta la posizione della pagina, con il
     * simbolo ">" a separare i livelli
     */
    private static String buildBreadcrumbString(ArrayDeque<Object> breadcrumb) {
        StringBuilder breadcrumbBuilder = new StringBuilder();

        for (Object s : breadcrumb) {
            breadcrumbBuilder.append(s);
            breadcrumbBuilder.append(" > ");
        }

        // cancella l'ultimo simbolo usato per dividere gli elementi, dato che,
        // per come viene costruita la stringa, a seguire non si trova nulla
        breadcrumbBuilder.delete(breadcrumbBuilder.lastIndexOf(" > "),
                breadcrumbBuilder.length() - 1);

        return breadcrumbBuilder.toString();
    }
}
