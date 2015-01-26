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
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.Weapon.Enchantment;

public class Instability extends Weapon.Enchantment {

    private static final String TXT_UNSTABLE = "Unstable %s";

    @Override
    public String name(final String weaponName) {
        return String.format(TXT_UNSTABLE, weaponName);
    }

    @Override
    public boolean proc(final Weapon weapon, final Char attacker, final Char defender, final int damage) {
        return Enchantment.random().proc(weapon, attacker, defender, damage);
    }

}
