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
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.levels.Room.Type;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CityLevel extends RegularLevel {

  private static class Smoke extends Emitter {

    private static final Emitter.Factory factory = new Factory() {

      @Override
      public void emit(final Emitter emitter, final int index, final float x, final float y) {
        SmokeParticle p = (SmokeParticle) emitter.recycle(SmokeParticle.class);
        p.reset(x, y);
      }
    };

    private int pos;

    public Smoke(final int pos) {
      super();

      this.pos = pos;

      PointF p = DungeonTilemap.tileCenterToWorld(pos);
      pos(p.x - 4, p.y - 2, 4, 0);

      pour(factory, 0.2f);
    }

    @Override
    public void update() {
      if (visible = Dungeon.visible[pos]) {
        super.update();
      }
    }
  }

  public static final class SmokeParticle extends PixelParticle {

    public SmokeParticle() {
      super();

      color(0x000000);
      speed.set(Random.Float(8), -Random.Float(8));
    }

    public void reset(final float x, final float y) {
      revive();

      this.x = x;
      this.y = y;

      left = lifespan = 2f;
    }

    @Override
    public void update() {
      super.update();
      float p = left / lifespan;
      am = p > 0.8f ? 1 - p : p * 0.25f;
      size(8 - (p * 4));
    }
  }

  public static void addVisuals(final Level level, final Scene scene) {
    for (int i = 0; i < LENGTH; i++) {
      if (level.map[i] == Terrain.WALL_DECO) {
        scene.add(new Smoke(i));
      }
    }
  }

  {
    color1 = 0x4b6636;
    color2 = 0xf2f2f2;
  }

  @Override
  public void addVisuals(final Scene scene) {
    super.addVisuals(scene);
    CityLevel.addVisuals(this, scene);
  }

  @Override
  protected void assignRoomType() {
    super.assignRoomType();

    for (Room r : rooms) {
      if (r.type == Type.TUNNEL) {
        r.type = Type.PASSAGE;
      }
    }
  }

  @Override
  protected void createItems() {
    super.createItems();

    Imp.Quest.spawn(this, roomEntrance);
  }

  @Override
  protected void decorate() {

    for (int i = 0; i < LENGTH; i++) {
      if ((map[i] == Terrain.EMPTY) && (Random.Int(10) == 0)) {
        map[i] = Terrain.EMPTY_DECO;
      } else if ((map[i] == Terrain.WALL) && (Random.Int(8) == 0)) {
        map[i] = Terrain.WALL_DECO;
      }
    }

    while (true) {
      int pos = roomEntrance.random();
      if (pos != entrance) {
        map[pos] = Terrain.SIGN;
        break;
      }
    }
  }

  @Override
  protected boolean[] grass() {
    return Patch.generate(feeling == Feeling.GRASS ? 0.60f : 0.40f, 3);
  }

  @Override
  public String tileDesc(final int tile) {
    switch (tile) {
      case Terrain.ENTRANCE:
        return "A ramp leads up to the upper depth.";
      case Terrain.EXIT:
        return "A ramp leads down to the lower depth.";
      case Terrain.WALL_DECO:
      case Terrain.EMPTY_DECO:
        return "Several tiles are missing here.";
      case Terrain.EMPTY_SP:
        return "Thick carpet covers the floor.";
      case Terrain.STATUE:
      case Terrain.STATUE_SP:
        return "The statue depicts some dwarf standing in a heroic stance.";
      case Terrain.BOOKSHELF:
        return "The rows of books on different disciplines fill the bookshelf.";
      default:
        return super.tileDesc(tile);
    }
  }

  @Override
  public String tileName(final int tile) {
    switch (tile) {
      case Terrain.WATER:
        return "Suspiciously colored water";
      case Terrain.HIGH_GRASS:
        return "High blooming flowers";
      default:
        return super.tileName(tile);
    }
  }

  @Override
  public String tilesTex() {
    return Assets.TILES_CITY;
  }

  @Override
  protected boolean[] water() {
    return Patch.generate(feeling == Feeling.WATER ? 0.65f : 0.45f, 4);
  }

  @Override
  public String waterTex() {
    return Assets.WATER_CITY;
  }
}
