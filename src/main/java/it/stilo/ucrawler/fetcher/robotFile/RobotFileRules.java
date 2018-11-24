package it.stilo.ucrawler.fetcher.robotFile;

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

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author stilo
 */
public class RobotFileRules {

	//lista dei path proibiti
	protected TreeSet<String> disallows = new TreeSet<String>();
	//lista dei path ammessi
	protected TreeSet<String> allows = new TreeSet<String>();

	public boolean allows(String path) {
		return !containsPrefixOf(disallows, path) || containsPrefixOf(allows, path);
	}

	public void addDisallow(String path) {
		disallows.add(path);
	}

	public void addAllow(String path) {
		allows.add(path);
	}
	
	private boolean containsPrefixOf(TreeSet<String> t, String s) {
		SortedSet<String> sub = t.headSet(s);
		// because redundant prefixes have been eliminated,
		// only a test against last item in headSet is necessary
		if (!sub.isEmpty() && s.startsWith(sub.last())) {
			return true; // prefix substring exists
		} 
		// might still exist exactly (headSet does not contain boundary)
		return t.contains(s); 
	}

}
