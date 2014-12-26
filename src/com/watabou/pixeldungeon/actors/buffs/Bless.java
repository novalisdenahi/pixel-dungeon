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

import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;

public class Bless extends FlavourBuff {

    private static final float DURATION_STEP = 1f;
    private static final float DURATION_OF_BLESSING = 10f;
    private static final float REGENERATION_DELAY = 5f;

    private static final String TXT_VALUE = "%+dHP";

    private int symbolLevel = 0;
    private float spent = 0;
    private float curDuration = 0;

    @Override
    public boolean act() {
        spend(DURATION_STEP);
        spent += DURATION_STEP;
        if (spent >= curDuration) {
            detach();
        } else {
            if (target.isAlive() && ((spent % REGENERATION_DELAY) == 0)) {

                if (target.HP < target.HT) {
                    int effect = (int) Math.sqrt(symbolLevel + 1);
                    target.HP += effect;
                    target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    target.sprite.showStatus(CharSprite.POSITIVE, TXT_VALUE, effect);
                }

            }
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.BLESS;
    }

    public void initialize(final int symbolLevel) {
        this.symbolLevel = symbolLevel;

        curDuration = (float) Math.sqrt(symbolLevel + 1) * DURATION_OF_BLESSING;
    }

    @Override
    public String toString() {
        return "Blessed";
    }

}
