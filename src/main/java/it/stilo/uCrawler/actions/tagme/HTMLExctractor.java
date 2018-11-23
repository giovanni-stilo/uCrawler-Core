package it.stilo.uCrawler.actions.tagme;

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

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ditommaso & stilo
 */
public class HTMLExctractor implements ActionIF {

    FileOutputStream f1, f2, f3, f4, f5, f6;
    Writer w1, w2, w3, w4, w5, w6;
    private String tags = ("tags");
    private String classname = ("classname");
    private String id = ("id");

    private String tagsForPage = ("tagsForPage");

    public void setTagsForPage(String tagsForPage) {
        this.tagsForPage = tagsForPage;
    }

    public void setClassnameForPage(String classnameForPage) {
        this.classnameForPage = classnameForPage;
    }

    public void setIdForPage(String idForPage) {
        this.idForPage = idForPage;
    }
    private String classnameForPage = ("classnameForPage");
    private String idForPage = ("idForPage");

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {

        Document doc = Jsoup.parse(page.getData());
        //Element content = doc.getElementById("content");
        Elements links = doc.body().select("*");

        try {
            /* String strtags = "";//links.html();//all tags
            String strclassname = "";
            String strid = "";*/
            w4.write(page.getUri().toString() + "\n");
            w5.write(page.getUri().toString() + "\n");
            w6.write(page.getUri().toString() + "\n");

        } catch (IOException ex) {
            Logger.getLogger(HTMLExctractor.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Element l : links) {
            try {
                w1.write(l.tagName() + "\n"); //all tag name
                w2.write(l.id() + "\n"); //all id name
                w3.write(l.className() + "\n"); //all class name

                w4.write(l.tagName() + " "); //all tag name
                w5.write(l.id() + " "); //all id name
                w6.write(l.className() + " "); //all class name

            } catch (IOException ex) {
                Logger.getLogger(HTMLExctractor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            w4.write("\n"); //all tag name
            w5.write("\n"); //all id name
            w6.write("\n"); //all class name
        } catch (IOException ex) {
            Logger.getLogger(HTMLExctractor.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*for (Element l : links) {
            strclassname += l.className() + " ";//all class name
            strid += l.id() + " ";//all id
            strtags += l.tagName()+" ";

        }
        try {
            w1.write(strtags+"\n");
            w2.write(strclassname+"\n");
            w3.write(strid+"\n");

        } catch (IOException ex) {
            Logger.getLogger(HTMLExctractor.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return true;
    }

    public void init() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        f1 = new FileOutputStream(tags);
        w1 = new OutputStreamWriter(new GZIPOutputStream(f1), "UTF-8");
        f2 = new FileOutputStream(classname);
        w2 = new OutputStreamWriter(new GZIPOutputStream(f2), "UTF-8");
        f3 = new FileOutputStream(id);
        w3 = new OutputStreamWriter(new GZIPOutputStream(f3), "UTF-8");

        f4 = new FileOutputStream(tagsForPage);
        w4 = new OutputStreamWriter(new GZIPOutputStream(f4), "UTF-8");
        f5 = new FileOutputStream(classnameForPage);
        w5 = new OutputStreamWriter(new GZIPOutputStream(f5), "UTF-8");
        f6 = new FileOutputStream(idForPage);
        w6 = new OutputStreamWriter(new GZIPOutputStream(f6), "UTF-8");

    }

    @Override
    public void finalize() throws IOException {

        w1.close();
        w2.close();
        w3.close();

        f1.close();
        f2.close();
        f3.close();

        w4.close();
        w5.close();
        w6.close();

        f4.close();
        f5.close();
        f6.close();
    }

}
