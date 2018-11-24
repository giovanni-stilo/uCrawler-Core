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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.io.FileUtils.lineIterator;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */

public class SeedsIgniter extends Thread {

    private URLManagerIF urlmanager;
    private long reSeedTime = -1;
    private List<Page> originals;    
    private String comment;
    

    private void setSeeds(List<Page> originals) {
        if (originals != null && this.originals == null) {
            this.originals = originals;
        }
    }

    private void setUrls(List<URI> originals) {
        if (originals != null && this.originals == null) {
            this.originals = new ArrayList<Page>();
            for (URI url : originals) {
                this.originals.add(new Page(url));
            }
        }
    }

    public SeedsIgniter(URLManagerIF urlmanager, List<URI> originalUrls) {
        this.urlmanager = urlmanager;
        this.setUrls(originalUrls);
        this.reSeed();
    }

    public SeedsIgniter(List<Page> originalPages, URLManagerIF urlmanager) {
        this.urlmanager = urlmanager;
        this.setSeeds(originalPages);
        this.reSeed();
    }
    
    public SeedsIgniter(URLManagerIF manager, String file) 
            throws IOException, MalformedURLException, URISyntaxException {
        this.urlmanager = manager;
        
        File in = new File(file);
        LineIterator it = lineIterator(in);
      
        this.setSeeds(this.generateSeeds(it));
        this.reSeed();
    }
    
    private List<Page> generateSeeds(LineIterator it) 
            throws MalformedURLException, URISyntaxException {
        ArrayList<Page> seeds= new ArrayList<Page>();
        String temp;
       
        if (comment != null) {
	        while(it.hasNext()) {
	            temp = (String)it.nextLine();
	            if (temp.startsWith(comment))
	            	continue;
	            Logger.getLogger(this.getClass()).info(temp);
	            seeds.add(new Page(temp));
	        }
    	}else {
    		while(it.hasNext()) {
    			temp = (String)it.nextLine();
    			Logger.getLogger(this.getClass()).info(temp);
    			seeds.add(new Page(temp));
    		}

    	}
        
        return seeds;
    }

    @Override
    public void run() {
        for (;;) {
            try {
                synchronized (this) {
                    this.wait(reSeedTime);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SeedsIgniter.class).debug(ex);
            }
            reSeed();
        }
    }

    public long getReSeedTime() {
        return reSeedTime;
    }

    public void setReSeedTime(long reSeedTime) {
        if (this.reSeedTime < 0 && reSeedTime >= 60000) {
            this.reSeedTime = reSeedTime;
            this.start();
        }
    }

    private void reSeed() {
        for (Page p : originals) {
            this.urlmanager.reque(p.clone());
        }        
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public void setClean(boolean clean) {
        if(clean)
            this.originals=null;
    }
    
    
}
