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
package com.watabou.pixeldungeon.sprites;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.utils.Callback;

import android.graphics.RectF;

public class HeroSprite extends CharSprite {

  private static final int FRAME_WIDTH = 12;
  private static final int FRAME_HEIGHT = 15;

  private static final int RUN_FRAMERATE = 20;

  private static TextureFilm tiers;

  public static Image avatar(final HeroClass cl, final int armorTier) {

    RectF patch = HeroSprite.tiers().get(armorTier);
    Image avatar = new Image(cl.spritesheet());
    RectF frame = avatar.texture.uvRect(1, 0, FRAME_WIDTH, FRAME_HEIGHT);
    frame.offset(patch.left, patch.top);
    avatar.frame(frame);

    return avatar;
  }

  public static TextureFilm tiers() {
    if (tiers == null) {
      SmartTexture texture = TextureCache.get(Assets.ROGUE);
      tiers = new TextureFilm(texture, texture.width, FRAME_HEIGHT);
    }

    return tiers;
  }

  private Animation fly;

  public HeroSprite() {
    super();

    link(Dungeon.hero);

    texture(Dungeon.hero.heroClass.spritesheet());
    updateArmor();

    idle();
  }

  @Override
  public void jump(final int from, final int to, final Callback callback) {
    super.jump(from, to, callback);
    play(fly);
  }

  @Override
  public void move(final int from, final int to) {
    super.move(from, to);
    if (ch.flying) {
      play(fly);
    }
    Camera.main.target = this;
  }

  @Override
  public void place(final int p) {
    super.place(p);
    Camera.main.target = this;
  }

  public boolean sprint(final boolean on) {
    run.delay = on ? 0.625f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
    return on;
  }

  @Override
  public void update() {
    sleeping = ((Hero) ch).restoreHealth;

    super.update();
  }

  public void updateArmor() {

    TextureFilm film =
        new TextureFilm(HeroSprite.tiers(), ((Hero) ch).tier(), FRAME_WIDTH, FRAME_HEIGHT);

    idle = new Animation(1, true);
    idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

    run = new Animation(RUN_FRAMERATE, true);
    run.frames(film, 2, 3, 4, 5, 6, 7);

    die = new Animation(20, false);
    die.frames(film, 8, 9, 10, 11, 12, 11);

    attack = new Animation(15, false);
    attack.frames(film, 13, 14, 15, 0);

    zap = attack.clone();

    operate = new Animation(8, false);
    operate.frames(film, 16, 17, 16, 17);

    fly = new Animation(1, true);
    fly.frames(film, 18);
  }
}
