package it.stilo.uCrawler.core.actions;

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

import java.util.List;

/**
 *
 * @author stilo
 */
public class PipelinedAction implements ActionIF{

	private List<ActionIF> actions ;
	
	
	public List<ActionIF> getActions() {
		return actions;
	}


	public void setActions(List<ActionIF> actions) {
		this.actions = actions;
	}


	@Override
	public boolean doSomething(Page page) throws ActionsException {
	
		for(ActionIF a : actions)
		{
			if(!a.doSomething(page)) return false;
		}
		return true;
	}


}
