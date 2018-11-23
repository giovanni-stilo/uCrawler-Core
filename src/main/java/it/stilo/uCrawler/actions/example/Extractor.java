package it.stilo.uCrawler.actions.example;

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

import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.core.actions.ActionIF;
import java.util.ArrayList;
import java.util.List;

import it.stilo.uCrawler.page.Page;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author stilo
 */
public class Extractor implements ActionIF {

    @Override
    public boolean doSomething(Page page) throws ActionsException {
    	
    	//System.out.println("********* EXTRACTOR DO SOMETHING");
    	
    	if(page.getData() != null){
    		Document doc = Jsoup.parse(page.getData());
    		Elements urls = doc.select("[href]");
    		List<URI> urlLinks = new ArrayList<URI>();
    		URI url;
    		for (Element u : urls) {
    			//crea url con il path assoluto, skippa quelli vuoti
    			if (!u.attr("abs:href").isEmpty()) {
    				try {
    					url = new URI(u.attr("abs:href"));
    					urlLinks.add(url);
    				} catch (URISyntaxException ex) {
    					throw new ActionsException("Exception during url extraction in "+ this.getClass().getCanonicalName()+".",ex);
    				} 
    			}
    		}

    		//setto contesto per la pagina
    		page.setInContext(this.getClass(),new ArrayList<URI>().addAll(urlLinks));
    		page.setFilteredLinks(urlLinks);
        	//System.out.println("********* EXTRACTOR DO SOMETHING, size = "+urlLinks.size());
    		return true;
    	}
    	
    	return false;
    }
}