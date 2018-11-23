package it.stilo.uCrawler.actions;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import it.stilo.uCrawler.actions.extraction.flexible.WhereAB;
import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;

/**
 *
 * @author stilo
 */
public class ContextListToJson implements ActionIF {

	private String encoding = "UTF-8";
	private String storePath = "./storage/local/json";
	private String filename;
	private WhereAB producerObject;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setStorePath(String path) {
		this.storePath = path;
	}

	@Required
	public void setFilename(String name) {
		this.filename = name;
	}

	@Required
	public void setProducerObject(WhereAB obj) {
		this.producerObject = obj;
	}

	@Override
	public boolean doSomething(Page page) throws ActionsException {

		HashMap<String, Object> contextMap = (HashMap<String, Object>) page
				.getFromContext(producerObject.getClass());

		if (contextMap != null) {
			try {
				Gson g = new Gson();
				JsonElement toJson = g.toJsonTree(contextMap.get(producerObject
						.getWhere()));

				if (toJson.isJsonArray()) {
					JsonElement element = null;

					if (!new File(storePath).exists())
						FileUtils.forceMkdir(new File(storePath));

					File fileOut = new File(storePath + File.separator
							+ filename);
					if (fileOut.exists()) {
						BufferedInputStream in = new BufferedInputStream(
								new FileInputStream(fileOut));
						JsonReader reader = new JsonReader(
								new InputStreamReader(in, encoding));
						if (reader.hasNext()) {
							reader.beginObject();
							try {
								reader.nextName();
								element = new JsonParser().parse(reader);
								reader.endObject();
							} catch(IOException e) {
								Logger.getLogger(this.getClass()).info("No name found in json");
							}
						}
						reader.close();
						in.close();
					}

					if (element != null && element.isJsonArray()) {
						((JsonArray) element).addAll((JsonArray) toJson);
					} else {
						element = toJson;
					}

					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(fileOut));
					JsonWriter writer = new JsonWriter(new OutputStreamWriter(
							out, encoding));
					writer.setIndent("  ");
					writer.beginObject();
					writer.name(producerObject.getWhere());
					g.toJson(element, toJson.getClass(), writer);
					writer.endObject();
					writer.close();
					out.close();
				}
			} catch (Exception e) {
				throw new ActionsException(e);
			}
		}
		return true;
	}
}