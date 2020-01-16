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

import java.util.HashSet;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GoblinKingSprite;
import com.watabou.utils.Random;

public class GoblinKing extends Mob {

  private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

  static {
    RESISTANCES.add(Death.class);
  }

  {
    name = Dungeon.depth == Statistics.getDeepestFloor(Dungeon.dungeonType) ? "King of the Goblins"
        : "Descendant of the Goblin King";
    spriteClass = GoblinKingSprite.class;

    HP = HT = 100;
    EXP = 20;
    defenseSkill = 18;

  }

  @Override
  public boolean act() {
    return super.act();
  }

  @Override
  public int attackProc(final Char enemy, final int damage) {
    int dice = Random.Int(10);
    switch (dice) {
      case 0:
        Buff.affect(enemy, Bleeding.class).set(damage);
        return damage;
      case 1:
        Buff.affect(enemy, Paralysis.class, Random.Float(1, 2));
        return damage;
      case 2:
        Buff.affect(enemy, Cripple.class);
        return damage;
      case 3:
        yell("Die human!!!");
        return damage;
      default:
        return damage;
    }
  }

  @Override
  public int attackSkill(final Char target) {
    return 20;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(8, 15);
  }

  @Override
  public String description() {
    return "The crown is worn. The weapon is rusty. This is the superiority of the goblin king. "
            + "But be careful, to be the Goblin King the goblin must be the wildest and worst goblin.";
  }

  @Override
  public void die(final Object cause) {

    super.die(cause);

    GameScene.bossSlain();
    Dungeon.level.drop(new SkeletonKey(), pos).sprite.drop();

    Badges.validateBossSlain();
    yell("My crown! No!");
  }

  @Override
  public int dr() {
    return 4;
  }

  @Override
  public void move(final int step) {
    super.move(step);
  }

  @Override
  public void notice() {
    super.notice();
    yell("Who is brave enough to face me? The King!");
  }

  @Override
  public HashSet<Class<?>> resistances() {
    return RESISTANCES;
  }
}
