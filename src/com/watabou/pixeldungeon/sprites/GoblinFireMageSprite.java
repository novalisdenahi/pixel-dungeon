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
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.mobs.GoblinFireMage;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.utils.Callback;

public class GoblinFireMageSprite extends MobSprite {

    public GoblinFireMageSprite() {
        super();

        texture(Assets.GOBLINMAGE);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle = new Animation(2, true);
        idle.frames(frames, 18, 18, 18, 19, 18, 18, 19, 19);

        run = new Animation(12, true);
        run.frames(frames, 22, 23, 24, 25, 18);

        attack = new Animation(12, false);
        attack.frames(frames, 20, 21, 18);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 26, 27, 28, 29);

        play(idle);

    }

    @Override
    public void onComplete(final Animation anim) {
        if (anim == zap) {
            idle();
        }
        super.onComplete(anim);
    }

    @Override
    public void zap(final int cell) {

        turnTo(ch.pos, cell);
        play(zap);

        MagicMissile.fire(parent, ch.pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((GoblinFireMage) ch).onZapComplete();
                    }
                });
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }
}
