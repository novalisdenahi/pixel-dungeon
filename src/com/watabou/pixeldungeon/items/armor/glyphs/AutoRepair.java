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

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.Armor.Glyph;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;

public class AutoRepair extends Glyph {

  private static final String TXT_AUTO_REPAIR = "%s of auto-repair";

  private static Glowing GRAY = new Glowing(0xCC8888);

  @Override
  public Glowing glowing() {
    return GRAY;
  }

  @Override
  public String name(final String weaponName) {
    return String.format(TXT_AUTO_REPAIR, weaponName);
  }

  @Override
  public int proc(final Armor armor, final Char attacker, final Char defender, final int damage) {
    if ((defender instanceof Hero) && (Dungeon.gold >= armor.tier)) {
      Dungeon.gold -= armor.tier;
      armor.polish();
    }
    return damage;
  }

}
