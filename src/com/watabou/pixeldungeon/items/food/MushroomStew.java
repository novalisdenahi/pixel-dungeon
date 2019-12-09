/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Tóth Dániel
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
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class MushroomStew extends Food {

  {
    name = "mushroom stew";
    image = ItemSpriteSheet.MUSHROOM_STEW;
    energy = Hunger.STARVING;
  }

  @Override
  public void execute(final Hero hero, final String action) {
    hero.HP = hero.HT;
    Buff.detach(hero, Poison.class);
    Buff.detach(hero, Cripple.class);
    Buff.detach(hero, Weakness.class);
    Buff.detach(hero, Bleeding.class);

    hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);

    super.execute(hero, action);
  }

  @Override
  public String info() {
    return "This stew made from purple magic mushroom. Smells good, looks... not bad. The cook said fine, worth a try.";
  }

  @Override
  public int price() {
    return 30 * quantity;
  }
}
