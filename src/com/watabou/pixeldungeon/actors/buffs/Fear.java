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
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.ui.BuffIndicator;

public class Fear extends FlavourBuff {

  public static float duration(final Char ch) {
    return DURATION;
  }

  public static final float DURATION = 3f;

  public static final String NO_WAY_TO_RUN = "AAAAAAAA!!!!";

  public Char theFearfulEnemy;

  @Override
  public boolean attachTo(final Char target) {
    spend(DURATION);
    return super.attachTo(target);
  }

  @Override
  public void detach() {
    super.detach();
  }

  @Override
  public int icon() {
    return BuffIndicator.FEAR;
  }

  public void setTheFearfulEnemy(final Char theFearfulEnemy) {
    this.theFearfulEnemy = theFearfulEnemy;
  }

  @Override
  public String toString() {
    return "In Fear";
  }

}
