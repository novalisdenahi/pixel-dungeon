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

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Bless extends FlavourBuff {

    private static final int FAVORED_BONUS = 3;
    private static final int PRIEST_CHANCE = 10;
    private static final int HIGHPRIEST_CHANCE = 6;
    private static final float DURATION_STEP = 1f;
    private static final float DURATION_OF_BLESSING = 10f;
    private static final float REGENERATION_DELAY = 5f;

    private static final String TXT_VALUE = "%+dHP";
    private static final String TXT_FAVORED = "The goddess look after you. You are really lucky!";

    private float spent = 0;

    @Override
    public boolean act() {
        spend(DURATION_STEP);
        spent += DURATION_STEP;
        if (spent >= DURATION_OF_BLESSING) {
            detach();
        } else {
            if (target.isAlive() && ((spent % REGENERATION_DELAY) == 0)) {

                int value = 1 + ((Dungeon.depth - 1) / 5);
                if (isFavored()) {
                    value = value * FAVORED_BONUS;
                    GLog.p(TXT_FAVORED);
                }
                int effect = Math.min(target.HT - target.HP, value);
                if (effect > 0) {
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

    private boolean isFavored() {
        int chanceOfFavored;
        if (((Hero) target).subClass == HeroSubClass.HIGHPRIEST) {
            chanceOfFavored = HIGHPRIEST_CHANCE;
        } else {
            chanceOfFavored = PRIEST_CHANCE;
        }

        if (Random.Int(chanceOfFavored) == 0) {
            return true;
        }
        return false;

    }

    @Override
    public String toString() {
        return "Blessed";
    }

}
