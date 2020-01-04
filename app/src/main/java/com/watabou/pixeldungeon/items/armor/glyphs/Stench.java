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

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.Armor.Glyph;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Stench extends Glyph {

  private static final String TXT_STENCH = "%s of stench";

  private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x22CC44);

  @Override
  public Glowing glowing() {
    return GREEN;
  }

  @Override
  public String name(final String weaponName) {
    return String.format(TXT_STENCH, weaponName);
  }

  @Override
  public int proc(final Armor armor, final Char attacker, final Char defender, final int damage) {

    int level = Math.max(0, armor.effectiveLevel());

    if (Level.adjacent(attacker.pos, defender.pos) && (Random.Int(level + 5) >= 4)) {

      GameScene.add(Blob.seed(attacker.pos, 20, ToxicGas.class));

    }

    return damage;
  }

}
