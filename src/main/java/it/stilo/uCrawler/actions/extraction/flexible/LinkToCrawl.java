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
import it.stilo.uCrawler.utils.UrlEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jsoup.nodes.Element;

/**
 * 
 * @author stilo
 */
public class LinkToCrawl implements ExtractionIF {

	@Override
	public void extraction(Element element, Page p) throws ExtractionException {
		if (element != null) {
			try {
				String absurl = UrlEncoder.recover(element.absUrl("href"));
				if (!absurl.isEmpty()) {
					if (p.getFilteredLinks() == null) {
						p.setFilteredLinks(new ArrayList<URI>());
					}

					p.getFilteredLinks().add(new URI(absurl));
				}
			} catch (URISyntaxException ex) {
				throw new ExtractionException(ex);
			}
		}
	}
}
