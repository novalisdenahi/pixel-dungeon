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

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.utils.GLog;

public abstract class EquipableItem extends Item {

  private static final String TXT_UNEQUIP_CURSED = "You can't remove cursed %s!";

  public static final String AC_EQUIP = "EQUIP";
  public static final String AC_UNEQUIP = "UNEQUIP";

  protected static void equipCursed(final Hero hero) {
    hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
    Sample.INSTANCE.play(Assets.SND_CURSED);
  }

  @Override
  public void cast(final Hero user, final int dst) {

    if (isEquipped(user)) {
      if ((quantity == 1) && !this.doUnequip(user, false, false)) {
        return;
      }
    }

    super.cast(user, dst);
  }

  @Override
  public void doDrop(final Hero hero) {
    if (!isEquipped(hero) || doUnequip(hero, false, false)) {
      super.doDrop(hero);
    }
  }

  public abstract boolean doEquip(Hero hero);

  public final boolean doUnequip(final Hero hero, final boolean collect) {
    return doUnequip(hero, collect, true);
  }

  public boolean doUnequip(final Hero hero, final boolean collect, final boolean single) {

    if (cursed) {
      GLog.w(TXT_UNEQUIP_CURSED, name());
      return false;
    }

    if (single) {
      hero.spendAndNext(time2equip(hero));
    } else {
      hero.spend(time2equip(hero));
    }

    if (collect && !collect(hero.belongings.backpack)) {
      Dungeon.level.drop(this, hero.pos);
    }

    return true;
  }

  @Override
  public void execute(final Hero hero, final String action) {
    if (action.equals(AC_EQUIP)) {
      doEquip(hero);
    } else if (action.equals(AC_UNEQUIP)) {
      doUnequip(hero, true);
    } else {
      super.execute(hero, action);
    }
  }

  protected float time2equip(final Hero hero) {
    return 1;
  }
}
