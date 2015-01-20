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

public class Drunk extends FlavourBuff {

    public static final float DURATION = 10f;

    private float spent = 0f;

    // @Override
    // public boolean act() {
    // spend(DURATION);
    // detach();
    // return true;
    // }

    @Override
    public boolean attachTo(final Char target) {
        target.immunities().add(Fear.class);
        spend(DURATION);
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        target.immunities().remove(Fear.class);
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.DRUNK;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public String toString() {
        return "Drunk";
    }
}
