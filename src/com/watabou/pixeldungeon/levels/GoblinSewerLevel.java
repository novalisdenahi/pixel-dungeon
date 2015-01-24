/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinPirate;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class GoblinSewerLevel extends RegularLevel {

    private static class Sink extends Emitter {

        private int pos;
        private float rippleDelay = 0;

        private static final Emitter.Factory factory = new Factory() {

            @Override
            public void emit(final Emitter emitter, final int index, final float x, final float y) {
                WaterParticle p = (WaterParticle) emitter.recycle(WaterParticle.class);
                p.reset(x, y);
            }
        };

        public Sink(final int pos) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            pos(p.x - 2, p.y + 1, 4, 0);

            pour(factory, 0.05f);
        }

        @Override
        public void update() {
            if (visible = Dungeon.visible[pos]) {

                super.update();

                if ((rippleDelay -= Game.elapsed) <= 0) {
                    GameScene.ripple(pos + WIDTH).y -= DungeonTilemap.SIZE / 2;
                    rippleDelay = Random.Float(0.2f, 0.3f);
                }
            }
        }
    }

    public static final class WaterParticle extends PixelParticle {

        public WaterParticle() {
            super();

            acc.y = 50;
            am = 0.5f;

            color(ColorMath.random(0xb6ccc2, 0x3b6653));
            size(2);
        }

        public void reset(final float x, final float y) {
            revive();

            this.x = x;
            this.y = y;

            speed.set(Random.Float(-2, +2), 0);

            left = lifespan = 0.5f;
        }
    }

    public static void addVisuals(final Level level, final Scene scene) {
        for (int i = 0; i < LENGTH; i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                scene.add(new Sink(i));
            }
        }
    }

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void addVisuals(final Scene scene) {
        super.addVisuals(scene);
        addVisuals(this, scene);
    }

    @Override
    protected void createItems() {
        // TODO do not add later levels
        if (Dungeon.dewVial && (Random.Int(4 - Dungeon.depth) == 0)) {
            addItemToSpawn(new DewVial());
            Dungeon.dewVial = false;
        }

        super.createItems();
    }

    @Override
    protected void createMobs() {
        super.createMobs();
        // TODO add different Quest
        // Ghost.Quest.spawn( this );
        GoblinPirate.Quest.spawn(this, roomEntrance);
    }

    @Override
    protected void decorate() {

        for (int i = 0; i < WIDTH; i++) {
            if ((map[i] == Terrain.WALL) &&
                    (map[i + WIDTH] == Terrain.WATER) &&
                    (Random.Int(4) == 0)) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i = WIDTH; i < (LENGTH - WIDTH); i++) {
            if ((map[i] == Terrain.WALL) &&
                    (map[i - WIDTH] == Terrain.WALL) &&
                    (map[i + WIDTH] == Terrain.WATER) &&
                    (Random.Int(2) == 0)) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i = WIDTH + 1; i < (LENGTH - WIDTH - 1); i++) {
            if (map[i] == Terrain.EMPTY) {

                int count =
                        (map[i + 1] == Terrain.WALL ? 1 : 0) +
                        (map[i - 1] == Terrain.WALL ? 1 : 0) +
                        (map[i + WIDTH] == Terrain.WALL ? 1 : 0) +
                        (map[i - WIDTH] == Terrain.WALL ? 1 : 0);

                if (Random.Int(16) < (count * count)) {
                    map[i] = Terrain.EMPTY_DECO;
                }
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
        return Patch.generate(feeling == Feeling.GRASS ? 0.60f : 0.40f, 4);
    }

    @Override
    public String tileDesc(final int tile) {
        switch (tile) {
        case Terrain.EMPTY_DECO:
            return "Wet yellowish moss covers the floor.";
        case Terrain.BOOKSHELF:
            return "The bookshelf is packed with cheap useless books. Might it burn?";
        default:
            return super.tileDesc(tile);
        }
    }

    @Override
    public String tileName(final int tile) {
        switch (tile) {
        case Terrain.WATER:
            return "Murky water";
        default:
            return super.tileName(tile);
        }
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_GOBLIN_SEWERS;
    }

    @Override
    protected boolean[] water() {
        return Patch.generate(feeling == Feeling.WATER ? 0.60f : 0.45f, 5);
    }

    @Override
    public String waterTex() {
        return Assets.WATER_SEWERS;
    }
}
