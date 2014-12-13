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
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ThrowingKnife extends MissileWeapon {

    {
        name = "Throwing knife";
        image = ItemSpriteSheet.THROWINGKNIFE;

        STR = 12;

        MIN = 2;
        MAX = 5;

        DLY = 0.5f;
    }

    public ThrowingKnife() {
        this(1);
    }

    public ThrowingKnife(final int number) {
        super();
        quantity = number;
    }

    @Override
    public String desc() {
        return "Lightweight knife without handle. It is not easy to deal with, but is in good hands" +
                " is dangerous. They can be thrown at very high rate.";
    }

    @Override
    public int price() {
        return 15 * quantity;
    }

    @Override
    public Item random() {
        quantity = Random.Int(5, 10);
        return this;
    }
}
