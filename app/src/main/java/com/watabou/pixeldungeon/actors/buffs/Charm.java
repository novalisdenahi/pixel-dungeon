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
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.rings.RingOfElements.Resistance;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Charm extends FlavourBuff {

  private static final String OBJECT = "object";

  public static float durationFactor(final Char ch) {
    Resistance r = ch.buff(Resistance.class);
    return r != null ? r.durationFactor() : 1;
  }

  public int object = 0;

  @Override
  public int icon() {
    return BuffIndicator.HEART;
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    super.restoreFromBundle(bundle);
    object = bundle.getInt(OBJECT);
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(OBJECT, object);

  }

  @Override
  public String toString() {
    return "Charmed";
  }
}
