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
package com.watabou.pixeldungeon.effects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.watabou.gltextures.Gradient;
import com.watabou.gltextures.SmartTexture;
import com.watabou.noosa.Game;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.utils.PointF;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.util.FloatMath;

public class HolyFlare extends Visual {

  private float duration = 0;
  private float lifespan;

  private boolean lightMode = true;

  private SmartTexture texture;

  private FloatBuffer vertices;
  private ShortBuffer indices;

  final int nRays;

  @SuppressLint("FloatMath")
  public HolyFlare(final int rayRitm, final float radius) {
    super(0, 0, 16, 16);

    // FIXME
    // Texture is incorrectly created every time we need
    // to show the effect, it must be refactored

    int gradient[] = { 0xFFFFFFFF, 0x00FFFFFF };
    texture = new Gradient(gradient);

    nRays = 1;

    angle = 64;
    angularSpeed = 0;

    vertices = ByteBuffer.allocateDirect(((rayRitm * 2) + 1) * 4 * (Float.SIZE / 8))
        .order(ByteOrder.nativeOrder()).asFloatBuffer();

    indices = ByteBuffer.allocateDirect((rayRitm * 3 * Short.SIZE) / 8)
        .order(ByteOrder.nativeOrder()).asShortBuffer();

    float v[] = new float[4];

    v[0] = 0;
    v[1] = 0;
    v[2] = 0.25f;
    v[3] = 0;
    vertices.put(v);

    v[2] = 0.75f;
    v[3] = 0;

    for (int i = 0; i < rayRitm; i++) {

      float a = (i * 3.1415926f * 2) / rayRitm;
      v[0] = FloatMath.cos(a) * radius;
      v[1] = FloatMath.sin(a) * radius;
      vertices.put(v);

      a += (3.1415926f * 2) / rayRitm / 2;
      v[0] = FloatMath.cos(a) * radius;
      v[1] = FloatMath.sin(a) * radius;
      vertices.put(v);

      indices.put((short) 0);
      indices.put((short) (1 + (i * 2)));
      indices.put((short) (2 + (i * 2)));
    }

    indices.position(0);
  }

  @Override
  public PointF center(final PointF p) {
    x = p.x - (width / 2);
    y = p.y - (height / 2);
    return p;
  }

  public PointF centerVertivalHighTop(final PointF p) {
    return new PointF(p.x, p.y - height);
  }

  public HolyFlare color(final int color, final boolean lightMode) {
    this.lightMode = lightMode;
    hardlight(color);

    return this;
  }

  @Override
  public void draw() {

    super.draw();

    if (lightMode) {
      GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
      drawRays();
      GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    } else {
      drawRays();
    }
  }

  private void drawRays() {

    NoosaScript script = NoosaScript.get();

    texture.bind();

    script.uModel.valueM4(matrix);
    script.lighting(
        rm, gm, bm, am,
        ra, ga, ba, aa);

    script.camera(camera);
    script.drawElements(vertices, indices, nRays * 3);
  }

  public HolyFlare show(final Char character, final float duration) {
    SpellSprite.show(character, SpellSprite.EMERALD);
    point(centerVertivalHighTop(character.sprite.center()));
    character.sprite.parent.addToBack(this);

    lifespan = this.duration = duration;

    return this;
  }

  @Override
  public void update() {

    if (duration > 0) {
      if ((lifespan -= Game.elapsed) > 0) {

        float p = 1 - (lifespan / duration); // 0 -> 1
        p = p < 0.25f ? p * 4 : (1 - p) * 1.333f;
        scale.set(p);
        alpha(p);

      } else {
        killAndErase();
      }
    }
  }
}
