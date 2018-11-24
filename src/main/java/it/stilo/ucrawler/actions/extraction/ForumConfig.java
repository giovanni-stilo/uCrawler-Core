package it.stilo.uCrawler.actions.extraction;

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


/**
 *
 * @author stilo
 */
public class ForumConfig {

	private String sections;
	private String threads;
	private String singleThread;
	private String threadTitle;
	private String pages;
	private String nextPage;
	private String messages;
	private String date;
	private String dateFormatterString;
	private String user;
	private String text;

	/**
	 * Fornisce l'impostazione relativa all'individuazione delle sezioni
	 * contenute nelle pagine del forum.
	 * 
	 * @return la query impostata
	 */
	public String getSections() {
		return sections;
	}

	/**
	 * Imposta il parametro per individuare le sezioni contenute nelle pagine
	 * del forum.
	 * 
	 * @param sections
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setSections(String sections) {
		this.sections = sections;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione delle discussioni
	 * contenute in una pagina.
	 * 
	 * @return la query impostata
	 */
	public String getThreads() {
		return threads;
	}

	/**
	 * Imposta il parametro per individuare le discussioni contenute in una
	 * pagina.
	 * 
	 * @param threads
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setThreads(String threads) {
		this.threads = threads;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione di una singola
	 * discussione tra quelle contenute in una pagina.
	 * 
	 * @return la query impostata
	 */
	public String getSingleThread() {
		return singleThread;
	}

	/**
	 * Imposta il parametro per individuare una singola discussione tra quelle
	 * contenute in una pagina.
	 * 
	 * @param threads
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setSingleThread(String singleThread) {
		this.singleThread = singleThread;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione del titolo della
	 * discussione.
	 * 
	 * @return la query impostata
	 */
	public String getThreadTitle() {
		return threadTitle;
	}

	/**
	 * Imposta il parametro per individuare il titolo della discussione.
	 * 
	 * @param threadTitle
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione dei link delle pagine
	 * che formano la discussione.
	 * 
	 * @return la query impostata
	 */
	public String getPages() {
		return pages;
	}

	/**
	 * Imposta il parametro per individuare i link delle pagine che formano la
	 * discussione.
	 * 
	 * @param pages
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setPages(String pages) {
		this.pages = pages;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione del link alla pagina
	 * successiva nella discussione.
	 * 
	 * @return la query impostata
	 */
	public String getNextPage() {
		return nextPage;
	}

	/**
	 * Imposta il parametro per individuare il link alla pagina successiva nella
	 * discussione.
	 * 
	 * @param nextPage
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione dei messaggi
	 * all'interno della pagina.
	 * 
	 * @return la query impostata
	 */
	public String getMessages() {
		return messages;
	}

	/**
	 * Imposta il parametro per individuare i messaggi nella pagina.
	 * 
	 * @param messages
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setMessages(String messages) {
		this.messages = messages;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione della data di un
	 * singolo messaggio.
	 * 
	 * @return la query impostata
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Imposta il parametro per individuare la data di un singolo messaggio.
	 * 
	 * @param date
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Fornisce l'impostazione per il parser della data di un singolo messaggio.
	 * 
	 * @return la stringa che rappresenta il formato della data
	 */
	public String getDateFormatterString() {
		return dateFormatterString;
	}

	/**
	 * Imposta la stringa da usare per il parsing della data di un singolo
	 * messaggio.
	 * 
	 * @param dateFormatterString
	 *            una stringa secondo la rappresentazione di
	 *            {@link java.text.SimpleDateFormat}
	 */
	public void setDateFormatterString(String dateFormatterString) {
		this.dateFormatterString = dateFormatterString;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione dell'autore di un
	 * messaggio.
	 * 
	 * @return la query impostata
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Imposta il parametro per individuare l'utente autore di un messaggio.
	 * 
	 * @param user
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Fornisce l'impostazione relativa all'individuazione del testo di un
	 * singolo messaggio.
	 * 
	 * @return la query impostata
	 */
	public String getText() {
		return text;
	}

	/**
	 * Imposta il parametro per individuare il testo di un singolo messaggio.
	 * 
	 * @param text
	 *            una query secondo la sintassi di
	 *            {@link org.jsoup.select.Selector}
	 */
	public void setText(String text) {
		this.text = text;
	}

}
