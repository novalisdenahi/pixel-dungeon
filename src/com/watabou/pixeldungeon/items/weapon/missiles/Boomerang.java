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
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.sprites.MissileSprite;

public class Boomerang extends MissileWeapon {

  {
    name = "boomerang";
    image = ItemSpriteSheet.BOOMERANG;

    STR = 10;

    MIN = 1;
    MAX = 4;

    stackable = false;
  }

  private boolean throwEquiped;

  @Override
  public void cast(final Hero user, final int dst) {
    throwEquiped = isEquipped(user);
    super.cast(user, dst);
  }

  private void circleBack(final int from, final Hero owner) {

    ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).reset(from, curUser.pos,
        curItem, null);

    if (throwEquiped) {
      owner.belongings.weapon = this;
      owner.spend(-TIME_TO_EQUIP);
    } else if (!collect(curUser.belongings.backpack)) {
      Dungeon.level.drop(this, owner.pos).sprite.drop();
    }
  }

  @Override
  public Item degrade() {
    MIN -= 1;
    MAX -= 2;
    return super.degrade();
  }

  @Override
  public String desc() {
    return "Thrown to the enemy this flat curved wooden missile will return to the hands of its thrower.";
  }

  @Override
  public boolean isUpgradable() {
    return true;
  }

  @Override
  public int maxDurability(final int lvl) {
    return 7 * (lvl < 16 ? 16 - lvl : 1);
  }

  @Override
  protected void miss(final int cell) {
    circleBack(cell, curUser);
  }

  @Override
  public void proc(final Char attacker, final Char defender, final int damage) {
    super.proc(attacker, defender, damage);
    if ((attacker instanceof Hero) && (((Hero) attacker).rangedWeapon == this)) {
      circleBack(defender.pos, (Hero) attacker);
    }
  }

  @Override
  public Item upgrade() {
    return upgrade(false);
  }

  @Override
  public Item upgrade(final boolean enchant) {
    MIN += 1;
    MAX += 2;
    super.upgrade(enchant);

    updateQuickslot();

    return this;
  }
}
