package it.stilo.uCrawler.utils.tagme;

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

import static it.stilo.uCrawler.utils.UrlEncoder.encode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.zip.GZIPInputStream;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author ditommaso & stilo
 */
public class SelectedUrl {

    //String txt = null;
    int limit = 1000;

    private void filterNews(HashSet url, File f) throws IOException, URISyntaxException {

        FileInputStream fin = new FileInputStream(f);
        GZIPInputStream gzis = new GZIPInputStream(fin);
        InputStreamReader xover = new InputStreamReader(gzis);
        BufferedReader is = new BufferedReader(xover);
        String line;
        int count = 0;

        DB db = DBMaker.newFileDB(new File(System.getProperty("user.home") + "/gdelt-tsv/testdb"))
                .cacheLRUEnable()
                .compressionEnable()
                .make();



       // ArrayList<String> list = new ArrayList<String>();
        ConcurrentNavigableMap<String, String> map =// db.getTreeMap("collection");
		db.createTreeMap("collection")
//                .keySerializer(BTreeKeySerializer.STRING)
//                .valueSerializer(Serializer.STRING)
                .valuesStoredOutsideNodes(true).makeOrGet();

        PrintWriter writer = new PrintWriter(System.getProperty("user.home") + "/gdelt-tsv/filteredNews", "UTF-8");

        while ((line = is.readLine()) != null) {
            String[] csv = line.split("\t");
            
            
            if (url.contains(csv[57])) {
                //txt += line;

	//                URI uri = new URI(encode(csv[57]));
		String uri = encode(csv[57]);

                if (map != null) {
                    if (!(map.containsKey(uri))) {
                        //ArrayList<String> fields = new ArrayList<String>();
                        //list.add(line);
                        map.put(uri, line);

                    } else {
                        //ArrayList<String> l = (ArrayList<String>) map.get(uri);
                     
                        map.put(uri, map.get(uri) + "\n"+line);
                    }

                    count++;
                    writer.println(line);

                    if (count % limit == 0) {
                        writer.flush();
                    }
                    //System.out.println(line);
                }

            }
        }

        is.close();
        db.commit();
        db.close();
        writer.flush();
        writer.close();

    }

    private HashSet urlSeed(File f) throws IOException {

        HashSet url = new HashSet();

        FileInputStream fin = new FileInputStream(f);
        GZIPInputStream gzis = new GZIPInputStream(fin);
        InputStreamReader xover = new InputStreamReader(gzis);
        BufferedReader is = new BufferedReader(xover);

        String line;

        while ((line = is.readLine()) != null) {
            url.add(line);
        }

        return url;

    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        SelectedUrl task = new SelectedUrl();
        HashSet url = new HashSet();

        File f1 = new File(System.getProperty("user.home") + "/gdelt-tsv/url-list-ru.seeds.gz");
        File f2 = new File(System.getProperty("user.home") + "/gdelt-tsv/summer-2014.CSV.gz");
        url = task.urlSeed(f1);
        System.out.println("Loaded");
        task.filterNews(url, f2);

    }
}
