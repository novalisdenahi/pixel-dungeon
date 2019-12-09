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
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.ThiefSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief extends Mob {

  private class Fleeing extends Mob.Fleeing {
    @Override
    protected void nowhereToRun() {
      if (buff(Terror.class) == null) {
        sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
        state = HUNTING;
      } else {
        super.nowhereToRun();
      }
    }
  }

  protected static final String TXT_STOLE = "%s stole %s from you!";

  protected static final String TXT_CARRIES = "\n\n%s is carrying a _%s_. Stolen obviously.";

  private static final String ITEM = "item";

  public Item item;

  {
    name = "crazy thief";
    spriteClass = ThiefSprite.class;

    HP = HT = 20;
    defenseSkill = 12;

    EXP = 5;
    maxLvl = 10;

    loot = RingOfHaggler.class;
    lootChance = 0.01f;

    FLEEING = new Fleeing();
  }

  @Override
  protected float attackDelay() {
    return 0.5f;
  }

  @Override
  public int attackProc(final Char enemy, final int damage) {
    if ((item == null) && (enemy instanceof Hero) && steal((Hero) enemy)) {
      state = FLEEING;
    }

    return damage;
  }

  @Override
  public int attackSkill(final Char target) {
    return 12;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(1, 7);
  }

  @Override
  public int defenseProc(final Char enemy, final int damage) {
    if (state == FLEEING) {
      Dungeon.level.drop(new Gold(), pos).sprite.drop();
    }

    return damage;
  }

  @Override
  public String description() {
    String desc =
        "Deeper levels of the dungeon have always been a hiding place for all kinds of criminals. "
            +
            "Not all of them could keep a clear mind during their extended periods so far from daylight. Long ago, "
            +
            "these crazy thieves and bandits have forgotten who they are and why they steal.";

    if (item != null) {
      desc += String.format(TXT_CARRIES, Utils.capitalize(name), item.name());
    }

    return desc;
  }

  @Override
  public void die(final Object cause) {

    super.die(cause);

    if (item != null) {
      Dungeon.level.drop(item, pos).sprite.drop();
    }
  }

  @Override
  public int dr() {
    return 3;
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    super.restoreFromBundle(bundle);
    item = (Item) bundle.get(ITEM);
  }

  protected boolean steal(final Hero hero) {

    Item item = hero.belongings.randomUnequipped();
    if (item != null) {

      GLog.w(TXT_STOLE, name, item.name());

      item.detachAll(hero.belongings.backpack);
      this.item = item;

      return true;
    } else {
      return false;
    }
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(ITEM, item);
  }
}
