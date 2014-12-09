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
package com.watabou.pixeldungeon.items.armor;

import java.util.ArrayList;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

abstract public class ClassArmor extends Armor {

    private static final String TXT_LOW_HEALTH = "Your health is too low!";
    private static final String TXT_NOT_EQUIPPED = "You need to be wearing this armor to use its special power!";

    private static final String ARMOR_STR = "STR";

    private static final String ARMOR_DR = "DR";

    public static ClassArmor upgrade(final Hero owner, final Armor armor) {

        ClassArmor classArmor = null;

        switch (owner.heroClass) {
        case WARRIOR:
            classArmor = new WarriorArmor();
            break;
        case ROGUE:
            classArmor = new RogueArmor();
            break;
        case MAGE:
            classArmor = new MageArmor();
            break;
        case HUNTRESS:
            classArmor = new HuntressArmor();
            break;
        case PRIEST:
            classArmor = new PriestArmor();
            break;
        }

        classArmor.STR = armor.STR;
        classArmor.DR = armor.DR;

        classArmor.inscribe(armor.glyph);

        return classArmor;
    }

    {
        levelKnown = true;
        cursedKnown = true;
        defaultAction = special();
    }

    public ClassArmor() {
        super(6);
    }

    @Override
    public ArrayList<String> actions(final Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if ((hero.HP >= 3) && isEquipped(hero)) {
            actions.add(special());
        }
        return actions;
    }

    @Override
    public String desc() {
        return "The thing looks awesome!";
    }

    abstract public void doSpecial();

    @Override
    public void execute(final Hero hero, final String action) {
        if (action == special()) {

            if (hero.HP < 3) {
                GLog.w(TXT_LOW_HEALTH);
            } else if (!isEquipped(hero)) {
                GLog.w(TXT_NOT_EQUIPPED);
            } else {
                curUser = hero;
                doSpecial();
            }

        } else {
            super.execute(hero, action);
        }
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
    public int price() {
        return 0;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        STR = bundle.getInt(ARMOR_STR);
        DR = bundle.getInt(ARMOR_DR);
    }

    abstract public String special();

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ARMOR_STR, STR);
        bundle.put(ARMOR_DR, DR);
    }
}
