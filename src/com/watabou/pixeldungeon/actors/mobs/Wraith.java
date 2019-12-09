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

import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Wraith extends Mob {

  private static final float SPAWN_DELAY = 2f;

  private static final String LEVEL = "level";

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

  static {
    IMMUNITIES.add(Death.class);
    IMMUNITIES.add(Terror.class);
  }

  public static void spawnAround(final int pos) {
    for (int n : Level.NEIGHBOURS4) {
      int cell = pos + n;
      if (Level.passable[cell] && (Actor.findChar(cell) == null)) {
        Wraith.spawnAt(cell);
      }
    }
  }

  public static Wraith spawnAt(final int pos) {
    if (Level.passable[pos] && (Actor.findChar(pos) == null)) {

      Wraith w = new Wraith();
      w.adjustStats(Dungeon.depth);
      w.pos = pos;
      w.state = w.HUNTING;
      GameScene.add(w, SPAWN_DELAY);

      w.sprite.alpha(0);
      w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 0.5f));

      w.sprite.emitter().burst(ShadowParticle.CURSE, 5);

      return w;
    } else {
      return null;
    }
  }

  private int level;

  {
    name = "wraith";
    spriteClass = WraithSprite.class;

    mobType = MobType.UNDEAD;
    HP = HT = 1;
    EXP = 0;

    flying = true;
  }

  public void adjustStats(final int level) {
    this.level = level;
    defenseSkill = attackSkill(null) * 5;
    enemySeen = true;
  }

  @Override
  public int attackSkill(final Char target) {
    return 10 + level;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(1, 3 + level);
  }

  @Override
  public String defenseVerb() {
    return "evaded";
  }

  @Override
  public String description() {
    return "A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. " +
        "Being an ethereal entity, it is very hard to hit with a regular weapon.";
  }

  @Override
  public HashSet<Class<?>> immunities() {
    return IMMUNITIES;
  }

  @Override
  public boolean reset() {
    state = WANDERING;
    return true;
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    super.restoreFromBundle(bundle);
    level = bundle.getInt(LEVEL);
    adjustStats(level);
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(LEVEL, level);
  }
}
