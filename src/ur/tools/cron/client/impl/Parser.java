package ur.tools.cron.client.impl;

/**
 * Related Values Processing Framework.
 * 
 * Copyright (C) 2003 Serge Brisson.
 * 
 * This software is distributable under LGPL license.
 * See details at the bottom of this file.
 * 
 * $Header: /cvsroot/rvpf/RVPF/java/src/net/sf/rvpf/clock/crontab/Parser.java,v 1.2 2003/09/10 18:00:11 sfb Exp $
 */

import java.util.Arrays;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Crontab Parser.
 * 
 * @author Serge Brisson
 * @version $Revision: 1.2 $
 */
class Parser {

    // Public Class Methods.

    static Crontab parse(final String entry) throws BadItemException {

        final String[] lists = entry.trim().split( Parser.CRONTAB_SPLITTER );
        final boolean[] minutes =
                Parser.parseList( Parser.getList( lists, 0 ), 0, 59 );
        final boolean[] hours =
                Parser.parseList( Parser.getList( lists, 1 ), 0, 23 );
        final boolean[] days =
                Parser.parseList( Parser.getList( lists, 2 ), 1, 31 );
        final boolean[] months =
                Parser.parseList( Parser.getList( lists, 3 ), 1, 12 );
        final boolean[] dows =
                Parser.parseList( Parser.getList( lists, 4 ).replaceAll(
                        "7",
                        "0" ), 0, 6 );

        if (lists.length < 5 || lists[4].equals( Parser.ALL_VALUES )) {
            Arrays.fill( dows, false );
        }
        else if (lists[2].equals( Parser.ALL_VALUES )) {
            Arrays.fill( days, false );
        }

        return new Crontab( minutes, hours, days, months, dows );
    }


    // Private Class Methods.

    private static String getList(final String[] lists, final int index) {

        if (index < lists.length) {
            return lists[index];
        }

        return Parser.ALL_VALUES;
    }


    private static void parseItem(
            final String item,
            final int origin,
            final boolean[] mask) throws BadItemException {

        int begin = origin;
        int end = begin + mask.length - 1;
        int step = 1;

        if (!Parser.matches( item, Parser.ITEM_PATTERN )) {
            throw new BadItemException( item );
        }

        if (!"*".equals( Parser.group(
                item,
                Parser.ITEM_PATTERN,
                Parser.RANGE_GROUP ) )) {
            begin =
                    Integer.parseInt( Parser.group(
                            item,
                            Parser.ITEM_PATTERN,
                            Parser.BEGIN_GROUP ) );
            if (Parser.group( item, Parser.ITEM_PATTERN, Parser.END_GROUP ) != null) {
                end =
                        Integer.parseInt( Parser.group(
                                item,
                                Parser.ITEM_PATTERN,
                                Parser.END_GROUP ) );
            }
            else {
                end = begin;
            }
        }
        if (Parser.group( item, Parser.ITEM_PATTERN, Parser.STEP_GROUP ) != null) {
            step =
                    Integer.parseInt( Parser.group(
                            item,
                            Parser.ITEM_PATTERN,
                            Parser.STEP_GROUP ) );
        }

        begin -= origin;
        end -= origin;
        if (begin < 0 || end >= mask.length || step <= 0 || step >= mask.length) {
            throw new BadItemException( item );
        }

        if (begin > end) {
            for (int i = 0; i <= end; i += step) {
                mask[i] = true;
            }
            end = mask.length - 1;
        }

        for (int i = begin; i <= end; i += step) {
            mask[i] = true;
        }
    }


    private static boolean matches(final String string, final String regexp) {

        final JavaScriptObject match = Parser.match( string, regexp );
        return match != null && match.toString().split( "," ).length > 0;
    }


    private static String group(
            final String string,
            final String regexp,
            final int group) {

        if (Parser.matches( string, regexp )) {

            final String[] match =
                    Parser.match( string, regexp ).toString().split( "," );
            if (match.length > group) {
                return match[group];
            }
        }
        return null;
    }


    private static native JavaScriptObject match(String string, String regexp) /*-{
        var re = new RegExp(regexp);
        return string.match(re);
    }-*/;


    private static boolean[] parseList(
            final String list,
            final int origin,
            final int limit) throws BadItemException {

        final String[] items = list.split( Parser.LIST_SPLITTER );
        final boolean[] mask = new boolean[1 + limit - origin];

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];

            if (item.length() == 0) {
                item = Parser.ALL_VALUES;
            }
            Parser.parseItem( item, origin, mask );
        }

        return mask;
    }

    // Class Constants.

    private static final String ALL_VALUES = "*";
    private static final String CRONTAB_SPLITTER = "\\s";
    private static final String ITEM_PATTERN =
            "(\\*|(\\d+)(?:-(\\d+))?)(?:/(\\d+))?";
    private static final String LIST_SPLITTER = ",";
    private static final int RANGE_GROUP = 1;
    private static final int BEGIN_GROUP = 2;
    private static final int END_GROUP = 3;
    private static final int STEP_GROUP = 4;

    // Nested Classes.

    static class BadItemException extends Exception {

        // Constructors.

        BadItemException(final String message) {

            super( message );
        }
    }
}

// $Log: Parser.java,v $
// Revision 1.2 2003/09/10 18:00:11 sfb
// Made local to package.
//

/*
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

