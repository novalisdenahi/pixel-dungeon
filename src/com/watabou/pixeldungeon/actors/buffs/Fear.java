/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Tóth Dániel
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

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Fear extends FlavourBuff {

    protected float duration;

    private static final String DURATION = "duration";

    @Override
    public boolean act() {
        spend(duration);
        detach();
        return true;
    }

    @Override
    public boolean attachTo(final Char target) {
        if (super.attachTo(target)) {
            target.pacified = true;
            target.paralysed = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.pacified = false;
        target.paralysed = false;
        super.detach();
    };

    @Override
    public int icon() {
        return BuffIndicator.FEAR;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        duration = bundle.getFloat(DURATION);
    }

    public void set(final float duration) {
        this.duration = duration;
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DURATION, duration);

    }

    @Override
    public String toString() {
        return "In Fear";
    }
}
