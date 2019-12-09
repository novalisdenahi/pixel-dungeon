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
package com.watabou.pixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class EnergyParticle extends PixelParticle {

  public static final Emitter.Factory FACTORY = new Factory() {
    @Override
    public void emit(final Emitter emitter, final int index, final float x, final float y) {
      ((EnergyParticle) emitter.recycle(EnergyParticle.class)).reset(x, y);
    }

    @Override
    public boolean lightMode() {
      return true;
    };
  };

  public EnergyParticle() {
    super();

    lifespan = 1f;
    color(0xFFFFAA);

    speed.polar(Random.Float(PointF.PI2), Random.Float(24, 32));
  }

  public void reset(final float x, final float y) {
    revive();

    left = lifespan;

    this.x = x - (speed.x * lifespan);
    this.y = y - (speed.y * lifespan);
  }

  @Override
  public void update() {
    super.update();

    float p = left / lifespan;
    am = p < 0.5f ? p * p * 4 : (1 - p) * 2;
    size(Random.Float((5 * left) / lifespan));
  }
}
