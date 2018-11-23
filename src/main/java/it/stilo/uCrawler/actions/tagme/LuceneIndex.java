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
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import static org.apache.lucene.util.Version.LUCENE_41;

/**
 *
 * @author ditommaso & stilo
 */
public class LuceneIndex implements ActionIF {

    private IndexWriter writer;
    private DocumentBuilderIF documentBuilder;
    private Analyzer analyzer;
    private String indexDir = "index";
    private boolean updateMode = false;

    public LuceneIndex() throws IOException {
       analyzer = new StandardAnalyzer(LUCENE_41, CharArraySet.EMPTY_SET);
       
        this.documentBuilder = new PageToDocumentBuilder();
    }
    
    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }
    
     public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
     
     public void setDocumentBuilder(DocumentBuilderIF builder) {
        this.documentBuilder = builder;
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        try {
        Document doc = this.documentBuilder.convertToDocument(page);
        
            writer.addDocument(doc);
        } catch (Throwable ex) {
            Logger.getLogger(LuceneIndex.class.getName()).debug(null, ex);
        } 

        return true;

    }

    public void finalize() {

        try {
            if (writer != null) {
                writer.commit();
                writer.close();
            }

        } catch (CorruptIndexException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).debug(null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).debug(null, ex);
        }
    }

    public void init() throws IOException {
        File indexFile = new File(indexDir);
        FileUtils.forceMkdir(indexFile);
        Directory index = new SimpleFSDirectory(indexFile);
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_41, analyzer);
        config.setMaxBufferedDocs(100000);
        if (updateMode) {
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        } else {
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }

        writer = new IndexWriter(index, config);

    }

}
