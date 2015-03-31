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
import com.watabou.pixeldungeon.actors.buffs.Bless;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.HolyFlare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class SymbolOfEstera extends Item {
    protected class Charger extends Buff {
        // TODO this Charger can be refactored to normal class. ! in that case the delay change!
        private static final float TIME_TO_CHARGE = 40f;

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
            float time2charge = TIME_TO_CHARGE;
            if (((Hero) target).subClass == HeroSubClass.HIGHPRIEST) {
                time2charge = TIME_TO_CHARGE / 2;
                // this is equals a level 3 wand mage combo charge
            }
            spend(time2charge);
        }
    }

    private static final int INITIAL_CHARGES = 3;

    private static final String AC_BLESS = "BLESS";
    private static final String AC_HEALING = "HEALING PRAY";

    private static final float TIME_TO_USE_SYMBOL = 1f;

    private static final String TXT_BLESS = "You feel yourself the holy blessing of Estera.";

    private static final String TXT_HEALING = "Estera hears your prayers. Your wounds are healing.";

    private static final String TXT_EMPTY = "The Symbol of Estera is not sufficiently recharged.";

    private static final Glowing WHITE = new Glowing(0xFFFFCC);

    private static final String MAX_CHARGES = "maxCharges";

    private static final String CUR_CHARGES = "curCharges";

    {
        name = "Symbol Of Estera";
        image = ItemSpriteSheet.SYMBOLOFESTERA;

        defaultAction = AC_BLESS;

        unique = true;
    }

    public int maxCharges = initialCharges();

    public int curCharges = maxCharges;

    protected Charger charger;

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

    public void charge(final Char owner) {
        (charger = new Charger()).attachTo(owner);
    }

    @Override
    public boolean collect(final Bag container) {
        if (super.collect(container)) {
            if (container.owner != null) {
                charge(container.owner);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute(final Hero hero, final String action) {
        if (action.equals(AC_BLESS)) {
            if (curCharges > 0) {

                Buff.affect(hero, Bless.class);
                curCharges--;
                hero.spend(TIME_TO_USE_SYMBOL);
                hero.busy();

                new HolyFlare(4, 32).show(hero, 1f);
                Sample.INSTANCE.play(Assets.SND_LEVELUP); // TODO bless sound
                hero.sprite.operate(hero.pos);

                // updateQuickslot();

                GLog.p(TXT_BLESS);
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

                new HolyFlare(4, 32).show(hero, 1f);
                Sample.INSTANCE.play(Assets.SND_LEVELUP); // TODO bless sound
                hero.sprite.operate(hero.pos);
                hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);

                // updateQuickslot();

                GLog.p(TXT_HEALING);
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
    };

    @Override
    public String info() {
        return "This is the Symbol of Estera. Estera is a kind and merciful goddess. "
                + "The symbol can store a fraction of the Estera power, but only the priest of Estera can use this power"
                + " You can feel the look of Estera in the symbol emerald eye.";
    }

    protected int initialCharges() {
        return INITIAL_CHARGES;
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
        return false;
    }

    @Override
    public void onDetach() {
        stopCharging();
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

    public void stopCharging() {
        if (charger != null) {
            charger.detach();
            charger = null;
        }
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
