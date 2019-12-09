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

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.mobs.Statue;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class StatuePainter extends Painter {

  public static void paint(final Level level, final Room room) {

    Painter.fill(level, room, Terrain.WALL);
    Painter.fill(level, room, 1, Terrain.EMPTY);

    Point c = room.center();
    int cx = c.x;
    int cy = c.y;

    Room.Door door = room.entrance();

    door.set(Room.Door.Type.LOCKED);
    level.addItemToSpawn(new IronKey());

    if (door.x == room.left) {

      Painter.fill(level, room.right - 1, room.top + 1, 1, room.height() - 1, Terrain.STATUE);
      cx = room.right - 2;

    } else if (door.x == room.right) {

      Painter.fill(level, room.left + 1, room.top + 1, 1, room.height() - 1, Terrain.STATUE);
      cx = room.left + 2;

    } else if (door.y == room.top) {

      Painter.fill(level, room.left + 1, room.bottom - 1, room.width() - 1, 1, Terrain.STATUE);
      cy = room.bottom - 2;

    } else if (door.y == room.bottom) {

      Painter.fill(level, room.left + 1, room.top + 1, room.width() - 1, 1, Terrain.STATUE);
      cy = room.top + 2;

    }

    Statue statue = new Statue();
    statue.pos = cx + (cy * Level.WIDTH);
    level.mobs.add(statue);
    Actor.occupyCell(statue);
  }
}
