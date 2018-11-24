package it.stilo.ucrawler.actions.tagme;

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

/*import it.acubelab.tagme.AnnotatedText;
import it.acubelab.tagme.Annotation;
import it.acubelab.tagme.TagmeParser;
import it.acubelab.tagme.config.TagmeConfig;
import it.acubelab.tagme.preprocessing.TopicSearcher;*/
import it.stilo.ucrawler.core.actions.ActionIF;
import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.page.Page;
import org.jsoup.nodes.Document;

/**
 *
 * @author ditommaso & stilo
 */
public class Tagger implements ActionIF {

    public Tagger(){
  //      TagmeConfig.init("./config.xml");
    }
    
    @Override
    public boolean doSomething(Page page) throws ActionsException {
        Document doc;
        String lang = "en";

  /*      

        AnnotatedText ann_text = new AnnotatedText(page.getFromContext(CleanPage.class).toString());
      
        try {
            TagmeParser parser = new TagmeParser(lang, true);
            parser.parse(ann_text);
        } catch (IOException ex) {
            Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        

        List<Annotation> annots = ann_text.getAnnotations();
        
        try {
            TopicSearcher searcher = new TopicSearcher(lang);
        } catch (IOException ex) {
            Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        page.setInContext(it.acubelab.tagme.Annotation.class, annots);
        page.setInContext(it.acubelab.tagme.AnnotatedText.class, ann_text);*/
        return true;
    }

}
