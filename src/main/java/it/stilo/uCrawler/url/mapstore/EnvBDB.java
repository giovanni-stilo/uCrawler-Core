package it.stilo.uCrawler.url.mapstore;

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

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

import java.io.File;

/**
 *
 * @author stilo
 */
public class EnvBDB {

    private Environment myEnv;
    private EntityStore store;

    // Our constructor does nothing
    public EnvBDB() {}

    // The setup() method opens the environment and store
    // for us.
    public void setup(String envPath,String storeName, boolean readOnly) 
        throws DatabaseException {
        File envHome = new File(envPath);

        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        StoreConfig storeConfig = new StoreConfig();

        myEnvConfig.setReadOnly(readOnly);
        
        storeConfig.setReadOnly(readOnly);
        storeConfig.setDeferredWrite(true);       

        // If the environment is opened for write, then we want to be 
        // able to create the environment and entity store if 
        // they do not exist.
        myEnvConfig.setAllowCreate(!readOnly);
        storeConfig.setAllowCreate(!readOnly);

        // Open the environment and entity store
        myEnv = new Environment(envHome, myEnvConfig);
        
        store = new EntityStore(myEnv, storeName, storeConfig);
    }

    // Return a handle to the entity store
    public EntityStore getEntityStore() {
        return store;
    }

    // Return a handle to the environment
    public Environment getEnv() {
        return myEnv;
    }
    
    public void flush(){
        store.sync();
    }

    // Close the store and environment
    public void close() throws DatabaseException{
        if (store != null) {  
            store.sync();
            store.close();
        }

        if (myEnv != null) {
            myEnv.compress();
            myEnv.close();
        }
    }
    
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        this.close();
    }
}
