/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  T�th D�niel
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
package com.watabou.pixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.pixeldungeon.Assets;
import com.watabou.utils.Random;

public class GoblinKingSprite extends MobSprite {

  public GoblinKingSprite() {
    super();

    texture(Assets.GOBLIN_KING);

    // 18 is the frames length 0-17: club, 18-35: sword, 36-53: axe
    int randomKingTexture = 18 * Random.Int(3);

    TextureFilm frames = new TextureFilm(texture, 13, 16);

    idle = new Animation(2, true);
    idle.frames(frames, 0 + randomKingTexture, 0 + randomKingTexture, 0 + randomKingTexture,
        1 + randomKingTexture,
        0 + randomKingTexture, 0 + randomKingTexture, 1 + randomKingTexture, 1 + randomKingTexture);

    run = new Animation(12, true);
    run.frames(frames, 4 + randomKingTexture, 5 + randomKingTexture, 6 + randomKingTexture,
        7 + randomKingTexture,
        0 + randomKingTexture);

    attack = new Animation(12, false);
    attack.frames(frames, 2 + randomKingTexture, 3 + randomKingTexture, 0 + randomKingTexture);

    die = new Animation(12, false);
    die.frames(frames, 8 + randomKingTexture, 9 + randomKingTexture, 10 + randomKingTexture,
        11 + randomKingTexture);

    play(idle);
  }
}
