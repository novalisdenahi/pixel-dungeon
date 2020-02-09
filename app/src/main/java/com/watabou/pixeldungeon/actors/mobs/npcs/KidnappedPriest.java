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

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.sprites.KidnappedSprite;
import com.watabou.pixeldungeon.sprites.RatKingSprite;

public class KidnappedPriest extends NPC {

  {
    name = "priest of estera";
    spriteClass = KidnappedSprite.class;

  }

  @Override
  public void add(final Buff buff) {
  }

  @Override
  protected Char chooseEnemy() {
    return null;
  }

  @Override
  public void damage(final int dmg, final Object src) {
  }

  @Override
  public int defenseSkill(final Char enemy) {
    return 1000;
  }

  @Override
  public String description() {
    return "The kidnapped High Priest Of Estera. It looks like she's in a bad shape.";
  }

  @Override
  public void interact() {
    sprite.turnTo(pos, Dungeon.hero.pos);
    yell("Help! Thanks to Estera! My prayer has been answered!");
    //TODO open window ... like the amulett , contains big talk...adn WIN the game

  }

  @Override
  public boolean reset() {
    return true;
  }

  @Override
  public float speed() {
    return 2f;
  }
}
