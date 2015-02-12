/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 T�th D�niel
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
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.plants.Plant;

public class Musmrooms extends Plant {

    private static final String TXT_DESC =
            "Lots of little, and some big purple mushroom on the wet ground.";

    {
        image = 8;
        plantName = "Purple Mushrooms";
    }

    @Override
    public void activate(final Char ch) {
        super.activate(ch);

        Dungeon.level.drop(new Mushroom(), pos).sprite.drop();

    }

    @Override
    public String desc() {
        return TXT_DESC;
    }
}
