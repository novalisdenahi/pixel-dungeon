/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Toth Daniel
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
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class Pan extends Item {

  {
    name = "pan";
    image = ItemSpriteSheet.PAN;

    unique = true;
  }

  @Override
  public String info() {
    return "Gotten worn old pan.";
  }

  @Override
  public boolean isIdentified() {
    return true;
  }

  @Override
  public boolean isUpgradable() {
    return false;
  }

  @Override
  public int price() {
    return 2;
  }
}
