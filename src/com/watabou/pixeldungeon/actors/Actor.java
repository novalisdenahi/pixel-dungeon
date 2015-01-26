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
package com.watabou.pixeldungeon.actors;

import java.util.Arrays;
import java.util.HashSet;

import android.util.SparseArray;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Actor implements Bundlable {

    public static final float TICK = 1f;

    private float time;

    private int id = 0;

    private static final String TIME = "time";

    private static final String ID = "id";

    private static HashSet<Actor> all = new HashSet<Actor>();

    private static Actor current;

    private static SparseArray<Actor> ids = new SparseArray<Actor>();

    private static float now = 0;

    private static Char[] chars = new Char[Level.LENGTH];

    public static void add(final Actor actor) {
        Actor.add(actor, now);
    }

    private static void add(final Actor actor, final float time) {

        if (all.contains(actor)) {
            return;
        }

        if (actor.id > 0) {
            ids.put(actor.id, actor);
        }

        all.add(actor);
        actor.time += time;
        actor.onAdd();

        if (actor instanceof Char) {
            Char ch = (Char) actor;
            chars[ch.pos] = ch;
            for (Buff buff : ch.buffs()) {
                all.add(buff);
                buff.onAdd();
            }
        }
    }

    public static void addDelayed(final Actor actor, final float delay) {
        Actor.add(actor, now + delay);
    }

    public static HashSet<Actor> all() {
        return all;
    }

    public static void clear() {

        now = 0;

        Arrays.fill(chars, null);
        all.clear();

        ids.clear();
    }

    // **********************
    // *** Static members ***

    public static Actor findById(final int id) {
        return ids.get(id);
    }

    public static Char findChar(final int pos) {
        return chars[pos];
    }

    public static void fixTime() {

        if ((Dungeon.hero != null) && all.contains(Dungeon.hero)) {
            Statistics.duration += now;
        }

        float min = Float.MAX_VALUE;
        for (Actor a : all) {
            if (a.time < min) {
                min = a.time;
            }
        }
        for (Actor a : all) {
            a.time -= min;
        }
        now = 0;
    }

    public static void freeCell(final int pos) {
        chars[pos] = null;
    }

    public static void init() {

        Actor.addDelayed(Dungeon.hero, -Float.MIN_VALUE);

        for (Mob mob : Dungeon.level.mobs) {
            Actor.add(mob);
        }

        for (Blob blob : Dungeon.level.blobs.values()) {
            Actor.add(blob);
        }

        current = null;
    }

    public static void occupyCell(final Char ch) {
        chars[ch.pos] = ch;
    }

    public static void process() {

        if (current != null) {
            return;
        }

        boolean doNext;

        do {
            now = Float.MAX_VALUE;
            current = null;

            Arrays.fill(chars, null);

            for (Actor actor : all) {
                if (actor.time < now) {
                    now = actor.time;
                    current = actor;
                }

                if (actor instanceof Char) {
                    Char ch = (Char) actor;
                    chars[ch.pos] = ch;
                }
            }

            if (current != null) {

                if ((current instanceof Char) && ((Char) current).sprite.isMoving) {
                    // If it's character's turn to act, but its sprite
                    // is moving, wait till the movement is over
                    current = null;
                    break;
                }

                doNext = current.act();
                if (doNext && !Dungeon.hero.isAlive()) {
                    doNext = false;
                    current = null;
                }
            } else {
                doNext = false;
            }

        } while (doNext);
    }

    public static void remove(final Actor actor) {

        if (actor != null) {
            all.remove(actor);
            actor.onRemove();

            if (actor.id > 0) {
                ids.remove(actor.id);
            }
        }
    }

    protected abstract boolean act();

    protected float cooldown() {
        return time - now;
    }

    protected void diactivate() {
        time = Float.MAX_VALUE;
    }

    public int id() {
        if (id > 0) {
            return id;
        } else {
            int max = 0;
            for (Actor a : all) {
                if (a.id > max) {
                    max = a.id;
                }
            }
            return (id = max + 1);
        }
    }

    /* protected */public void next() {
        if (current == this) {
            current = null;
        }
    }

    protected void onAdd() {
    }

    protected void onRemove() {
    }

    protected void postpone(final float time) {
        if (this.time < (now + time)) {
            this.time = now + time;
        }
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        time = bundle.getFloat(TIME);
        id = bundle.getInt(ID);
    }

    protected void spend(final float time) {
        this.time += time;
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        bundle.put(TIME, time);
        bundle.put(ID, id);
    }
}
