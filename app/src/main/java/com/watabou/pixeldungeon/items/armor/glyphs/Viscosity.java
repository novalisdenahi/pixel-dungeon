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
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.Armor.Glyph;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Viscosity extends Glyph {

  public static class DeferedDamage extends Buff {

    private static final String DAMAGE = "damage";

    protected int damage = 0;

    @Override
    public boolean act() {
      if (target.isAlive()) {

        target.damage(1, this);
        if ((target == Dungeon.hero) && !target.isAlive()) {
          // FIXME
          Dungeon.fail(
              Utils.format(ResultDescriptions.GLYPH, "enchantment of viscosity", Dungeon.depth));
          GLog.n("The enchantment of viscosity killed you...");

          Badges.validateDeathFromGlyph();
        }
        spend(TICK);

        if (--damage <= 0) {
          detach();
        }

      } else {

        detach();

      }
      return true;
    }

    @Override
    public boolean attachTo(final Char target) {
      if (super.attachTo(target)) {
        postpone(TICK);
        return true;
      } else {
        return false;
      }
    }

    @Override
    public int icon() {
      return BuffIndicator.DEFERRED;
    }

    public void prolong(final int damage) {
      this.damage += damage;
    };

    @Override
    public void restoreFromBundle(final Bundle bundle) {
      super.restoreFromBundle(bundle);
      damage = bundle.getInt(DAMAGE);
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
      super.storeInBundle(bundle);
      bundle.put(DAMAGE, damage);

    }

    @Override
    public String toString() {
      return Utils.format("Defered damage (%d)", damage);
    }
  }

  private static final String TXT_VISCOSITY = "%s of viscosity";

  private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x8844CC);

  @Override
  public Glowing glowing() {
    return PURPLE;
  }

  @Override
  public String name(final String weaponName) {
    return String.format(TXT_VISCOSITY, weaponName);
  }

  @Override
  public int proc(final Armor armor, final Char attacker, final Char defender, final int damage) {

    if (damage == 0) {
      return 0;
    }

    int level = Math.max(0, armor.effectiveLevel());

    if (Random.Int(level + 7) >= 6) {

      DeferedDamage debuff = defender.buff(DeferedDamage.class);
      if (debuff == null) {
        debuff = new DeferedDamage();
        debuff.attachTo(defender);
      }
      debuff.prolong(damage);

      defender.sprite.showStatus(CharSprite.WARNING, "deferred %d", damage);

      return 0;

    } else {
      return damage;
    }
  }
}