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

import it.stilo.uCrawler.page.Page;
import it.stilo.uCrawler.utils.tagme.StringUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;


/**
 *
 * @author stilo & ditommaso
 */
public class PageToDocumentBuilder implements DocumentBuilderIF {

//    private TopicSearcher searcher;

    public static final String IDURI = "IDURI";
    public static final String URIENCODED = "URI";
    public static final String TEXT = "TEXT";
    public static final String PAGE = "PAGE";
    public static final String TAGS = "TAGS";
    public static final String WIKIPAGES = "WIKIPAGES";

    public static final String GLOBALEVENTID = "GLOBALEVENTID";
    public static final String DATE = "DATE";
    public static final String ACTOR1CODE = "ACTOR1CODE";
    public static final String ACTOR1NAME = "ACTOR1NAME";
    public static final String ACTOR1COUNTRYCODE = "ACTOR1COUNTRYCODE";
    public static final String ACTOR1KNOWNGROUPCODE = "ACTOR1KNOWNGROUPCODE";
    public static final String ACTOR1ETHNICCODE = "ACTOR1ETHNICCODE";
    public static final String ACTOR1RELIGION1CODE = "ACTOR1RELIGION1CODE";
    public static final String ACTOR1RELIGION2CODE = "ACTOR1RELIGION2CODE";
    public static final String ACTOR1TYPE1CODE = "ACTOR1TYPE1CODE";
    public static final String ACTOR1TYPE2CODE = "ACTOR1TYPE2CODE";
    public static final String ACTOR1TYPE3CODE = "ACTOR1TYPE3CODE";
    public static final String ACTOR2CODE = "ACTOR2CODE";
    public static final String ACTOR2NAME = "ACTOR2NAME";
    public static final String ACTOR2COUNTRYCODE = "ACTOR2COUNTRYCODE";
    public static final String ACTOR2KNOWNGROUPCODE = "ACTOR2KNOWNGROUPCODE";
    public static final String ACTOR2ETHNICCODE = "ACTOR2ETHNICCODE";
    public static final String ACTOR2RELIGION1CODE = "ACTOR2RELIGION1CODE";
    public static final String ACTOR2RELIGION2CODE = "ACTOR2RELIGION2CODE";
    public static final String ACTOR2TYPE1CODE = "ACTOR2TYPE1CODE";
    public static final String ACTOR2TYPE2CODE = "ACTOR2TYPE2CODE";
    public static final String ACTOR2TYPE3CODE = "ACTOR2TYPE3CODE";
    public static final String ISROOTEVENT = "ISROOTEVENT";
    public static final String EVENTCODE = "EVENTCODE";
    public static final String EVENTBASECODE = "EVENTBASECODE";
    public static final String EVENTROOTCODE = "EVENTROOTCODE";
    public static final String QUADCLASS = "QUADCLASS";
    public static final String GOLDSTEINSCALE = "GOLDSTEINSCALE";
    public static final String NUMMENTIONS = "NUMMENTIONS";
    public static final String NUMSOURCES = "NUMSOURCES";
    public static final String NUMARTICLES = "NUMARTICLES";
    public static final String AVGTONE = "AVGTONE";
    public static final String ACTOR1GEO_TYPE = "ACTOR1GEO_TYPE";
    public static final String ACTOR1GEO_FULLNAME = "ACTOR1GEO_FULLNAME";
    public static final String ACTOR1GEO_COUNTRYCODE = "ACTOR1GEO_COUNTRYCODE";
    public static final String ACTOR1GEO_ADM1CODE = "ACTOR1GEO_ADM1CODE";
    public static final String ACTOR1GEO_LAT = "ACTOR1GEO_LAT";
    public static final String ACTOR1GEO_LONG = "ACTOR1GEO_LONG";
    public static final String ACTOR1GEO_FEATUREID = "ACTOR1GEO_FEATUREID";
    public static final String ACTOR2GEO_TYPE = "ACTOR2GEO_TYPE";
    public static final String ACTOR2GEO_FULLNAME = "ACTOR2GEO_FULLNAME";
    public static final String ACTOR2GEO_COUNTRYCODE = "ACTOR2GEO_COUNTRYCODE";
    public static final String ACTOR2GEO_ADM1CODE = "ACTOR2GEO_ADM1CODE";
    public static final String ACTOR2GEO_LAT = "ACTOR2GEO_LAT";
    public static final String ACTOR2GEO_LONG = "ACTOR2GEO_LONG";
    public static final String ACTOR2GEO_FEATUREID = "ACTOR2GEO_FEATUREID";
    public static final String ACTIONGEO_TYPE = "ACTIONGEO_TYPE";
    public static final String ACTIONGEO_FULLNAME = "ACTIONGEO_FULLNAME";
    public static final String ACTIONGEO_COUNTRYCODE = "ACTIONGEO_COUNTRYCODE";
    public static final String ACTIONGEO_ADM1CODE = "ACTIONGEO_ADM1CODE";
    public static final String ACTIONGEO_LAT = "ACTIONGEO_LAT";
    public static final String ACTIONGEO_LONG = "ACTIONGEO_LONG";
    public static final String ACTIONGEO_FEATUREID = "ACTIONGEO_FEATUREID";
    public static final String DATEADDED = "DATEADDED";
    public static final String SOURCEURL = "SOURCEURL";

