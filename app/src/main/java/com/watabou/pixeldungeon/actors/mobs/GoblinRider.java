/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Toth Daniel
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
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.sprites.GoblinRiderSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class GoblinRider extends Mob {

  {
    name = "goblin rider";
    spriteClass = GoblinRiderSprite.class;

    HP = HT = 22;
    defenseSkill = 8;

    EXP = 5;
    maxLvl = 10;

  }

  @Override
  public int attackSkill(final Char target) {
    return 12;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(4, 9);
  }

  @Override
  public String defenseVerb() {
    return "blocked";
  }

  @Override
  public String description() {
    return "It's a goblin ... It's a Worg .... It's a Goblin rider. Yes it's a goblin rider."
        + "Hard to decide which is worse. Better kill both of them. ";
  }

  @Override
  public void die(final Object cause) {
    // super Char die
    destroy();
    // play the good spire
    if (Random.Int(3) == 0) {
      ((GoblinRiderSprite) sprite).dieWithoutGoblin();
      Goblin goblin = new Goblin();
      if (buff(Burning.class) != null) {
        Buff.affect(goblin, Burning.class).reignite(goblin);
      }
      if (buff(Poison.class) != null) {
        Buff.affect(goblin, Poison.class).set(2);
      }
      goblin.HP = (goblin.HT - Random.Int(6));
      goblin.pos = pos;
      goblin.state = goblin.HUNTING;
    } else {
      sprite.die();
    }

    // super mob die
    if (Dungeon.hero.lvl <= (maxLvl + 2)) {
      dropLoot();
    }
    if (Dungeon.hero.isAlive() && !Dungeon.visible[pos]) {
      GLog.i(super.TXT_DIED);
    }
  }

  @Override
  public int dr() {
    return 5;
  }

}
