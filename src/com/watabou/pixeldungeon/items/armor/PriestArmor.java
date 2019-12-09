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
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.MobType;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PriestArmor extends ClassArmor {
  private static final String TXT_NOT_PRIEST = "Only priest can use this armor!";

  private static final String AC_SPECIAL = "HOLY STRIKE";

  {
    name = "priest armor";
    image = ItemSpriteSheet.ARMOR_PRIEST;
  }

  @Override
  public String desc() {
    return "This shining iron armor bears Estera's blessing. Only A Priest of Estera capable to use the armor power. "
        + "The priest then release the power of the holy armor. Blinded the unbelievers , the wicked will ignite.";
  }

  @Override
  public boolean doEquip(final Hero hero) {
    if (hero.heroClass == HeroClass.PRIEST) {
      return super.doEquip(hero);
    } else {
      GLog.w(TXT_NOT_PRIEST);
      return false;
    }
  }

  @Override
  public void doSpecial() {

    curUser.HP -= (curUser.HP / 3);

    for (Mob mob : Dungeon.level.mobs) {
      if (Level.fieldOfView[mob.pos]) {

        Buff.prolong(mob, Blindness.class, Random.Int(3, 6));
        mob.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
        if ((mob.mobType == MobType.UNDEAD)
            || (mob.mobType == MobType.DEMON)) {
          Buff.affect(mob, Burning.class).reignite(mob);
        }

      }
    }
    new Flare(6, 32).show(curUser.sprite, 2f);
    curUser.spendAndNext(Actor.TICK);

  }

  @Override
  public String special() {
    return AC_SPECIAL;
  }
}
