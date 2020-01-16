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

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinAsh;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.food.Rum;
import com.watabou.pixeldungeon.sprites.GoblinSprite;
import com.watabou.utils.Random;

public class Goblin extends Mob {

  {
    name = "goblin";
    spriteClass = GoblinSprite.class;

    HP = HT = 12;
    defenseSkill = 4;

    EXP = 2;
    maxLvl = 8;

    lootGenerator();

  }

  @Override
  public int attackSkill(final Char target) {
    return 11;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(2, 5);
  }

  @Override
  public String description() {
    return "Goblins are the smallest goblinoids. They organized in tribes, living under the surface. "
            + "If you can see one, you can be sure there are more goblins around you. "
            +"The goblins sneaking into villages and towns at night to steal what they can.  ";
  }

  @Override
  public void die(final Object cause) {
    GoblinAsh.Quest.process(pos);
    super.die(cause);
  }

  @Override
  public int dr() {
    return 2;
  }

  private void lootGenerator() {
    if (Random.Int(2) == 0) {
      loot = Gold.class;
      lootChance = 0.5f;
    } else {
      loot = Rum.class;
      lootChance = 0.3f;
    }

  }
}
