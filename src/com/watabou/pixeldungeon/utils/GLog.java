/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.utils;

import android.util.Log;

import com.watabou.utils.Signal;

public class GLog {

    public static final String TAG = "GAME";

    public static final String POSITIVE = "++ ";
    public static final String NEGATIVE = "-- ";
    public static final String WARNING = "** ";
    public static final String HIGHLIGHT = "@@ ";

    public static Signal<String> update = new Signal<String>();

    public static void h(final String text, final Object... args) {
        GLog.i(HIGHLIGHT + text, args);
    }

    public static void i(String text, final Object... args) {

        if (args.length > 0) {
            text = Utils.format(text, args);
        }

        Log.i(TAG, text);
        update.dispatch(text);
    }

    public static void n(final String text, final Object... args) {
        GLog.i(NEGATIVE + text, args);
    }

    public static void p(final String text, final Object... args) {
        GLog.i(POSITIVE + text, args);
    }

    public static void w(final String text, final Object... args) {
        GLog.i(WARNING + text, args);
    }
}
