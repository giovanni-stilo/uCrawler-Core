package it.stilo.ucrawler.concurrentFetcher.configuration;

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
public abstract class NumericConstraintIF {

    private double next = -1;

    public double nextValue() {
        if (next == -1) {
            return next();
        } else {
            double tmp = next;
            next = -1;
            return tmp;
        }
    }

    public abstract double next();

    public void forceNext(double next) {
        this.next = next;
    }
    
    public abstract NumericConstraintIF clone();
}
