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
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;

public class HeroAction {

  public static class Ascend extends HeroAction {
    public Ascend(final int stairs) {
      dst = stairs;
    }
  }

  public static class Attack extends HeroAction {
    public Char target;

    public Attack(final Char target) {
      this.target = target;
    }
  }

  public static class Buy extends HeroAction {
    public Buy(final int dst) {
      this.dst = dst;
    }
  }

  public static class Cook extends HeroAction {
    public Cook(final int pot) {
      dst = pot;
    }
  }

  public static class Descend extends HeroAction {
    public Descend(final int stairs) {
      dst = stairs;
    }
  }

  public static class Interact extends HeroAction {
    public NPC npc;

    public Interact(final NPC npc) {
      this.npc = npc;
    }
  }

  public static class Move extends HeroAction {
    public Move(final int dst) {
      this.dst = dst;
    }
  }

  public static class OpenChest extends HeroAction {
    public OpenChest(final int dst) {
      this.dst = dst;
    }
  }

  public static class PickUp extends HeroAction {
    public PickUp(final int dst) {
      this.dst = dst;
    }
  }

  public static class Unlock extends HeroAction {
    public Unlock(final int door) {
      dst = door;
    }
  }

  public int dst;
}
