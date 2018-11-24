package it.stilo.uCrawler.storage.db;

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
import it.stilo.uCrawler.storage.StorageIF;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import javax.sql.DataSource;

/**
 *
 * @author stilo
 */
public class JDBCStorage implements StorageIF {

    private DataSource dataSource;

    public JDBCStorage(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    public long loadData(Page page) throws SQLException {

        Connection con = this.dataSource.getConnection();
        con.setAutoCommit(true);

        con.close();

        return -1;
    }

    @Override
    public boolean isPresent(Page page) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean storeData(Page page) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<URI> getKeyIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Iterator<Page> getPageIterator() throws Throwable {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    private void createMap(String name) {
        String crtSql = "CREATE TABLE `"+name+"` (\n"
                + "  `hash` binary(32) NOT NULL,\n"
                + "  `version` bigint(20) NOT NULL,\n"
                + "  `uri` text NOT NULL,\n"
                + "  `data` longtext NOT NULL,\n"
                + "  PRIMARY KEY (`hash`)\n"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    }
    
    private boolean checkMap(String name){
        String chkSql="SHOW TABLES LIKE 'crawl_%';";
        return false;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        long cur = System.currentTimeMillis();
        int limit = 100000000;
        for (int i = 0; i < limit; i++) {
            org.apache.commons.codec.digest.DigestUtils.sha256("" + i);
        }
        System.out.println("#" + (System.currentTimeMillis() - cur));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        cur = System.currentTimeMillis();

        for (int i = 0; i < limit; i++) {
            digest.digest((i + "").getBytes("UTF-8"));
        }

        System.out.println("#" + (System.currentTimeMillis() - cur));
    }

    
}
