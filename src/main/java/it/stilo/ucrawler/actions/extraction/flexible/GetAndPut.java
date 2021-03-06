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
import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * @author stilo
 */
public class GetAndPut implements ExtractionIF {

	private WhereAB where;
	private DoIF toDo;

	@Override
	public void extraction(Element element, Page p) throws ExtractionException {
		where.putSomewhere(toDo.get(element, p), p);
	}

	public WhereAB getWhere() {
		return where;
	}

	@Required
	public void setWhere(WhereAB where) {
		this.where = where;
	}

	public DoIF getToDo() {
		return toDo;
	}

	@Required
	public void setToDo(DoIF toDo) {
		this.toDo = toDo;
	}

}
