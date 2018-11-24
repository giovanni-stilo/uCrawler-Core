package it.stilo.ucrawler.actions.extraction;

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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import it.stilo.ucrawler.core.actions.ActionsException;
import it.stilo.ucrawler.crawler.Crawler;
import it.stilo.ucrawler.page.Page;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author stilo
 */
public class ForumExtractor extends DataExtractor {

	private ForumConfig config;

	@SuppressWarnings("unchecked")
	protected List<URI> extraction(Document doc, Page pg)
			throws ActionsException {

		List<URI> urlLinks = new ArrayList<URI>();
		HashMap<String, Object> contextMap;
		HashMap<String, Object> cloneMap;
		ArrayDeque<String> breadcrumb;
		ArrayDeque<String> cloneBreadcrumb;

		// estrae il titolo della pagina corrente
		// TODO gestire a seconda che si tratti del titolo di una
		// discussione oppure del nome di una sezione (forse non necessario)
		Logger.getLogger(this.getClass()).info(
				"Getting section/thread title...");
		String title = doc.select(config.getThreadTitle()).text();
		Logger.getLogger(this.getClass()).info("Title is: " + title);

		contextMap = (HashMap<String, Object>) pg.getFromContext(this
				.getClass());

		if (contextMap == null) {
			// nessun contesto ereditato
			Logger.getLogger(this.getClass()).info("Context null");
			contextMap = new HashMap<String, Object>();
			breadcrumb = new ArrayDeque<String>();
			breadcrumb.add(title);
			contextMap.put("breadcrumb", breadcrumb);
			pg.setInContext(this.getClass(), contextMap);
		} else {
			// contesto ereditato, cloniamo l'HashMap per effettuare
			// le modifiche necessarie
			Logger.getLogger(this.getClass()).info("Context not null");
			cloneMap = (HashMap<String, Object>) contextMap.clone();

			breadcrumb = (ArrayDeque<String>) contextMap.get("breadcrumb");
			if (breadcrumb.contains(title)) {
				// il titolo nel contesto ereditato contiene quello della
				// pagina corrente (vero quando in una discussione o in una
				// sezione ci troviamo in una pagina successiva alla prima)
				Logger.getLogger(this.getClass()).info(
						"Title contained in inherited context");
			} else {
				// il titolo nel contesto ereditato non contiene quello della
				// pagina corrente, dunque siamo scesi di un livello
				Logger.getLogger(this.getClass()).info(
						"Title not contained in inherited context");
				cloneBreadcrumb = breadcrumb.clone();
				cloneBreadcrumb.add(title);
				cloneMap.put("breadcrumb", cloneBreadcrumb);
			}

			Logger.getLogger(this.getClass())
					.info("Parent breadcrumb: "
							+ this.buildBreadcrumbString((ArrayDeque<String>) contextMap
									.get("breadcrumb")));

			Logger.getLogger(this.getClass())
					.info("Child breadcrumb: "
							+ this.buildBreadcrumbString((ArrayDeque<String>) cloneMap
									.get("breadcrumb")));

			pg.setInContext(this.getClass(), cloneMap);
		}

		// estrae le sezioni contenute nella pagina
		Elements sections = doc.select(config.getSections());
		Logger.getLogger(this.getClass()).info("Getting sections...");
		Logger.getLogger(this.getClass()).info(
				sections.size() + " sections found");

		urlLinks.addAll(this.sectionsExtraction(sections));

		// estrae le discussioni presenti nella pagina
		Elements threads = doc.select(config.getThreads());
		Logger.getLogger(this.getClass()).info("Getting threads...");
		Logger.getLogger(this.getClass()).info(
				threads.size() + " threads found");

		urlLinks.addAll(this.threadsExtraction(threads));

		// prende tutti i messaggi contenuti nella pagina
		Elements messages = doc.select(config.getMessages());
		Logger.getLogger(this.getClass()).info("Getting messages...");
		Logger.getLogger(this.getClass()).info(
				messages.size() + " messages found");

		this.messagesExtraction(messages);

		// individua il tag che contiene i link delle pagine
		// della discussione
		// TODO gestire a seconda che si tratti delle pagine di una discussione
		// oppure delle pagine di una sezione (forse non necessario)
		Elements pagination = doc.select(config.getPages());
		URI toAdd = pageExtraction(pagination);

		Logger.getLogger(this.getClass()).info(
				"Getting next section/thread page...");

		if (toAdd != null) {
			urlLinks.add(toAdd);
			Logger.getLogger(this.getClass()).info(
					"Next section/thread page found");
		} else {
			Logger.getLogger(this.getClass()).info(
					"Next section/thread page not found");
		}

		return urlLinks;
	}

	/**
	 * Imposta l'oggetto contenente i paramerti dipendenti dal forum.
	 * 
	 * @param config
	 *            un oggetto della classe
	 *            {@link it.stilo.ucrawler.actions.extraction.ForumConfig}
	 */
	public void setConfig(ForumConfig config) {
		this.config = config;
	}

	// TODO rimpiazzare sectionsExtraction e threadsExtraction con un unico
	// metodo generico

