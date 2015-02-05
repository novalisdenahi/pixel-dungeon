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
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.sprites.WorgSprite;
import com.watabou.utils.Random;

public class Worg extends Mob {

    {
        name = "worg";
        spriteClass = WorgSprite.class;

        HP = HT = 15;
        defenseSkill = 5;
        baseSpeed = 2f;

        EXP = 3;
        maxLvl = 9;

        loot = new MysteryMeat();
        lootChance = 0.1f;
    }

    @Override
    public int attackSkill(final Char target) {
        return 12;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 6);
    }

    @Override
    public String defenseVerb() {
        return "dodge";
    }

    @Override
    public String description() {
        return
                "Worgs are like the surface wolf. Except they are bigger, strongger and more bloodthirsty. "
                + "The goblins often tame them with the purpose of riding. The worg meat is not realy tasty, "
                + "but eatable if you are hungry. ";
    }

    @Override
    public void die(final Object cause) {
        // Ghost.Quest.process(pos);
        super.die(cause);
    }

    @Override
    public int dr() {
        return 4;
    }
}
