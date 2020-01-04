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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import java.util.HashSet;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.BeeSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bee extends NPC {

  private class Wandering implements AiState {

    @Override
    public boolean act(final boolean enemyInFOV, final boolean justAlerted) {
      if (enemyInFOV) {

        enemySeen = true;

        notice();
        state = HUNTING;
        target = enemy.pos;

      } else {

        enemySeen = false;

        int oldPos = pos;
        if (getCloser(Dungeon.hero.pos)) {
          spend(1 / speed());
          return moveSprite(oldPos, pos);
        } else {
          spend(TICK);
        }

      }
      return true;
    }

    @Override
    public String status() {
      return Utils.format("This %s is wandering", name);
    }
  }

  private static final String LEVEL = "level";

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

  static {
    IMMUNITIES.add(Poison.class);
  }

  {
    name = "golden bee";
    spriteClass = BeeSprite.class;

    viewDistance = 4;

    WANDERING = new Wandering();

    flying = true;
    state = WANDERING;
  }

  private int level;

  @Override
  protected boolean act() {
    HP--;
    if (HP <= 0) {
      die(null);
      return true;
    } else {
      return super.act();
    }
  }

  @Override
  public int attackProc(final Char enemy, final int damage) {
    if (enemy instanceof Mob) {
      ((Mob) enemy).aggro(this);
    }
    return damage;
  }

  @Override
  public int attackSkill(final Char target) {
    return defenseSkill;
  }

  @Override
  protected Char chooseEnemy() {

    if ((enemy == null) || !enemy.isAlive()) {
      HashSet<Mob> enemies = new HashSet<Mob>();
      for (Mob mob : Dungeon.level.mobs) {
        if (mob.hostile && Level.fieldOfView[mob.pos]) {
          enemies.add(mob);
        }
      }

      return enemies.size() > 0 ? Random.element(enemies) : null;

    } else {

      return enemy;

    }
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(HT / 10, HT / 4);
  }

  @Override
  public String description() {
    return "Despite their small size, golden bees tend " +
        "to protect their master fiercely. They don't live long though.";
  }

  @Override
  public HashSet<Class<?>> immunities() {
    return IMMUNITIES;
  }

  @Override
  public void interact() {

    int curPos = pos;

    moveSprite(pos, Dungeon.hero.pos);
    move(Dungeon.hero.pos);

    Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
    Dungeon.hero.move(curPos);

    Dungeon.hero.spend(1 / Dungeon.hero.speed());
    Dungeon.hero.busy();
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    super.restoreFromBundle(bundle);
    spawn(bundle.getInt(LEVEL));
  }

  public void spawn(final int level) {
    this.level = level;

    HT = (3 + level) * 5;
    defenseSkill = 9 + level;
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(LEVEL, level);
  }
}
