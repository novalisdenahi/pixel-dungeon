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
package com.watabou.pixeldungeon;

import java.util.ArrayList;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class Journal {

    public static enum Feature {
        WELL_OF_HEALTH("Well of Health"),
        WELL_OF_AWARENESS("Well of Awareness"),
        WELL_OF_TRANSMUTATION("Well of Transmutation"),
        ALCHEMY("Alchemy pot"),
        GARDEN("Garden"),
        STATUE("Animated statue"),
        PIRATE("Goblin pirate"),
        ASH("Ash"),
        GHOST("Sad ghost"),
        WANDMAKER("Old wandmaker"),
        TROLL("Troll blacksmith"),
        IMP("Ambitious imp");

        public String desc;

        private Feature(final String desc) {
            this.desc = desc;
        }
    };

    public static class Record implements Comparable<Record>, Bundlable {

        private static final String FEATURE = "feature";
        private static final String DEPTH = "depth";

        public Feature feature;
        public int depth;

        public Record() {
        }

        public Record(final Feature feature, final int depth) {
            this.feature = feature;
            this.depth = depth;
        }

        @Override
        public int compareTo(final Record another) {
            return another.depth - depth;
        }

        @Override
        public void restoreFromBundle(final Bundle bundle) {
            feature = Feature.valueOf(bundle.getString(FEATURE));
            depth = bundle.getInt(DEPTH);
        }

        @Override
        public void storeInBundle(final Bundle bundle) {
            bundle.put(FEATURE, feature.toString());
            bundle.put(DEPTH, depth);
        }
    }

    public static ArrayList<Record> records;

    private static final String JOURNAL = "journal";

    public static void add(final Feature feature) {
        int size = records.size();
        for (int i = 0; i < size; i++) {
            Record rec = records.get(i);
            if ((rec.feature == feature) && (rec.depth == Dungeon.depth)) {
                return;
            }
        }

        records.add(new Record(feature, Dungeon.depth));
    }

    public static void remove(final Feature feature) {
        int size = records.size();
        for (int i = 0; i < size; i++) {
            Record rec = records.get(i);
            if ((rec.feature == feature) && (rec.depth == Dungeon.depth)) {
                records.remove(i);
                return;
            }
        }
    }

    public static void reset() {
        records = new ArrayList<Journal.Record>();
    }

    public static void restoreFromBundle(final Bundle bundle) {
        records = new ArrayList<Record>();
        for (Bundlable rec : bundle.getCollection(JOURNAL)) {
            records.add((Record) rec);
        }
    }

    public static void storeInBundle(final Bundle bundle) {
        bundle.put(JOURNAL, records);
    }
}