	/**
	 * Estrae i link delle sezioni dalla pagina principale del forum e quelli
	 * delle sottosezioni dalle pagine.
	 * 
	 * @param sections
	 *            l'oggetto della classe Elements che corrisponde alla parte di
	 *            pagina contenente i link alle sezioni
	 * @return gli URL delle pagine corrispondenti alle sezioni
	 * @throws ActionsException
	 */
	private ArrayList<URI> sectionsExtraction(Elements sections)
			throws ActionsException {

		ArrayList<URI> sectionsURL = new ArrayList<URI>();

		for (Element section : sections) {

			String sectionURL = section.absUrl("href");

			Logger.getLogger(this.getClass()).info(sectionURL);
			try {
				sectionsURL.add(new URI(sectionURL));
			} catch (URISyntaxException e) {
				throw new ActionsException(
						"Exception during forum section url extraction in "
								+ this.getClass().getCanonicalName() + " - "
								+ e.getLocalizedMessage() + ".", e);
			}

		}

		return sectionsURL;
	}

	/**
	 * Estrae tutti i link alle discussioni presenti nella pagina.
	 * 
	 * @param threads
	 *            l'oggetto della classe Elements che corrisponde alla parte di
	 *            pagina contenente i link alle discussioni
	 * @return gli URL delle pagine delle discussioni
	 * @throws ActionsException
	 */
	private ArrayList<URI> threadsExtraction(Elements threads)
			throws ActionsException {

		ArrayList<URI> threadsURL = new ArrayList<URI>();

		for (Element thread : threads) {
			String threadURL = thread.select(config.getSingleThread()).first()
					.absUrl("href");
			Logger.getLogger(this.getClass()).info(threadURL);

			try {
				threadsURL.add(new URI(threadURL));
			} catch (URISyntaxException e) {
				throw new ActionsException(
						"Exception during forum thread url extraction in "
								+ this.getClass().getCanonicalName() + " - "
								+ e.getLocalizedMessage() + ".", e);
			}
		}

		return threadsURL;
	}

	/**
	 * Estrae tutti i messaggi presenti nella pagina.
	 * 
	 * @param el
	 *            l'oggetto della classe Elements che corrisponde alla parte
	 *            della pagina, elaborata dal parser, contenente tutti i
	 *            messaggi
	 * @throws ActionsException
	 * @throws ParseException
	 */
	private void messagesExtraction(Elements messages) throws ActionsException {

		// oggetto che serve per effettuare il parsing della data di un
		// messaggio
		SimpleDateFormat formatter = new SimpleDateFormat(
				config.getDateFormatterString());

		for (Element m : messages) {

			// data del messaggio
			// TODO eliminazione del .first()
			Date messageDate;
			try {
				// TODO gestire i messaggi con data "Oggi" e "Ieri"
				messageDate = formatter.parse(m.select(config.getDate())
						.first().text());

				// nome utente dell'autore del messaggio
				String messageUser = m.select(config.getUser()).text();

				// testo del messaggio
				String messageText = m.select(config.getText()).text();
				ForumMessage message = new ForumMessage(messageDate,
						messageUser, messageText);

				// Logger.getLogger(this.getClass()).info(message.toString());

			} catch (ParseException e) {
				throw new ActionsException(
						"Exception during forum thread message extraction in "
								+ this.getClass().getCanonicalName() + " - "
								+ e.getLocalizedMessage() + ".", e);
			}

		}
	}

	/**
	 * Estrae il link alla pagina successiva in una discussione.
	 * 
	 * @param pagination
	 *            l'oggetto della classe Elements che contiene i link alle
	 *            pagine della discussione
	 * @return l'URL della pagina successiva nella discussione (se presente)
	 * @throws ActionsException
	 */
	private URI pageExtraction(Elements pagination) throws ActionsException {

		// in ogni pagina della discussione, i link alle altre pagine
		// potrebbero comparire due volte, nella parte alta e nella parte
		// bassa della pagina: prendiamo solamente il primo gruppo
		Element pages = pagination.first();

		if (pages != null) {

			Logger.getLogger(this.getClass()).info("Thread pages found");

			// trova il link corrispondente alla pagina successiva
			// rispetto a quella selezionata
			Element next = pages.select(config.getNextPage()).first();

			try {
				if (next != null) {

					Logger.getLogger(this.getClass()).info(
							"baseUri for next thread page: " + pages.baseUri());

					Logger.getLogger(this.getClass()).info(
							"relativeUrl for next thread page: "
									+ next.attr("href"));

					return new URI(next.absUrl("href"));
				}
			} catch (URISyntaxException e) {
				throw new ActionsException(
						"Exception during forum next page extraction in "
								+ this.getClass().getCanonicalName() + ".", e);
			}

		}

		return null;
	}

	/**
	 * Costruisce una stringa che mostra la posizione della pagina all'interno
	 * del forum.
	 * 
	 * @param breadcrumb
	 *            l'oggetto di classe ArrayDeque presente nel contesto di una
	 *            pagina di un forum
	 * @return la stringa che rappresenta la posizione della pagina, con il
	 *         simbolo ">" a separare i livelli
	 */
	private String buildBreadcrumbString(ArrayDeque<String> breadcrumb) {
		StringBuilder breadcrumbBuilder = new StringBuilder();

		for (String s : breadcrumb) {
			breadcrumbBuilder.append(s);
			breadcrumbBuilder.append(" > ");
		}

		// cancella l'ultimo simbolo usato per dividere gli elementi, dato che,
		// per come viene costruita la stringa, a seguire non si trova nulla
		breadcrumbBuilder.delete(breadcrumbBuilder.lastIndexOf(" > "),
				breadcrumbBuilder.length() - 1);

		return breadcrumbBuilder.toString();
	}

	public static void main(String[] args) throws Throwable {

		// create and configure beans
		ApplicationContext context = new FileSystemXmlApplicationContext(
				new String[] { "conf/forum_extraction_bean.xml" });
		// retrieve configured instance
		Crawler c = (Crawler) context.getBean("forum");
		c.startCrawling();
	}

}
