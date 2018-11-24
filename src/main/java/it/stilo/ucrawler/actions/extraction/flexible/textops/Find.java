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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author stilo
 */
public class Find implements PerformerIF{

	private int start;
	private Pattern regex;
	private boolean sensitive;

	public Find(boolean sensitive) {
		this.sensitive = sensitive;
	}

	public Find() {
		this(false);
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
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
	public void setRegex(String regex) {
		int caseSensitive = sensitive ? 0 : Pattern.CASE_INSENSITIVE;
		this.regex = Pattern.compile(regex , Pattern.UNICODE_CASE | Pattern.UNICODE_CASE | caseSensitive);
	}

	@Override
	public Object performOp(Object obj)  throws PerformerException {
		if (obj == null)
			return null;
		Matcher m = regex.matcher(obj.toString());
		if (m.find(start)) {
			return m.group();
		}else {
			return null;
		}
	}

}
