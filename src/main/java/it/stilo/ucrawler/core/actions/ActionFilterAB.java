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

/**
 *
 * @author stilo
 */
public abstract class ActionFilterAB implements ActionIF{
    private ActionIF action;
    
    public ActionFilterAB(ActionIF action){
        this.action=action;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        if(check(page))
            return action.doSomething(page);
        
        return true;
    }
    
    public abstract boolean check(Page page);
    
}
