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
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GoblinCaveBossLevel extends Level {

    private static final int TOP = 2;

    private static final int HALL_WIDTH = 8;
    private static final int HALL_HEIGHT = 8;
    private static final int CHAMBER_HEIGHT = 3;
    private static final int LEFT = (WIDTH - HALL_WIDTH) / 2;

    private static final int CENTER = LEFT + (HALL_WIDTH / 2);
    private static final String DOOR = "door";

    private static final String ENTERED = "entered";

    private static final String DROPPED = "droppped";

    public static int pedestal(final boolean left) {
        if (left) {
            return (((TOP + (HALL_HEIGHT / 2)) * WIDTH) + CENTER) - 2;
        } else {
            return ((TOP + (HALL_HEIGHT / 2)) * WIDTH) + CENTER + 2;
        }
    }

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    private int arenaDoor;

    private boolean enteredArena = false;
    private boolean keyDropped = false;

    @Override
    public void addVisuals(final Scene scene) {
        GoblinCavesLevel.addVisuals(this, scene);
    }

    @Override
    protected boolean build() {

        Painter.fill(this, LEFT, TOP, HALL_WIDTH, HALL_HEIGHT, Terrain.EMPTY);
        // TODO no special or .. idk
        // Painter.fill(this, CENTER, TOP, 1, HALL_HEIGHT, Terrain.EMPTY_SP);

        // TODO statu just nex to the door
        // int y = TOP + 1;
        // while (y < (TOP + HALL_HEIGHT)) {
        // map[((y * WIDTH) + CENTER) - 2] = Terrain.STATUE_SP;
        // map[(y * WIDTH) + CENTER + 2] = Terrain.STATUE_SP;
        // y += 2;
        // }

        // NO PEDESTAL
        // int left = GoblinCaveBossLevel.pedestal(true);
        // int right = GoblinCaveBossLevel.pedestal(false);
        // map[left] = map[right] = Terrain.PEDESTAL;
        // for (int i = left + 1; i < right; i++) {
        // map[i] = Terrain.EMPTY_SP;
        // }

        exit = ((TOP - 1) * WIDTH) + CENTER;
        map[exit] = Terrain.LOCKED_EXIT;

        arenaDoor = ((TOP + HALL_HEIGHT) * WIDTH) + CENTER;
        map[arenaDoor] = Terrain.DOOR;

        Painter.fill(this, LEFT, TOP + HALL_HEIGHT + 1, HALL_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY);
        Painter.fill(this, LEFT, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF);
        Painter.fill(this, (LEFT + HALL_WIDTH) - 1, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF);

        entrance = ((TOP + HALL_HEIGHT + 2 + Random.Int(CHAMBER_HEIGHT - 1)) * WIDTH) + LEFT
                + (/* 1 + */Random.Int(HALL_WIDTH - 2));
        map[entrance] = Terrain.ENTRANCE;

        return true;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos =
                        Random.IntRange(LEFT + 1, (LEFT + HALL_WIDTH) - 2) +
                        (Random.IntRange(TOP + HALL_HEIGHT + 1, TOP + HALL_HEIGHT + CHAMBER_HEIGHT) * WIDTH);
            } while ((pos == entrance) || (map[pos] == Terrain.SIGN));
            drop(item, pos).type = Heap.Type.SKELETON;
        }
    }

    @Override
    protected void createMobs() {
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

        int sign = arenaDoor + WIDTH + 1;
        map[sign] = Terrain.SIGN;
    }

    @Override
    public Heap drop(final Item item, final int cell) {

        if (!keyDropped && (item instanceof SkeletonKey)) {

            keyDropped = true;

            Level.set(arenaDoor, Terrain.DOOR);
            GameScene.updateMap(arenaDoor);
            Dungeon.observe();
        }

        return super.drop(item, cell);
    }

    private boolean outsideEntraceRoom(final int cell) {
        return (cell / WIDTH) < (arenaDoor / WIDTH);
    }

    @Override
    public void press(final int cell, final Char hero) {

        super.press(cell, hero);

        if (!enteredArena && outsideEntraceRoom(cell) && (hero == Dungeon.hero)) {

            enteredArena = true;

            Mob boss = Bestiary.mob(Dungeon.depth);
            boss.state = boss.HUNTING;
            int count = 0;
            do {
                boss.pos = Random.Int(LENGTH);
            } while (!passable[boss.pos] ||
                    !outsideEntraceRoom(boss.pos) ||
                    (Dungeon.visible[boss.pos] && (count++ < 20)));
            GameScene.add(boss);

            if (Dungeon.visible[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
            }

            Level.set(arenaDoor, Terrain.LOCKED_DOOR);
            GameScene.updateMap(arenaDoor);
            Dungeon.observe();
        }
    }

    @Override
    public int randomRespawnCell() {
        return -1;
    }

    @Override
    public Actor respawner() {
        return null;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        arenaDoor = bundle.getInt(DOOR);
        enteredArena = bundle.getBoolean(ENTERED);
        keyDropped = bundle.getBoolean(DROPPED);
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DOOR, arenaDoor);
        bundle.put(ENTERED, enteredArena);
        bundle.put(DROPPED, keyDropped);
    }

    @Override
    public String tileDesc(final int tile) {
        switch (tile) {
        case Terrain.ENTRANCE:
            return "The ladder leads up to the upper depth.";
        case Terrain.EXIT:
            return "The ladder leads down to the lower depth.";
        case Terrain.HIGH_GRASS:
            return "Huge mushrooms block the view.";
        case Terrain.WALL_DECO:
            return "This is an interesting artwork... or bloody hand prints?";
        case Terrain.BOOKSHELF:
            return "Wow a bookshelf! Can goblins read?";
        default:
            return super.tileDesc(tile);
        }
    }

    @Override
    public String tileName(final int tile) {
        switch (tile) {
        case Terrain.GRASS:
            return "Fluorescent moss";
        case Terrain.HIGH_GRASS:
            return "Fluorescent mushrooms";
        case Terrain.WATER:
            return "Murky water";
        default:
            return super.tileName(tile);
        }
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_GOBLIN_CAVES;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_CAVES;
    }
}
