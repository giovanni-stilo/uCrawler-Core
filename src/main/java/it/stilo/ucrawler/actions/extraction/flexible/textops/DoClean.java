package it.stilo.uCrawler.actions.extraction.flexible.textops;

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

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Required;

import it.stilo.uCrawler.actions.extraction.flexible.DoIF;
import it.stilo.uCrawler.actions.extraction.flexible.ExtractionException;
import it.stilo.uCrawler.page.Page;


/**
 *
 * @author stilo
 */
public class DoClean implements DoIF {

	private PerformerIF performer;
	private DoIF toDo;


	/**
	 * @return the toDo
	 */
	public DoIF getToDo() {
		return toDo;
	}


	/**
	 * @param toDo the toDo to set
	 */
	@Required
	public void setToDo(DoIF toDo) {
		this.toDo = toDo;
	}


	@Override
	public Object get(Element element, Page p) throws ExtractionException {
		return performer.performOp(toDo.get(element, p));
	}


	/**
	 * @return the action
	 */
	public PerformerIF getPerformer() {
		return performer;
	}


	/**
	 * @param action the action to set
	 */
	@Required
	public void setPerformer(PerformerIF action) {
		this.performer = action;
	}

}