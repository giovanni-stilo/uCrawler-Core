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


import java.util.Date;

/**
 *
 * @author stilo
 */
public class ForumMessage {
	
	private Date date;
	private String user;
	private String text;
	
	public ForumMessage(Date date, String user, String text) {
		this.date = date;
		this.user = user;
		this.text = text;
	}
	
	public ForumMessage(String user, String text) {
		this.user = user;
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public String getUser() {
		return user;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		if(date != null)
			builder.append(this.date.toString());
		
		builder.append(", user: ");
		builder.append(this.user);
		builder.append("\n");
		builder.append(this.text);
		
		return builder.toString();
	}
}
