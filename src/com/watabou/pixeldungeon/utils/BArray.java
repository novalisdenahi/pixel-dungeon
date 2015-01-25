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

public class BArray {

    public static boolean[] and(final boolean[] a, final boolean[] b, boolean[] result) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = a[i] && b[i];
        }

        return result;
    }

    public static boolean[] is(final int[] a, boolean[] result, final int v1) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = a[i] == v1;
        }

        return result;
    }

    public static boolean[] isNot(final int[] a, boolean[] result, final int v1) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = a[i] != v1;
        }

        return result;
    }

    public static boolean[] isNotOneOf(final int[] a, boolean[] result, final int... v) {

        int length = a.length;
        int nv = v.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = true;
            for (int j = 0; j < nv; j++) {
                if (a[i] == v[j]) {
                    result[i] = false;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean[] isOneOf(final int[] a, boolean[] result, final int... v) {

        int length = a.length;
        int nv = v.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = false;
            for (int j = 0; j < nv; j++) {
                if (a[i] == v[j]) {
                    result[i] = true;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean[] not(final boolean[] a, boolean[] result) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = !a[i];
        }

        return result;
    }

    public static boolean[] or(final boolean[] a, final boolean[] b, boolean[] result) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = a[i] || b[i];
        }

        return result;
    }
}
