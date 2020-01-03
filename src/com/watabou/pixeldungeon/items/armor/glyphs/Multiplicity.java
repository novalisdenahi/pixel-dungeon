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

import java.util.ArrayList;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.Armor.Glyph;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Multiplicity extends Glyph {

  private static final String TXT_MULTIPLICITY = "%s of multiplicity";

  private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xCCAA88);

  @Override
  public Glowing glowing() {
    return PINK;
  }

  @Override
  public String name(final String weaponName) {
    return String.format(TXT_MULTIPLICITY, weaponName);
  }

  @Override
  public int proc(final Armor armor, final Char attacker, final Char defender, final int damage) {

    int level = Math.max(0, armor.effectiveLevel());

    if (Random.Int((level / 2) + 6) >= 5) {

      ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

      for (int element : Level.NEIGHBOURS8) {
        int p = defender.pos + element;
        if ((Actor.findChar(p) == null) && (Level.passable[p] || Level.avoid[p])) {
          respawnPoints.add(p);
        }
      }

      if (respawnPoints.size() > 0) {
        MirrorImage mob = new MirrorImage();
        mob.duplicate((Hero) defender);
        GameScene.add(mob);
        WandOfBlink.appear(mob, Random.element(respawnPoints));

        defender.damage(Random.IntRange(1, defender.HT / 6), this);
        checkOwner(defender);
      }

    }

    return damage;
  }
}
