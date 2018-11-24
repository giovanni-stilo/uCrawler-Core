package it.stilo.uCrawler.url.mapstore.test;

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

import com.hazelcast.core.HazelcastInstance;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author musolino & stilo
 */

public class Dispari implements MapStoreTestObjectManagerDataStructureIF{

    private ConcurrentMap<String, String> urlMap;
    
    private HazelcastInstance instance;
    private long sleepTime;
    
    public Dispari(HazelcastInstance instance, long sleepTime) {
        this.instance = instance;

        this.urlMap = instance.getMap("csi.uri.map");     
     
        this.sleepTime = sleepTime;
        
        try {
            Thread.sleep(this.sleepTime);            
        } catch (InterruptedException ex) {
            Logger.getLogger(Dispari.class.getName()).log(Level.FATAL, null, ex);
        }
      
        for(int i=0; i<1000000; i++){
                Logger.getLogger(this.getClass()).info("Get of: "+"http://www.test.com/"+Integer.toString(i)+": "+urlMap.get("http://www.test.com/"+Integer.toString(i)));
        }
        
        for(;;){
            try {
                Thread.sleep(3000);
                Logger.getLogger(this.getClass()).info("Map Size: "+getSize());
            } catch (InterruptedException ex) {
                Logger.getLogger(Dispari.class.getName()).log(Level.FATAL, null, ex);
            }
        }
        
    }

    @Override
    public int getSize() {
        return this.urlMap.size();
    }
    
}
