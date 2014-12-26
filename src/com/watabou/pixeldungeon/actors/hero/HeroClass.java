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
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.SymbolOfEstera;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.items.armor.ClothArmor;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.ShortSword;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.items.weapon.missiles.Dart;
import com.watabou.utils.Bundle;

public enum HeroClass {

    WARRIOR("warrior"), MAGE("mage"), ROGUE("rogue"), HUNTRESS("huntress"), PRIEST("priest");

    private String title;

    public static final String[] WAR_PERKS = {
            "Warriors start with 11 points of Strength.",
            "Warriors start with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
            "Warriors are less proficient with missile weapons.",
            "Any piece of food restores some health when eaten.",
            "Potions of Strength are identified from the beginning.",
    };

    public static final String[] MAG_PERKS = {
            "Mages start with a unique Wand of Magic Missile. This wand can be later \"disenchanted\" to upgrade another wand.",
            "Mages recharge their wands faster.",
            "When eaten, any piece of food restores 1 charge for all wands in the inventory.",
            "Mages can use wands as a melee weapon.",
            "Scrolls of Identify are identified from the beginning."
    };

    public static final String[] ROG_PERKS = {
            "Rogues start with a Ring of Shadows+1.",
            "Rogues identify a type of a ring on equipping it.",
            "Rogues are proficient with light armor, dodging better while wearing one.",
            "Rogues are proficient in detecting hidden doors and traps.",
            "Rogues can go without food longer.",
            "Scrolls of Magic Mapping are identified from the beginning."
    };

    public static final String[] HUN_PERKS = {
            "Huntresses start with 15 points of Health.",
            "Huntresses start with a unique upgradeable boomerang.",
            "Huntresses are proficient with missile weapons and get damage bonus for excessive strength when using them.",
            "Huntresses gain more health from dewdrops.",
            "Huntresses sense neighbouring monsters even if they are hidden behind obstacles."
    };

    public static final String[] PRIEST_PERK = {
            "Priest start with a unique upgradeable item the Symbol of Estera.",
            "Priest have better chance to hit undead creatures.",
            "Priest can deal more damage to undead creatures.",
            "Potions of Healing are identified from the beginning."
    };// TODO add holy water bonus?

    private static final String CLASS = "class";

    private static void initCommon(final Hero hero) {
        (hero.belongings.armor = new ClothArmor()).identify();
        new Food().identify().collect();
    }

    private static void initHuntress(final Hero hero) {

        hero.HP = (hero.HT -= 5);

        (hero.belongings.weapon = new Dagger()).identify();
        Boomerang boomerang = new Boomerang();
        boomerang.identify().collect();

        Dungeon.quickslot = boomerang;

    }

    private static void initMage(final Hero hero) {
        (hero.belongings.weapon = new Knuckles()).identify();

        WandOfMagicMissile wand = new WandOfMagicMissile();
        wand.identify().collect();

        Dungeon.quickslot = wand;

        new ScrollOfIdentify().setKnown();
    }

    private static void initPriest(final Hero hero) {

        (hero.belongings.weapon = new Knuckles()).identify();

        new Dart(5).identify().collect();
        Dungeon.quickslot = Dart.class;

        SymbolOfEstera symbolOfEstera = new SymbolOfEstera();
        symbolOfEstera.identify().collect();

        new PotionOfHealing().setKnown();
    }

    private static void initRogue(final Hero hero) {
        (hero.belongings.weapon = new Dagger()).identify();
        (hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
        new Dart(8).identify().collect();

        hero.belongings.ring1.activate(hero);

        Dungeon.quickslot = Dart.class;

        new ScrollOfMagicMapping().setKnown();
    }

    private static void initWarrior(final Hero hero) {
        hero.STR = hero.STR + 1;

        (hero.belongings.weapon = new ShortSword()).identify();
        new Dart(8).identify().collect();

        Dungeon.quickslot = Dart.class;

        new PotionOfStrength().setKnown();
    }

    public static HeroClass restoreInBundle(final Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? valueOf(value) : ROGUE;
    }

    private HeroClass(final String title) {
        this.title = title;
    }

    public void initHero(final Hero hero) {

        hero.heroClass = this;

        initCommon(hero);

        switch (this) {
        case WARRIOR:
            initWarrior(hero);
            break;

        case MAGE:
            initMage(hero);
            break;

        case ROGUE:
            initRogue(hero);
            break;

        case HUNTRESS:
            initHuntress(hero);
            break;

        case PRIEST:
            initPriest(hero);
            break;
        }

        if (Badges.isUnlocked(masteryBadge())) {
            new TomeOfMastery().collect();
        }

        hero.updateAwareness();
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
        case WARRIOR:
            return Badges.Badge.MASTERY_WARRIOR;
        case MAGE:
            return Badges.Badge.MASTERY_MAGE;
        case ROGUE:
            return Badges.Badge.MASTERY_ROGUE;
        case HUNTRESS:
            return Badges.Badge.MASTERY_HUNTRESS;
        case PRIEST:
            return Badges.Badge.MASTERY_PRIEST;
        }
        return null;
    }

    public String[] perks() {

        switch (this) {
        case WARRIOR:
            return WAR_PERKS;
        case MAGE:
            return MAG_PERKS;
        case ROGUE:
            return ROG_PERKS;
        case HUNTRESS:
            return HUN_PERKS;
        case PRIEST:
            return PRIEST_PERK;
        }

        return null;
    }

    public String spritesheet() {

        switch (this) {
        case WARRIOR:
            return Assets.WARRIOR;
        case MAGE:
            return Assets.MAGE;
        case ROGUE:
            return Assets.ROGUE;
        case HUNTRESS:
            return Assets.HUNTRESS;
        case PRIEST:
            return Assets.PRIEST;
        }

        return null;
    }

    public void storeInBundle(final Bundle bundle) {
        bundle.put(CLASS, toString());
    }

    public String title() {
        return title;
    }
}
