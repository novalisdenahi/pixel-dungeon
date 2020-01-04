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
package com.watabou.pixeldungeon.items;

import java.util.ArrayList;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

abstract public class KindOfWeapon extends EquipableItem {

  private static final String TXT_EQUIP_CURSED =
      "you wince as your grip involuntarily tightens around your %s";

  protected static final float TIME_TO_EQUIP = 1f;

  @Override
  public ArrayList<String> actions(final Hero hero) {
    ArrayList<String> actions = super.actions(hero);
    actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
    return actions;
  }

  public void activate(final Hero hero) {
  }

  public float acuracyFactor(final Hero hero) {
    return 1f;
  }

  public int damageRoll(final Hero owner) {
    return Random.NormalIntRange(min(), max());
  }

  @Override
  public boolean doEquip(final Hero hero) {

    detachAll(hero.belongings.backpack);

    if ((hero.belongings.weapon == null) || hero.belongings.weapon.doUnequip(hero, true)) {

      hero.belongings.weapon = this;
      activate(hero);

      QuickSlot.refresh();

      cursedKnown = true;
      if (cursed) {
        EquipableItem.equipCursed(hero);
        GLog.n(TXT_EQUIP_CURSED, name());
      }

      hero.spendAndNext(TIME_TO_EQUIP);
      return true;

    } else {

      collect(hero.belongings.backpack);
      return false;
    }
  }

  @Override
  public boolean doUnequip(final Hero hero, final boolean collect, final boolean single) {
    if (super.doUnequip(hero, collect, single)) {

      hero.belongings.weapon = null;
      return true;

    } else {

      return false;

    }
  }

  @Override
  public boolean isEquipped(final Hero hero) {
    return hero.belongings.weapon == this;
  }

  abstract public int max();

  abstract public int min();

  public void proc(final Char attacker, final Char defender, final int damage) {
  }

  public float speedFactor(final Hero hero) {
    return 1f;
  }

}
