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

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.GoblinFireMageSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GoblinFireMage extends GoblinMage implements Callback {

  private static final float TIME_TO_ZAP = 2f;

  private static final String TXT_FIREBOLT_KILLED = "%s's fire bolt killed you...";

  private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

  static {
    RESISTANCES.add(Burning.class);
  }

  {
    name = "insane goblin fire mage";
    spriteClass = GoblinFireMageSprite.class;

    HP = HT = 14;
    defenseSkill = 6;

    EXP = 5;
    maxLvl = 10;

    loot = Generator.Category.POTION;
    lootChance = 0.33f;
  }

  @Override
  public String description() {
    return "The goblin mage is a honored member of the tribe. "
        + " But there are a few exception, like a goblin fire mage. These goblin mages glorify the fire so that "
        + "it is often their own tribe to serve as the flames.";
  }

  @Override
  protected boolean doAttack(final Char enemy) {

    if (Level.distance(pos, enemy.pos) <= 1) {

      return super.doAttack(enemy);

    } else {

      boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
      if (visible) {
        ((GoblinFireMageSprite) sprite).zap(enemy.pos);
      } else {
        zap();
      }
      return !visible;
    }
  }

  @Override
  public HashSet<Class<?>> resistances() {
    return RESISTANCES;
  }

  private void zap() {
    spend(TIME_TO_ZAP);

    if (Char.hit(this, enemy, true)) {
      int dmg = Random.Int(1, 10);
      enemy.damage(dmg, this);

      GameScene.add(Blob.seed(enemy.pos, 1, Fire.class));

      Buff.affect(enemy, Burning.class).reignite(enemy);
      // hmm hmm TODO do we need this ballastica stuff
      // for (int i = 1; i < (Ballistica.distance - 1); i++) {
      // int c = Ballistica.trace[i];
      // if (Level.flamable[c]) {
      // GameScene.add(Blob.seed(c, 1, Fire.class));
      // }
      // }

      if (!enemy.isAlive() && (enemy == Dungeon.hero)) {
        Dungeon.fail(Utils.format(ResultDescriptions.MOB,
            Utils.indefinite(name), Dungeon.depth));
        GLog.n(TXT_FIREBOLT_KILLED, name);
      }
    } else {
      enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
    }
  }
}
