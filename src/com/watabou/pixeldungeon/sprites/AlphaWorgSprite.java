/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Tóth Dániel
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

public class AlphaWorgSprite extends MobSprite {

    private Animation howls;

    public AlphaWorgSprite() {
        super();

        texture(Assets.ALPHAWORG);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(16, false);
        run.frames(frames, 4, 5, 6, 7, 8, 9, 4);

        attack = new Animation(12, false);
        attack.frames(frames, 2, 3, 2);

        howls = new Animation(12, false);
        howls.frames(frames, 14, 15, 16, 17);

        die = new Animation(12, false);
        die.frames(frames, 10, 11, 12, 13);

        play(idle);
    }

    public void howls() {
        play(howls);
    }
}
