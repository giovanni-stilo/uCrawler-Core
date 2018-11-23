package it.stilo.uCrawler.actions.extraction.flexible.textops;

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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class Replace implements PerformerIF {

	private Pattern regex;
	private String replacement = "";
	private boolean sensitive;
	private boolean first = false;

	public Replace(boolean sensitive) {
		this.sensitive = sensitive;
	}

	public Replace() {
		this(false);
	}

	@Override
	public Object performOp(Object obj) throws PerformerException {
		if (obj == null)
			return null;
		if (first)
			return regex.matcher(obj.toString()).replaceFirst(replacement);
		else
			return regex.matcher(obj.toString()).replaceAll(replacement);
	}


	/**
	 * @return the replacement
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * @param replacement the replacement to set
	 */
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}



	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex.pattern();
	}

	/**
	 * @param regex the regex to set
	 */
	@Required
	public void setRegex(String regex) {
		int caseSensitive = sensitive ? 0 : Pattern.CASE_INSENSITIVE;
		this.regex = Pattern.compile(regex, Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS | caseSensitive);
	}


	/**
	 * @return the first
	 */
	public boolean isFirst() {
		return first;
	}


	/**
	 * @param first the first to set
	 */
	public void setFirst(boolean first) {
		this.first = first;
	}


}
