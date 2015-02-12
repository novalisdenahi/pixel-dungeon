/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Tóth Dániel
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
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.potions.PotionOfToxicGas;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class Mushroom extends Food {

    public static class Seed extends Plant.Seed {
        {
            plantName = "mushroom";

            name = "seed of " + plantName;
            image = ItemSpriteSheet.PM_MUSHROOM;

            plantClass = Musmrooms.class;
            alchemyClass = PotionOfToxicGas.class;
        }

        @Override
        public String desc() {
            return name;
        }
    }

    {
        name = "purple magic mushroom";
        image = ItemSpriteSheet.PM_MUSHROOM;
        energy = Hunger.HUNGRY;
    }

    @Override
    public void execute(final Hero hero, final String action) {
        Buff.prolong(hero, Vertigo.class, Vertigo.duration(hero));
        Buff.affect(hero, Poison.class).set(Poison.durationFactor(hero) * (4 + (Dungeon.depth / 2)));

        super.execute(hero, action);
    }

    @Override
    public String info() {
        return "This is a Purple Magic Mushroom. This is really rare and tasty mushroom and sadly poison. "
                + "The shamans say things you will experience when you taste incredible.";
    }

    @Override
    public int price() {
        return 15;
    }
}
