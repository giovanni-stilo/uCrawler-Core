package it.stilo.ucrawler.url;

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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo
 */
public class LazySeedsIgniter extends Thread {

    private URLManagerIF urlmanager;
    private long timeSlot = 15 * 60 * 50;
    private long sleepTime = 30 * 1000;
    private long sTime = 0;
    private int size = 180 * 50;
    private int firstLoad = 8000000;

    private BufferedReader reader;
    
    private boolean end=false;
    private boolean info=false;

    public LazySeedsIgniter(URLManagerIF urlmanager, String filename) throws IOException{
        this.urlmanager = urlmanager;
        reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(filename))));
        
    }
    /*
    public void init() throws IOException, URISyntaxException{
	this.start();
    }*/

    public boolean getInfo() {
        return info;
    }

    public void setInfo(boolean info) {
        this.info = info;
    }
    
    public void setTimeSlot(long timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFirstLoad(int first) throws IOException, URISyntaxException {
        this.firstLoad = first;
        end = this.seedding(this.firstLoad);
    }

    @Override
    public void run() {
        int chunk = 0;
        try {         

            if (!end) {
                for (;;) {
                    sTime = System.currentTimeMillis();

                    Logger.getLogger(LazySeedsIgniter.class).info("Processing chunk number: " + (chunk++));
                    end = this.seedding(this.size);

                    if (end) {
                        Logger.getLogger(LazySeedsIgniter.class).info("Shoutdown Ignitier.");
                        break;
                    }
                    long wTime = timeSlot - (System.currentTimeMillis() - sTime);
                    if (wTime > 0) {
                        Logger.getLogger(LazySeedsIgniter.class).info("Sleeping for: " + wTime + " milliseconds.");
                        this.sleep(wTime);
                    }
                    while (urlmanager.getSize() > 0) {
                        this.sleep(sleepTime);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(LazySeedsIgniter.class).debug(ex);
        }

        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LazySeedsIgniter.class).debug(ex);
        }
    }

    private boolean seedding(int length) throws IOException, URISyntaxException {
        String line;
        for (int i = 0; i < length; i++) {
            line = this.reader.readLine();
            if (line == null) {
                return true;
            } else {
                this.reque(line);
                if(info)
                    Logger.getLogger(LazySeedsIgniter.class).info("Added Url: "+i);
            }
        }
        return false;
    }

    private void reque(String url) throws URISyntaxException {
        urlmanager.reque(new Page(url));
    }
}
