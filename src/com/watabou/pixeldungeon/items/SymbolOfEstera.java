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
package com.watabou.pixeldungeon.items;

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class SymbolOfEstera extends Item {
    protected class Charger extends Buff {
        // TODO this Charger can be refactored to normal class. ! in that case the delay change!
        private static final float TIME_TO_CHARGE = 50f;

        @Override
        public boolean act() {

            if (curCharges < maxCharges) {
                curCharges++;
                updateQuickslot();
            }

            delay();

            return true;
        }

        @Override
        public boolean attachTo(final Char target) {
            super.attachTo(target);
            delay();

            return true;
        }

        protected void delay() {
            float time2charge = TIME_TO_CHARGE / (float) Math.sqrt(1 + level);

            spend(time2charge);
        }
    }

    private static final String AC_BLESS = "BLESS";
    private static final String AC_HEALING = "HEALING PRAY";

    private static final float TIME_TO_USE_SYMBOL = 1f;

    private static final String TXT_BLESS = "You feel yourself the holy blessing of Estera.";

    private static final String TXT_HEALING = "Estera hears your prayers. Your wounds are healing.";

    private static final String TXT_EMPTY = "The Symbol of Estera is not sufficiently recharged.";

    {
        name = "Symbol Of Estera";
        image = ItemSpriteSheet.SYMBOLOFESTERA;

        // defaultAction = AC_BLESS;

        unique = true;
    }

    private static final Glowing WHITE = new Glowing(0xFFFFCC);

    public int maxCharges = initialCharges();

    public int curCharges = maxCharges;

    protected Charger charger;

    private static final String MAX_CHARGES = "maxCharges";

    private static final String CUR_CHARGES = "curCharges";

    @Override
    public ArrayList<String> actions(final Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.heroClass == HeroClass.PRIEST) {
            actions.add(AC_BLESS);
            if (hero.subClass == HeroSubClass.HIGHPRIEST) {
                actions.add(AC_HEALING);
            }
        }
        return actions;
    }

    @Override
    public void execute(final Hero hero, final String action) {
        if (action.equals(AC_BLESS)) {
            if (curCharges > 0) {
                // TODO implement add bless buff

                curCharges--;

                hero.spend(TIME_TO_USE_SYMBOL);
                hero.busy();

                Sample.INSTANCE.play(Assets.SND_LEVELUP); // TODO bless sound
                hero.sprite.operate(hero.pos);

                updateQuickslot();

                GLog.w(TXT_BLESS);
            } else {
                GLog.w(TXT_EMPTY);
            }

        } else if (action.equals(AC_HEALING)) {

            if (curCharges >= maxCharges) {

                hero.HP = hero.HT;

                Buff.detach(hero, Cripple.class);
                Buff.detach(hero, Weakness.class);
                Buff.detach(hero, Bleeding.class);

                curCharges = 0;
                hero.spend(TIME_TO_USE_SYMBOL);
                hero.busy();

                Sample.INSTANCE.play(Assets.SND_LEVELUP); // TODO bless sound
                hero.sprite.operate(hero.pos);
                hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);

                updateQuickslot();

                GLog.w(TXT_HEALING);
            } else {
                GLog.w(TXT_EMPTY);
            }

        } else {

            super.execute(hero, action);

        }
    }

    @Override
    public Glowing glowing() {
        return isFull() ? WHITE : null;
    }

    @Override
    public String info() {
        // TODO add good info
        return
        "You can store excess dew in this tiny vessel for drinking it later. " +
                "If the vial is full, in a moment of deadly peril the dew will be " +
                "consumed automatically.";
    }

    protected int initialCharges() {
        return 3;
    }

    public boolean isFull() {
        return curCharges >= maxCharges;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        maxCharges = bundle.getInt(MAX_CHARGES);
        curCharges = bundle.getInt(CUR_CHARGES);
    }

    @Override
    public String status() {
        return curCharges + "/" + maxCharges;
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_CHARGES, maxCharges);
        bundle.put(CUR_CHARGES, curCharges);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + status() + ")";
    }
}
