/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014 Tóth Dániel
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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.sprites.ImpSprite;
import com.watabou.pixeldungeon.utils.Utils;

public class GoblinShopkeeper extends Shopkeeper {

    private static final String TXT_GREETINGS = "Time is money, friend! Hurry!";

    {
        name = "Goblin Usurer";
        spriteClass = ImpSprite.class;
    }

    private boolean seenBefore = false;

    @Override
    protected boolean act() {

        if (!seenBefore && Dungeon.visible[pos]) {
            yell(Utils.format(TXT_GREETINGS));
            seenBefore = true;
        }

        return super.act();
    }

    @Override
    public String description() {
        return
        "This goblin looks frendly and smart. Maybe a little bit looks too smart. He is a sly boots. "
                + " Watch out for your purse.";
    }

}
