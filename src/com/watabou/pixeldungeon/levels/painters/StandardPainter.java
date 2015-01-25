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
package com.watabou.pixeldungeon.levels.painters;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class StandardPainter extends Painter {

    public static void paint(final Level level, final Room room) {

        Painter.fill(level, room, Terrain.WALL);
        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        if (!Dungeon.bossLevel() && (Random.Int(5) == 0)) {
            switch (Random.Int(6)) {
            case 0:
                if (level.feeling != Level.Feeling.GRASS) {
                    if ((Math.min(room.width(), room.height()) >= 4) && (Math.max(room.width(), room.height()) >= 6)) {
                        StandardPainter.paintGraveyard(level, room);
                        return;
                    }
                    break;
                } else {
                    // Burned room
                }
            case 1:
                if (Dungeon.depth > 1) {
                    StandardPainter.paintBurned(level, room);
                    return;
                }
                break;
            case 2:
                if (Math.max(room.width(), room.height()) >= 4) {
                    StandardPainter.paintStriped(level, room);
                    return;
                }
                break;
            case 3:
                if ((room.width() >= 6) && (room.height() >= 6)) {
                    StandardPainter.paintStudy(level, room);
                    return;
                }
                break;
            case 4:
                if (level.feeling != Level.Feeling.WATER) {
                    if ((room.connected.size() == 2) && (room.width() >= 4) && (room.height() >= 4)) {
                        StandardPainter.paintBridge(level, room);
                        return;
                    }
                    break;
                } else {
                    // Fissure
                }
            case 5:
                if (!Dungeon.bossLevel() && !Dungeon.bossLevel(Dungeon.depth + 1) &&
                        (Math.min(room.width(), room.height()) >= 5)) {
                    StandardPainter.paintFissure(level, room);
                    return;
                }
                break;
            }
        }

        Painter.fill(level, room, 1, Terrain.EMPTY);
    }

    private static void paintBridge(final Level level, final Room room) {

        Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1,
                !Dungeon.bossLevel() && !Dungeon.bossLevel(Dungeon.depth + 1) && (Random.Int(3) == 0) ?
                        Terrain.CHASM :
                        Terrain.WATER);

        Point door1 = null;
        Point door2 = null;
        for (Point p : room.connected.values()) {
            if (door1 == null) {
                door1 = p;
            } else {
                door2 = p;
            }
        }

        if (((door1.x == room.left) && (door2.x == room.right)) ||
                ((door1.x == room.right) && (door2.x == room.left))) {

            int s = room.width() / 2;

            Painter.drawInside(level, room, door1, s, Terrain.EMPTY_SP);
            Painter.drawInside(level, room, door2, s, Terrain.EMPTY_SP);
            Painter.fill(level, room.center().x, Math.min(door1.y, door2.y), 1, Math.abs(door1.y - door2.y) + 1,
                    Terrain.EMPTY_SP);

        } else if (((door1.y == room.top) && (door2.y == room.bottom)) ||
                ((door1.y == room.bottom) && (door2.y == room.top))) {

            int s = room.height() / 2;

            Painter.drawInside(level, room, door1, s, Terrain.EMPTY_SP);
            Painter.drawInside(level, room, door2, s, Terrain.EMPTY_SP);
            Painter.fill(level, Math.min(door1.x, door2.x), room.center().y, Math.abs(door1.x - door2.x) + 1, 1,
                    Terrain.EMPTY_SP);

        } else if (door1.x == door2.x) {

            Painter.fill(level, door1.x == room.left ? room.left + 1 : room.right - 1, Math.min(door1.y, door2.y), 1,
                    Math.abs(door1.y - door2.y) + 1, Terrain.EMPTY_SP);

        } else if (door1.y == door2.y) {

            Painter.fill(level, Math.min(door1.x, door2.x), door1.y == room.top ? room.top + 1 : room.bottom - 1,
                    Math.abs(door1.x - door2.x) + 1, 1, Terrain.EMPTY_SP);

        } else if ((door1.y == room.top) || (door1.y == room.bottom)) {

            Painter.drawInside(level, room, door1, Math.abs(door1.y - door2.y), Terrain.EMPTY_SP);
            Painter.drawInside(level, room, door2, Math.abs(door1.x - door2.x), Terrain.EMPTY_SP);

        } else if ((door1.x == room.left) || (door1.x == room.right)) {

            Painter.drawInside(level, room, door1, Math.abs(door1.x - door2.x), Terrain.EMPTY_SP);
            Painter.drawInside(level, room, door2, Math.abs(door1.y - door2.y), Terrain.EMPTY_SP);

        }
    }

    private static void paintBurned(final Level level, final Room room) {
        for (int i = room.top + 1; i < room.bottom; i++) {
            for (int j = room.left + 1; j < room.right; j++) {
                int t = Terrain.EMBERS;
                switch (Random.Int(5)) {
                case 0:
                    t = Terrain.EMPTY;
                    break;
                case 1:
                    t = Terrain.FIRE_TRAP;
                    break;
                case 2:
                    t = Terrain.SECRET_FIRE_TRAP;
                    break;
                case 3:
                    t = Terrain.INACTIVE_TRAP;
                    break;
                }
                level.map[(i * Level.WIDTH) + j] = t;
            }
        }
    }

    private static void paintFissure(final Level level, final Room room) {
        Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1, Terrain.EMPTY);

        for (int i = room.top + 2; i < (room.bottom - 1); i++) {
            for (int j = room.left + 2; j < (room.right - 1); j++) {
                int v = Math.min(i - room.top, room.bottom - i);
                int h = Math.min(j - room.left, room.right - j);
                if ((Math.min(v, h) > 2) || (Random.Int(2) == 0)) {
                    Painter.set(level, j, i, Terrain.CHASM);
                }
            }
        }
    }

    private static void paintGraveyard(final Level level, final Room room) {
        Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1, Terrain.GRASS);

        int w = room.width() - 1;
        int h = room.height() - 1;
        int nGraves = Math.max(w, h) / 2;

        int index = Random.Int(nGraves);

        int shift = Random.Int(2);
        for (int i = 0; i < nGraves; i++) {
            int pos = w > h ?
                    room.left + 1 + shift + (i * 2) + ((room.top + 2 + Random.Int(h - 2)) * Level.WIDTH) :
                    (room.left + 2 + Random.Int(w - 2)) + ((room.top + 1 + shift + (i * 2)) * Level.WIDTH);
            level.drop(i == index ? Generator.random() : new Gold(), pos).type = Heap.Type.TOMB;
        }
    }

    private static void paintStriped(final Level level, final Room room) {
        Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1, Terrain.EMPTY_SP);

        if (room.width() > room.height()) {
            for (int i = room.left + 2; i < room.right; i += 2) {
                Painter.fill(level, i, room.top + 1, 1, room.height() - 1, Terrain.HIGH_GRASS);
            }
        } else {
            for (int i = room.top + 2; i < room.bottom; i += 2) {
                Painter.fill(level, room.left + 1, i, room.width() - 1, 1, Terrain.HIGH_GRASS);
            }
        }
    }

    private static void paintStudy(final Level level, final Room room) {
        Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1, Terrain.BOOKSHELF);
        Painter.fill(level, room.left + 2, room.top + 2, room.width() - 3, room.height() - 3, Terrain.EMPTY_SP);

        for (Point door : room.connected.values()) {
            if (door.x == room.left) {
                Painter.set(level, door.x + 1, door.y, Terrain.EMPTY);
            } else if (door.x == room.right) {
                Painter.set(level, door.x - 1, door.y, Terrain.EMPTY);
            } else if (door.y == room.top) {
                Painter.set(level, door.x, door.y + 1, Terrain.EMPTY);
            } else if (door.y == room.bottom) {
                Painter.set(level, door.x, door.y - 1, Terrain.EMPTY);
            }
        }

        Painter.set(level, room.center(), Terrain.PEDESTAL);
    }
}
