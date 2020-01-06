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
package com.watabou.pixeldungeon.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.levels.Level;

public class Wound extends Image {

  private static final float TIME_TO_FADE = 0.8f;

  public static void hit(final Char ch) {
    Wound.hit(ch, 0);
  }

  public static void hit(final Char ch, final float angle) {
    Wound w = (Wound) ch.sprite.parent.recycle(Wound.class);
    ch.sprite.parent.bringToFront(w);
    w.reset(ch.pos);
    w.angle = angle;
  }

  public static void hit(final int pos) {
    Wound.hit(pos, 0);
  }

  public static void hit(final int pos, final float angle) {
    Group parent = Dungeon.hero.sprite.parent;
    Wound w = (Wound) parent.recycle(Wound.class);
    parent.bringToFront(w);
    w.reset(pos);
    w.angle = angle;
  }

  private float time;

  public Wound() {
    super(Effects.get(Effects.Type.WOUND));
    origin.set(width / 2, height / 2);
  }

  public void reset(final int p) {
    revive();

    x = ((p % Level.WIDTH) * DungeonTilemap.SIZE) + ((DungeonTilemap.SIZE - width) / 2);
    y = ((p / Level.WIDTH) * DungeonTilemap.SIZE) + ((DungeonTilemap.SIZE - height) / 2);

    time = TIME_TO_FADE;
  }

  @Override
  public void update() {
    super.update();

    if ((time -= Game.elapsed) <= 0) {
      kill();
    } else {
      float p = time / TIME_TO_FADE;
      alpha(p);
      scale.x = 1 + p;
    }
  }
}