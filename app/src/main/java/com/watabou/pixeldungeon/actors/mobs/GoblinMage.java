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
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.GoblinMageSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GoblinMage extends Mob implements Callback {

  private static final float TIME_TO_ZAP = 2f;

  private static final String TXT_MAGICMISSLE_KILLED = "%s's magic missle killed you...";

  {
    name = "goblin mage";
    spriteClass = GoblinMageSprite.class;

    HP = HT = 14;
    defenseSkill = 6;

    EXP = 5;
    maxLvl = 10;

    loot = Generator.Category.SCROLL;
    lootChance = 0.33f;
  }

  @Override
  public int attackSkill(final Char target) {
    return 11;
  }

  @Override
  public void call() {
    next();
  }

  @Override
  protected boolean canAttack(final Char enemy) {
    return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
  }

  @Override
  public int damageRoll() {
    return Random.NormalIntRange(2, 6);
  }

  @Override
  public String description() {
    return "The goblin mage is a honored member of the tribe. "
        + " The tribe fears and respects the mage, not only because of the knowledge of magic, "
        + "but also because of the cunning and meanness.";
  }

  @Override
  protected boolean doAttack(final Char enemy) {

    if (Level.distance(pos, enemy.pos) <= 1) {

      return super.doAttack(enemy);

    } else {

      boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
      if (visible) {
        ((GoblinMageSprite) sprite).zap(enemy.pos);
      } else {
        zap();
      }
      return !visible;
    }
  }

  @Override
  public int dr() {
    return 4;
  }

  public void onZapComplete() {
    zap();
    next();
  }

  private void zap() {
    spend(TIME_TO_ZAP);

    if (Char.hit(this, enemy, true)) {
      int dmg = Random.Int(1, 10);
      enemy.damage(dmg, this);

      if (!enemy.isAlive() && (enemy == Dungeon.hero)) {
        Dungeon.fail(Utils.format(ResultDescriptions.MOB,
            Utils.indefinite(name), Dungeon.depth));
        GLog.n(TXT_MAGICMISSLE_KILLED, name);
      }
    } else {
      enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
    }
  }
}
