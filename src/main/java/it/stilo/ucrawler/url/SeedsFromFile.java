package it.stilo.uCrawler.url;

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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import static org.apache.commons.io.FileUtils.lineIterator;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class SeedsFromFile {
    
    public SeedsFromFile(URLManagerIF manager, String path) 
            throws IOException, MalformedURLException, URISyntaxException {
        
        File in = new File(path);
        LineIterator it = lineIterator(in);
      
        this.generateSeeds(it, manager);

    }
    
    private void generateSeeds(LineIterator it, URLManagerIF manager) 
            throws MalformedURLException, URISyntaxException {
        
        String temp;
        
        while(it.hasNext()) {
            temp = (String)it.nextLine();
            Logger.getLogger(this.getClass()).info(temp);
            manager.reque(new Page(temp));
        }
    }
    
    
    
}
