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
public class RouteToFirstAndPutToField extends RouteAB{

    
    public RouteToFirstAndPutToField(){
        GetAndPut doPut=new GetAndPut();
        PutToField inMem=new PutToField();
        inMem.setWhere(target);
        doPut.setWhere(inMem);
        this.action=doPut;
        
    }
    
    public DoIF getToDo() {
        return ((GetAndPut)this.action).getToDo();
    }
    @Required
    public void setToDo(DoIF toDo) {
        ((GetAndPut)this.action).setToDo(toDo);
    }
    
    public void setAction(ExtractionIF action) {;     
    }
    
    @Override
    public void extraction(Element element,Page p) throws ExtractionException {
        if(element!=null)
            this.action.extraction(element.select(this.target).first(),p);
    }  
}
