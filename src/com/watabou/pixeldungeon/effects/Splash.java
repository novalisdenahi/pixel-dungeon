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

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Splash {

    private static class SplashFactory extends Emitter.Factory {

        public int color;
        public float dir;
        public float cone;

        @Override
        public void emit(final Emitter emitter, final int index, final float x, final float y) {
            PixelParticle p = (PixelParticle) emitter.recycle(PixelParticle.Shrinking.class);

            p.reset(x, y, color, 4, Random.Float(0.5f, 1.0f));
            p.speed.polar(Random.Float(dir - (cone / 2), dir + (cone / 2)), Random.Float(40, 80));
            p.acc.set(0, +100);
        }
    }

    private static final SplashFactory FACTORY = new SplashFactory();

    public static void at(final int cell, final int color, final int n) {
        Splash.at(DungeonTilemap.tileCenterToWorld(cell), color, n);
    }

    public static void at(final PointF p, final float dir, final float cone, final int color, final int n) {

        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        emitter.pos(p);

        FACTORY.color = color;
        FACTORY.dir = dir;
        FACTORY.cone = cone;
        emitter.burst(FACTORY, n);
    }

    public static void at(final PointF p, final int color, final int n) {

        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        emitter.pos(p);

        FACTORY.color = color;
        FACTORY.dir = -3.1415926f / 2;
        FACTORY.cone = 3.1415926f;
        emitter.burst(FACTORY, n);
    }
}