    private static ArrayList<String> list = new ArrayList<String>();

    static {
        list.add(GLOBALEVENTID);
        list.add(DATE);
        list.add(ACTOR1CODE);
        list.add(ACTOR1NAME);
        list.add(ACTOR1COUNTRYCODE);
        list.add(ACTOR1KNOWNGROUPCODE);
        list.add(ACTOR1ETHNICCODE);
        list.add(ACTOR1RELIGION1CODE);
        list.add(ACTOR1RELIGION2CODE);
        list.add(ACTOR1TYPE1CODE);
        list.add(ACTOR1TYPE2CODE);
        list.add(ACTOR1TYPE3CODE);
        list.add(ACTOR2CODE);
        list.add(ACTOR2NAME);
        list.add(ACTOR2COUNTRYCODE);
        list.add(ACTOR2KNOWNGROUPCODE);
        list.add(ACTOR2ETHNICCODE);
        list.add(ACTOR2RELIGION1CODE);
        list.add(ACTOR2RELIGION2CODE);
        list.add(ACTOR2TYPE1CODE);
        list.add(ACTOR2TYPE2CODE);
        list.add(ACTOR2TYPE3CODE);
        list.add(ISROOTEVENT);
        list.add(EVENTCODE);
        list.add(EVENTBASECODE);
        list.add(EVENTROOTCODE);
        list.add(QUADCLASS);
        list.add(GOLDSTEINSCALE);
        list.add(NUMMENTIONS);
        list.add(NUMSOURCES);
        list.add(NUMARTICLES);
        list.add(AVGTONE);
        list.add(ACTOR1GEO_TYPE);
        list.add(ACTOR1GEO_FULLNAME);
        list.add(ACTOR1GEO_COUNTRYCODE);
        list.add(ACTOR1GEO_ADM1CODE);
        list.add(ACTOR1GEO_LAT);
        list.add(ACTOR1GEO_LONG);
        list.add(ACTOR1GEO_FEATUREID);
        list.add(ACTOR2GEO_TYPE);
        list.add(ACTOR2GEO_FULLNAME);
        list.add(ACTOR2GEO_COUNTRYCODE);
        list.add(ACTOR2GEO_ADM1CODE);
        list.add(ACTOR2GEO_LAT);
        list.add(ACTOR2GEO_LONG);
        list.add(ACTOR2GEO_FEATUREID);
        list.add(ACTIONGEO_TYPE);
        list.add(ACTIONGEO_FULLNAME);
        list.add(ACTIONGEO_COUNTRYCODE);
        list.add(ACTIONGEO_ADM1CODE);
        list.add(ACTIONGEO_LAT);
        list.add(ACTIONGEO_LONG);
        list.add(ACTIONGEO_FEATUREID);
        list.add(DATEADDED);
        list.add(SOURCEURL);

    }
    
    public PageToDocumentBuilder() throws IOException{
//        searcher     = new TopicSearcher("en");

    }
        
    @Override
    public Document convertToDocument(Page page)  throws IOException{

        //fieldS 
        ArrayList<String[]> list = (ArrayList<String[]>) page.getFromContext(ReaderAndSplitterFile.class);
        Document doc = null;

        //String url = p;
        for (String[] s : list) {
            doc = new Document();

            doc.add(new IntField(IDURI, page.getUri().hashCode(), Field.Store.YES));//hascode page
            doc.add(new StringField(URIENCODED, page.getUri().toString(), Field.Store.YES));//uri page
            doc.add(new TextField(TEXT, page.getFromContext(CleanPage.class).toString(), Field.Store.YES));//clean page
            doc.add(new TextField(PAGE, page.getData(), Field.Store.YES)); //original page

/*           ArrayList<Annotation> annots = (ArrayList<Annotation>) page.getFromContext(it.acubelab.tagme.Annotation.class);

            AnnotatedText ann_text = (AnnotatedText) page.getFromContext(it.acubelab.tagme.AnnotatedText.class);
            //doc.add(new TextField(ANN_TEXT, ann_text.toString(), Field.Store.YES));//annotation
            String tags = "", wiki = "";
            for (Annotation a : annots) {
                tags += ann_text.getOriginalText(a) + " ";
                wiki += searcher.getTitle(a.getTopic());
            }
            doc.add(new TextField(TAGS, tags, Field.Store.YES));//tagged page
            doc.add(new StringField(WIKIPAGES, tags, Field.Store.YES)); //wiki pages

            indexFields(doc, s);
*/
        }
        return doc;
    }

    private void indexFields(Document doc, String[] s) {
        doc.add(new LongField(GLOBALEVENTID, Long.parseLong(s[0]), Field.Store.YES));

        StringUtils var = new StringUtils();
        long date = 0;
        try {
            date = var.toEpochTime(s[1]);
            doc.add(new LongField(DATE, date, Field.Store.YES));
        } catch (ParseException ex) {
            Logger.getLogger(PageToDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 5; i <= 57; i++) {
            if (s[i] != null) {

                doc.add(new TextField(list.get(i - 4), s[i], Field.Store.YES));
            }

        }

    }

}
