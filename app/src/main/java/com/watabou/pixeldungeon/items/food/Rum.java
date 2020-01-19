/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Toth Daniel
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

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Drunk;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;

public class Rum extends Item {

  private static final float TIME_TO_DRINK = 2f;

  public static final String AC_DRINK = "DRINK";

  // TODO add throw and shutter effect
  public String message =
      "Ahh! This is strong! You feel dizzy and brave enough to fight with anyone.";

  {
    stackable = true;
    name = "Goblin RUM";
    image = ItemSpriteSheet.RUM;
  }

  @Override
  public ArrayList<String> actions(final Hero hero) {
    ArrayList<String> actions = super.actions(hero);
    actions.add(AC_DRINK);
    return actions;
  }

  @Override
  public void execute(final Hero hero, final String action) {
    if (action.equals(AC_DRINK)) {

      detach(hero.belongings.backpack);

      GLog.i(message);
      Buff.affect(hero, Drunk.class);
      hero.sprite.operate(hero.pos);
      hero.busy();
      Sample.INSTANCE.play(Assets.SND_DRINK);

      hero.spend(TIME_TO_DRINK);

    } else {

      super.execute(hero, action);

    }
  }

  @Override
  public String info() {
    return "Smell's bad. It's alcohol, but it has no connection to the Dwarfs beer or the Elves wine."
        + "Remember: Drink responsibly!";
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
    return 5 * quantity;
  }

  @Override
  protected void onThrow(final int cell) {
    if ((Dungeon.level.map[cell] == Terrain.WELL) || Level.pit[cell]) {

      super.onThrow(cell);

    } else {

      shatter(cell);

    }
  }

  public void shatter(final int cell) {
    if (Dungeon.visible[cell]) {
      GLog.i("The bottle shatters and the RUM splashes harmlessly.");
      Sample.INSTANCE.play(Assets.SND_SHATTER);
      splash(cell);
    }
  }

  protected void splash(final int cell) {
    final int color = ItemSprite.pick(image, 8, 10);
    Splash.at(cell, color, 5);
  }
}
