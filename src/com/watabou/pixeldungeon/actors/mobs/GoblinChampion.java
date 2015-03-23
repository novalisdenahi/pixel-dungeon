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
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinAsh;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.food.Rum;
import com.watabou.pixeldungeon.sprites.GoblinChampionSprite;
import com.watabou.utils.Random;

public class GoblinChampion extends Mob {

    {
        name = "goblin champion";
        spriteClass = GoblinChampionSprite.class;

        HP = HT = 18;
        defenseSkill = 12;

        EXP = 5;
        maxLvl = 10;

        lootGenerator();

    }

    @Override
    public int attackSkill(final Char target) {
        return 16; // TODO not too much
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 9);
    }

    @Override
    public String description() {
        // TODO FIX desc
        return
                "Goblins are small goblinoids. They organized in tribes living under the surface. If you see one goblin "
                + "you can be sure there are more of them. The goblins sneaking into villages and towns by night to take what they can. ";
    }

    @Override
    public void die(final Object cause) {
        GoblinAsh.Quest.process(pos);
        super.die(cause);
    }

    @Override
    public int dr() {
        return 5;
    }

    private void lootGenerator() {
        if (Random.Int(2) == 0) {
            loot = Gold.class;
            lootChance = 0.5f;
        } else {
            loot = Rum.class;
            lootChance = 0.3f;
        }

    }
}
