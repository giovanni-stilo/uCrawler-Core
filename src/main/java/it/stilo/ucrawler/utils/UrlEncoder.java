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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author stilo
 */
public class UrlEncoder {

    public static String decode(String raw) {
        
        
        return raw.replaceAll("%20", " ")
                .replaceAll("%3A", ":")
                .replaceAll("%2F", "/")
                .replaceAll("%3B", ";")
                .replaceAll("%40", "@")
                .replaceAll("%3C", "<")
                .replaceAll("%3E", ">")
                .replaceAll("%3D", "=")
                .replaceAll("%26", "&")
                .replaceAll("%25", "%")
                .replaceAll("%24", "\\$")
                .replaceAll("%23", "#")
                .replaceAll("%2B", "+")
                .replaceAll("%2C", ",")
                .replaceAll("%3F", "?")
                .replaceAll("%5B", "[")
                .replaceAll("%5D", "]")
                .replaceAll("%22", "\"")
                .replaceAll("%5E", "^")
                .replaceAll("%7B", "{")
                .replaceAll("%7D", "}")
                .replaceAll("%5C", "\\\\")
                .replaceAll("%E2%80%99", "’");
        
    }
    
    public static String encode(String raw) {
        String proto="";
        if(raw.startsWith("http://")){
            raw=raw.substring(7);
            proto="http://";
        }
        else if(raw.startsWith("https://")){
            raw=raw.substring(8);
            proto="https://";
        }
        return proto+raw.replaceAll("%","%25")
                .replaceAll(" ","%20")
                .replaceAll(":","%3A")
                //.replaceAll("/","%2F")
                .replaceAll(";","%3B")
                .replaceAll("@","%40")
                .replaceAll("<","%3C")
                .replaceAll(">","%3E")
                //.replaceAll("=","%3D")
                //.replaceAll("&","%26")               
                .replaceAll("\\$","%24")
                .replaceAll("#","%23")
                .replaceAll("\\+\\+","+%2B")
                .replaceAll(",","%2C")
                .replaceAll("\\[","%5B")
                .replaceAll("\\]","%5D")
                .replaceAll("\"","%22")
                .replaceAll("\\^", "%5E")
                .replaceAll("\\{", "%7B")
                .replaceAll("\\}", "%7D")
                .replaceAll("\\\\", "%5C")
                .replaceAll("’", "%E2%80%99");
                //.replaceAll("\\?","%3F");
    }
    
    public static String recover(String raw){
        return encode(decode(raw));
    }

    public static void main(String [] args) throws IOException, URISyntaxException{
        


        String tst="http://www.diamonds.net/News/NewsItem.aspx?ArticleID=47479&ArticleTitle=Global+Diamond+Production++11%25+to+%2414B+in+2013";
        String tst1="http://www.usatoday.com/story/News%20[NEWS]/Local%20News%20[NEWS01]/2014/07/14/man-accused-of-assaulting-brother-with-gun/12619561/";
        System.out.println(new URI(recover(tst1)).toString());
        
        /*File in = new File("conf/GDELT/jul-aug-2014-1");
        LineIterator it = lineIterator(in);
        String temp;
        while(it.hasNext()) {
            temp = (String)it.nextLine();
            
            try {
                new URI(recover(temp));
            } catch (Exception ex) {
                System.out.println(temp);
                System.out.println(recover(temp));
            }
        }*/
    }
}
