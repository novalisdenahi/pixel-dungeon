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
package com.watabou.pixeldungeon.actors.blobs;

import java.util.Arrays;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;

public class Blob extends Actor {

    public static final int WIDTH = Level.WIDTH;
    public static final int HEIGHT = Level.HEIGHT;
    public static final int LENGTH = Level.LENGTH;

    @SuppressWarnings("unchecked")
    public static <T extends Blob> T seed(final int cell, final int amount, final Class<T> type) {
        try {

            T gas = (T) Dungeon.level.blobs.get(type);
            if (gas == null) {
                gas = type.newInstance();
                Dungeon.level.blobs.put(type, gas);
            }

            gas.seed(cell, amount);

            return gas;

        } catch (Exception e) {
            PixelDungeon.reportException(e);
            return null;
        }
    }

    public int volume = 0;
    public int[] cur;

    protected int[] off;

    public BlobEmitter emitter;

    private static final String CUR = "cur";
    private static final String START = "start";

    protected Blob() {

        cur = new int[LENGTH];
        off = new int[LENGTH];

        volume = 0;
    }

    @Override
    public boolean act() {

        spend(TICK);

        if (volume > 0) {

            volume = 0;
            evolve();

            int[] tmp = off;
            off = cur;
            cur = tmp;

        }

        return true;
    }

    public void clear(final int cell) {
        volume -= cur[cell];
        cur[cell] = 0;
    }

    protected void evolve() {

        boolean[] notBlocking = BArray.not(Level.solid, null);

        for (int i = 1; i < (HEIGHT - 1); i++) {

            int from = (i * WIDTH) + 1;
            int to = (from + WIDTH) - 2;

            for (int pos = from; pos < to; pos++) {
                if (notBlocking[pos]) {

                    int count = 1;
                    int sum = cur[pos];

                    if (notBlocking[pos - 1]) {
                        sum += cur[pos - 1];
                        count++;
                    }
                    if (notBlocking[pos + 1]) {
                        sum += cur[pos + 1];
                        count++;
                    }
                    if (notBlocking[pos - WIDTH]) {
                        sum += cur[pos - WIDTH];
                        count++;
                    }
                    if (notBlocking[pos + WIDTH]) {
                        sum += cur[pos + WIDTH];
                        count++;
                    }

                    int value = sum >= count ? (sum / count) - 1 : 0;
                    off[pos] = value;

                    volume += value;
                } else {
                    off[pos] = 0;
                }
            }
        }
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {

        super.restoreFromBundle(bundle);

        int[] data = bundle.getIntArray(CUR);
        if (data != null) {
            int start = bundle.getInt(START);
            for (int i = 0; i < data.length; i++) {
                cur[i + start] = data[i];
                volume += data[i];
            }
        }

        if (Level.resizingNeeded) {
            int[] cur = new int[Level.LENGTH];
            Arrays.fill(cur, 0);

            int loadedMapSize = Level.loadedMapSize;
            for (int i = 0; i < loadedMapSize; i++) {
                System.arraycopy(this.cur, i * loadedMapSize, cur, i * Level.WIDTH, loadedMapSize);
            }

            this.cur = cur;
        }
    }

    public void seed(final int cell, final int amount) {
        cur[cell] += amount;
        volume += amount;
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);

        if (volume > 0) {

            int start;
            for (start = 0; start < LENGTH; start++) {
                if (cur[start] > 0) {
                    break;
                }
            }
            int end;
            for (end = LENGTH - 1; end > start; end--) {
                if (cur[end] > 0) {
                    break;
                }
            }

            bundle.put(START, start);
            bundle.put(CUR, trim(start, end + 1));

        }
    }

    public String tileDesc() {
        return null;
    }

    private int[] trim(final int start, final int end) {
        int len = end - start;
        int[] copy = new int[len];
        System.arraycopy(cur, start, copy, 0, len);
        return copy;
    }

    public void use(final BlobEmitter emitter) {
        this.emitter = emitter;
    }
}
