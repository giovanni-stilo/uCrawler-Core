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

import org.springframework.beans.factory.annotation.Required;

import it.stilo.ucrawler.core.actions.ActionIF;
import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.page.Page;

/**
 *
 * @author stilo
 */
public class TryCatchActionException implements ActionIF {

	private ActionIF tryAction;
	private ActionIF catchAction;
	private ActionIF finallyAction;

	@Override
	public boolean doSomething(Page page) throws ActionsException {
		try {
			return tryAction.doSomething(page);
		}catch(ActionsException e) {
			if (catchAction != null) {
				return catchAction.doSomething(page);
			}
		}finally {
			if (finallyAction != null) {
				return finallyAction.doSomething(page);
			}
		}
		return true;
	}

	/**
	 * @return the tryAction
	 */
	public ActionIF getTryAction() {
		return tryAction;
	}

	/**
	 * @param tryAction the tryAction to set
	 */
	@Required
	public void setTryAction(ActionIF tryAction) {
		this.tryAction = tryAction;
	}

	/**
	 * @return the catchAction
	 */
	public ActionIF getCatchAction() {
		return catchAction;
	}

	/**
	 * @param catchAction the catchAction to set
	 */
	public void setCatchAction(ActionIF catchAction) {
		this.catchAction = catchAction;
	}
	/**
	 * @return the catchAction
	 */
	public ActionIF getFinallyAction() {
		return catchAction;
	}

	/**
	 * @param catchAction the catchAction to set
	 */
	public void setFinallyAction(ActionIF finallyAction) {
		this.finallyAction = finallyAction;
	}


}
