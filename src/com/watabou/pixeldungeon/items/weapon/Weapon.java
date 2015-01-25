/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.items.weapon;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Fire;
import com.watabou.pixeldungeon.items.weapon.enchantments.Horror;
import com.watabou.pixeldungeon.items.weapon.enchantments.Instability;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.items.weapon.enchantments.Luck;
import com.watabou.pixeldungeon.items.weapon.enchantments.Paralysis;
import com.watabou.pixeldungeon.items.weapon.enchantments.Piercing;
import com.watabou.pixeldungeon.items.weapon.enchantments.Poison;
import com.watabou.pixeldungeon.items.weapon.enchantments.Slow;
import com.watabou.pixeldungeon.items.weapon.enchantments.Swing;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Weapon extends KindOfWeapon {

    public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[] {
                Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class,
                Slow.class, Swing.class, Piercing.class, Instability.class, Horror.class, Luck.class };
        private static final float[] chances = new float[] { 10, 10, 1, 2, 1, 2, 3, 3, 3, 2, 2 };

        @SuppressWarnings("unchecked")
        public static Enchantment random() {
            try {
                return ((Class<Enchantment>) enchants[Random.chances(chances)]).newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        public ItemSprite.Glowing glowing() {
            return ItemSprite.Glowing.WHITE;
        }

        public String name(final String weaponName) {
            return weaponName;
        }

        public abstract boolean proc(Weapon weapon, Char attacker, Char defender, int damage);

        @Override
        public void restoreFromBundle(final Bundle bundle) {
        }

        @Override
        public void storeInBundle(final Bundle bundle) {
        }

    }

    public enum Imbue {
        NONE, SPEED, ACCURACY
    }

    private static final String TXT_IDENTIFY =
            "You are now familiar enough with your %s to identify it. It is %s.";

    private static final String TXT_INCOMPATIBLE =
            "Interaction of different types of magic has negated the enchantment on this weapon!";
    private static final String TXT_TO_STRING = "%s :%d";
    public int STR = 10;

    public float ACU = 1;
    public float DLY = 1f;

    public Imbue imbue = Imbue.NONE;

    private int hitsToKnow = 20;

    protected Enchantment enchantment;

    private static final String ENCHANTMENT = "enchantment";
    private static final String IMBUE = "imbue";

    @Override
    public float acuracyFactor(final Hero hero) {

        int encumbrance = STR - hero.STR();

        if (this instanceof MissileWeapon) {
            switch (hero.heroClass) {
            case WARRIOR:
                encumbrance += 3;
                break;
            case HUNTRESS:
                encumbrance -= 2;
                break;
            default:
            }
        }

        if (hero.subClass == HeroSubClass.PALADIN) {
            encumbrance--;
        }

        return (encumbrance > 0 ? (float) (ACU / Math.pow(1.5, encumbrance)) : ACU) *
                (imbue == Imbue.ACCURACY ? 1.5f : 1.0f);
    }

    @Override
    public int damageRoll(final Hero hero) {

        int damage = super.damageRoll(hero);

        if ((hero.rangedWeapon != null) == (hero.heroClass == HeroClass.HUNTRESS)) {
            int exStr = hero.STR() - STR;
            if (exStr > 0) {
                damage += Random.IntRange(0, exStr);
            }
        }

        return damage;
    }

    public Weapon enchant(final Enchantment ench) {
        enchantment = ench;
        return this;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return enchantment != null ? enchantment.glowing() : null;
    }

    public boolean isEnchanted() {
        return enchantment != null;
    }

    @Override
    public String name() {
        return enchantment == null ? super.name() : enchantment.name(super.name());
    }

    @Override
    public void proc(final Char attacker, final Char defender, final int damage) {

        if (enchantment != null) {
            enchantment.proc(this, attacker, defender, damage);
        }

        if (!levelKnown) {
            if (--hitsToKnow <= 0) {
                levelKnown = true;
                GLog.i(TXT_IDENTIFY, name(), toString());
                Badges.validateItemLevelAquired(this);
            }
        }
    }

    @Override
    public Item random() {
        if (Random.Float() < 0.4) {
            int n = 1;
            if (Random.Int(3) == 0) {
                n++;
                if (Random.Int(3) == 0) {
                    n++;
                }
            }
            if (Random.Int(2) == 0) {
                upgrade(n);
            } else {
                degrade(n);
                cursed = true;
            }
        }
        return this;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        enchantment = (Enchantment) bundle.get(ENCHANTMENT);
        imbue = bundle.getEnum(IMBUE, Imbue.class);
    }

    @Override
    public float speedFactor(final Hero hero) {

        int encumrance = STR - hero.STR();
        if ((this instanceof MissileWeapon) && (hero.heroClass == HeroClass.HUNTRESS)) {
            encumrance -= 2;
        }
        if (hero.subClass == HeroSubClass.PALADIN) { // TODO check this. paladin STR bonus kick out. add shields!!!
            encumrance--;
        }

        return (encumrance > 0 ? (float) (DLY * Math.pow(1.2, encumrance)) : DLY) *
                (imbue == Imbue.SPEED ? 0.6f : 1.0f);
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ENCHANTMENT, enchantment);
        bundle.put(IMBUE, imbue);
    }

    @Override
    public String toString() {
        return levelKnown ? Utils.format(TXT_TO_STRING, super.toString(), STR) : super.toString();
    }

    public Item upgrade(final boolean enchant) {
        if (enchantment != null) {
            if (!enchant && (Random.Int(level) > 0)) {
                GLog.w(TXT_INCOMPATIBLE);
                enchant(null);
            }
        } else {
            if (enchant) {
                enchant(Enchantment.random());
            }
        }

        return super.upgrade();
    }
}
