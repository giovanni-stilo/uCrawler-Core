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

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Required;

import it.stilo.ucrawler.page.Page;

/**
 *
 * @author stilo
 */
public class TryCatchExtractionException implements ExtractionIF {

	private ExtractionIF tryAction;
	private ExtractionIF catchAction;
	private ExtractionIF finallyAction;

	@Override
	public void extraction(Element element, Page p) throws ExtractionException {
		try {
			tryAction.extraction(element, p);
		}catch(ExtractionException e) {
			if (catchAction != null) {
				catchAction.extraction(element, p);
			}
		}finally {
			if (finallyAction != null) {
				finallyAction.extraction(element, p);
			}
		}
	}

	/**
	 * @return the tryAction
	 */
	public ExtractionIF getTryAction() {
		return tryAction;
	}

	/**
	 * @param tryAction the tryAction to set
	 */
	@Required
	public void setTryAction(ExtractionIF tryAction) {
		this.tryAction = tryAction;
	}

	/**
	 * @return the catchAction
	 */
	public ExtractionIF getCatchAction() {
		return catchAction;
	}

	/**
	 * @param catchAction the catchAction to set
	 */
	public void setCatchAction(ExtractionIF catchAction) {
		this.catchAction = catchAction;
	}

	/**
	 * @return the finallyAction
	 */
	public ExtractionIF getFinallyAction() {
		return finallyAction;
	}

	/**
	 * @param finallyAction the finallyAction to set
	 */
	public void setFinallyAction(ExtractionIF finallyAction) {
		this.finallyAction = finallyAction;
	}



}
