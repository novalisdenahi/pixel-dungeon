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
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.ui.BuffIndicator;

public class Buff extends Actor {

    public static <T extends Buff> T affect(final Char target, final Class<T> buffClass) {
        T buff = target.buff(buffClass);
        if (buff != null) {
            return buff;
        } else {
            return Buff.append(target, buffClass);
        }
    }

    public static <T extends FlavourBuff> T affect(final Char target, final Class<T> buffClass, final float duration) {
        T buff = Buff.affect(target, buffClass);
        buff.spend(duration);
        return buff;
    }

    public static <T extends Buff> T append(final Char target, final Class<T> buffClass) {
        try {
            T buff = buffClass.newInstance();
            buff.attachTo(target);
            return buff;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends FlavourBuff> T append(final Char target, final Class<T> buffClass, final float duration) {
        T buff = Buff.append(target, buffClass);
        buff.spend(duration);
        return buff;
    }

    public static void detach(final Buff buff) {
        if (buff != null) {
            buff.detach();
        }
    }

    public static void detach(final Char target, final Class<? extends Buff> cl) {
        Buff.detach(target.buff(cl));
    }

    public static <T extends FlavourBuff> T prolong(final Char target, final Class<T> buffClass, final float duration) {
        T buff = Buff.affect(target, buffClass);
        buff.postpone(duration);
        return buff;
    }

    public Char target;

    @Override
    public boolean act() {
        diactivate();
        return true;
    }

    public boolean attachTo(final Char target) {

        if (target.immunities().contains(getClass())) {
            return false;
        }

        this.target = target;
        target.add(this);

        return true;
    }

    public void detach() {
        target.remove(this);
    }

    public int icon() {
        return BuffIndicator.NONE;
    }
}
