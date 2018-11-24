package it.stilo.ucrawler.utils;

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

import java.util.Comparator;

/**
 *
 * @author stilo
 */
public class LongTuple<E> {
    
    private long key;
    private E value;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }
    
    public LongTuple(){
        super();
    }
    
    public LongTuple(long key,E value){
        this.key=key;
        this.value=value;
    }

       
    private static class DescComparator implements Comparator<LongTuple> {
        @Override
        public int compare(LongTuple  o1, LongTuple o2) {
            return (int) ((o1.key - o2.key) % 2);
        }
    }
    
    public static Comparator<LongTuple> getDescComparator(){
        return new DescComparator();
    }
    
    private static class CrescComparator implements Comparator<LongTuple> {
        @Override
        public int compare(LongTuple  o1, LongTuple o2) {
            return (int) ((o2.key - o1.key) % 2);
        }
    }
    
    public static Comparator<LongTuple> getCrescComparator(){
        return new CrescComparator();
    }
}
