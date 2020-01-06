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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.levels.painters.AltarPainter;
import com.watabou.pixeldungeon.levels.painters.ArmoryPainter;
import com.watabou.pixeldungeon.levels.painters.BlacksmithPainter;
import com.watabou.pixeldungeon.levels.painters.BossExitPainter;
import com.watabou.pixeldungeon.levels.painters.CryptPainter;
import com.watabou.pixeldungeon.levels.painters.EntrancePainter;
import com.watabou.pixeldungeon.levels.painters.ExitPainter;
import com.watabou.pixeldungeon.levels.painters.GardenPainter;
import com.watabou.pixeldungeon.levels.painters.GoblinPiratePainter;
import com.watabou.pixeldungeon.levels.painters.LaboratoryPainter;
import com.watabou.pixeldungeon.levels.painters.LibraryPainter;
import com.watabou.pixeldungeon.levels.painters.MagicWellPainter;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.levels.painters.PassagePainter;
import com.watabou.pixeldungeon.levels.painters.PitPainter;
import com.watabou.pixeldungeon.levels.painters.PoolPainter;
import com.watabou.pixeldungeon.levels.painters.RatKingPainter;
import com.watabou.pixeldungeon.levels.painters.ShopPainter;
import com.watabou.pixeldungeon.levels.painters.StandardPainter;
import com.watabou.pixeldungeon.levels.painters.StatuePainter;
import com.watabou.pixeldungeon.levels.painters.StoragePainter;
import com.watabou.pixeldungeon.levels.painters.TrapsPainter;
import com.watabou.pixeldungeon.levels.painters.TreasuryPainter;
import com.watabou.pixeldungeon.levels.painters.TunnelPainter;
import com.watabou.pixeldungeon.levels.painters.VaultPainter;
import com.watabou.pixeldungeon.levels.painters.WeakFloorPainter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Room extends Rect implements Graph.Node, Bundlable {

  public static class Door extends Point {

    public static enum Type {
      EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
    }

    public Type type = Type.EMPTY;

    public Door(final int x, final int y) {
      super(x, y);
    }

    public void set(final Type type) {
      if (type.compareTo(this.type) > 0) {
        this.type = type;
      }
    }
  }

  public static enum Type {
    NULL(null), STANDARD(StandardPainter.class), ENTRANCE(EntrancePainter.class), EXIT(
        ExitPainter.class), BOSS_EXIT(BossExitPainter.class), TUNNEL(TunnelPainter.class), PASSAGE(
            PassagePainter.class), SHOP(ShopPainter.class), BLACKSMITH(
                BlacksmithPainter.class), GOBLIN_PIRATE(GoblinPiratePainter.class), TREASURY(
                    TreasuryPainter.class), ARMORY(ArmoryPainter.class), LIBRARY(
                        LibraryPainter.class), LABORATORY(LaboratoryPainter.class), VAULT(
                            VaultPainter.class), TRAPS(TrapsPainter.class), STORAGE(
                                StoragePainter.class), MAGIC_WELL(MagicWellPainter.class), GARDEN(
                                    GardenPainter.class), CRYPT(CryptPainter.class), STATUE(
                                        StatuePainter.class), POOL(PoolPainter.class), RAT_KING(
                                            RatKingPainter.class), WEAK_FLOOR(
                                                WeakFloorPainter.class), PIT(
                                                    PitPainter.class), ALTAR(AltarPainter.class);

    private Method paint;

    private Type(final Class<? extends Painter> painter) {
      try {
        paint = painter.getMethod("paint", Level.class, Room.class);
      } catch (Exception e) {
        paint = null;
      }
    }

    public void paint(final Level level, final Room room) {
      try {
        paint.invoke(null, level, room);
      } catch (Exception e) {
        PixelDungeon.reportException(e);
      }
    }
  }

  public static final ArrayList<Type> SPECIALS = new ArrayList<Type>(Arrays.asList(

      Type.ARMORY, Type.WEAK_FLOOR, Type.MAGIC_WELL, Type.CRYPT, Type.POOL, Type.GARDEN,
      Type.LIBRARY,
      Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.STATUE, Type.LABORATORY, Type.VAULT,
      Type.ALTAR));

  private static final String ROOMS = "rooms";

  public static void restoreRoomsFromBundle(final Bundle bundle) {
    if (bundle.contains(ROOMS)) {
      SPECIALS.clear();
      for (String type : bundle.getStringArray(ROOMS)) {
        SPECIALS.add(Type.valueOf(type));
      }
    } else {
      Room.shuffleTypes();
    }
  };

  public static void shuffleTypes() {
    int size = SPECIALS.size();
    for (int i = 0; i < (size - 1); i++) {
      int j = Random.Int(i, size);
      if (j != i) {
        Type t = SPECIALS.get(i);
        SPECIALS.set(i, SPECIALS.get(j));
        SPECIALS.set(j, t);
      }
    }
  }

  public static void storeRoomsInBundle(final Bundle bundle) {
    String[] array = new String[SPECIALS.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = SPECIALS.get(i).toString();
    }
    bundle.put(ROOMS, array);
  }

  public static void useType(final Type type) {
    if (SPECIALS.remove(type)) {
      SPECIALS.add(type);
    }
  }

  public HashSet<Room> neigbours = new HashSet<Room>();

  public HashMap<Room, Door> connected = new HashMap<Room, Door>();

  public int distance;

  public int price = 1;

  public Type type = Type.NULL;

  public void addNeigbour(final Room other) {

    Rect i = intersect(other);
    if (((i.width() == 0) && (i.height() >= 3)) ||
        ((i.height() == 0) && (i.width() >= 3))) {
      neigbours.add(other);
      other.neigbours.add(this);
    }

  }

  // **** Graph.Node interface ****

  public Point center() {
    return new Point(
        ((left + right) / 2) + (((right - left) & 1) == 1 ? Random.Int(2) : 0),
        ((top + bottom) / 2) + (((bottom - top) & 1) == 1 ? Random.Int(2) : 0));
  }

  public void connect(final Room room) {
    if (!connected.containsKey(room)) {
      connected.put(room, null);
      room.connected.put(this, null);
    }
  }

  @Override
  public int distance() {
    return distance;
  }

  @Override
  public void distance(final int value) {
    distance = value;
  }

  @Override
  public Collection<Room> edges() {
    return neigbours;
  }

  public Door entrance() {
    return connected.values().iterator().next();
  }

  public boolean inside(final int p) {
    int x = p % Level.WIDTH;
    int y = p / Level.WIDTH;
    return (x > left) && (y > top) && (x < right) && (y < bottom);
  }

  @Override
  public int price() {
    return price;
  }

  @Override
  public void price(final int value) {
    price = value;
  }

  public int random() {
    return random(0);
  }

  public int random(final int m) {
    int x = Random.Int(left + 1 + m, right - m);
    int y = Random.Int(top + 1 + m, bottom - m);
    return x + (y * Level.WIDTH);
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    left = bundle.getInt("left");
    top = bundle.getInt("top");
    right = bundle.getInt("right");
    bottom = bundle.getInt("bottom");
    type = Type.valueOf(bundle.getString("type"));
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    bundle.put("left", left);
    bundle.put("top", top);
    bundle.put("right", right);
    bundle.put("bottom", bottom);
    bundle.put("type", type.toString());
  }
}
