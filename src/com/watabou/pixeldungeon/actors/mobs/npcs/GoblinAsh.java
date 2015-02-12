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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import android.util.Log;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.GoblinBroth;
import com.watabou.pixeldungeon.items.food.MushroomStew;
import com.watabou.pixeldungeon.items.quest.DriedRose;
import com.watabou.pixeldungeon.items.quest.Mushroom;
import com.watabou.pixeldungeon.items.quest.Pan;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.levels.GoblinSewerLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GoblinAshSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndAsh;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GoblinAsh extends NPC {

    public static class Quest {

        private static boolean spawned;

        private static boolean given;
        private static boolean alternative;
        private static int left2kill;
        private static boolean processed;
        private static int depth;

        private static boolean completed;

        private static final String NODE = "ash";

        private static final String SPAWNED = "spawned";
        private static final String ALTERNATIVE = "alternative";
        private static final String LEFT2KILL = "left2kill";
        private static final String PROCESSED = "processed";
        private static final String DEPTH = "depth";
        private static final String GIVEN = "given";
        private static final String COMPLITED = "complited";

        private static final String ITEM1 = "item1";
        private static final String ITEM2 = "item2";

        public static Item item1;
        public static Item item2;

        public static void complete() {
            completed = true;

            item1 = null;
            item2 = null;

            // TODO add Badge validition
            Journal.remove(Journal.Feature.ASH);
        }

        public static void process(final int pos) {
            if (spawned && given && !processed && (depth == Dungeon.depth)) {
                if (alternative) {
                    int shrubPos = Dungeon.level.randomRespawnCell();
                    while (Dungeon.level.heaps.get(shrubPos) != null) {
                        shrubPos = Dungeon.level.randomRespawnCell();
                    }
                    Dungeon.level.plant(new Mushroom.Seed(), shrubPos);
                    processed = true;

                } else {

                    if (Random.Int(left2kill) == 0) {
                        Dungeon.level.drop(new Pan(), pos).sprite.drop();
                        processed = true;
                    } else {
                        left2kill--;
                    }

                }
            }
        }

        public static void reset() {
            spawned = false;

            item1 = null;
            item2 = null;
        }

        public static void restoreFromBundle(final Bundle bundle) {

            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

                given = node.getBoolean(GIVEN);
                completed = node.getBoolean(COMPLITED);
                alternative = node.getBoolean(ALTERNATIVE);
                if (!alternative) {
                    left2kill = node.getInt(LEFT2KILL);
                }

                given = node.getBoolean(GIVEN);
                depth = node.getInt(DEPTH);
                processed = node.getBoolean(PROCESSED);

                item1 = (Item) node.get(ITEM1);
                item2 = (Item) node.get(ITEM2);
            } else {
                Quest.reset();
            }
        }

        public static void spawn(final GoblinSewerLevel level, final Room room) {
            // always spawn Goblin dungeon level 6 next to the shop
            // TODO set back to 2 - 4 add alchemy room?
            Log.i("GoblinAsh", "depht: " + Dungeon.depth);
            if (!spawned && (Dungeon.depth == 2)) {

                GoblinAsh npc = new GoblinAsh();
                // TODO alchemy room?
                do {
                    npc.pos = room.random();
                } while ((level.map[npc.pos] == Terrain.ENTRANCE) || (level.map[npc.pos] == Terrain.SIGN));
                level.mobs.add(npc);
                Actor.occupyCell(npc);

                spawned = true;

                alternative = Random.Int(2) == 0;
                if (!alternative) {
                    left2kill = 6;
                }

                processed = false;
                depth = Dungeon.depth;

                given = false;
                completed = false;

                item1 = new GoblinBroth();
                item2 = new MushroomStew();

            }
        }

        public static void storeInBundle(final Bundle bundle) {

            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);

            if (spawned) {

                node.put(ALTERNATIVE, alternative);
                if (!alternative) {
                    node.put(LEFT2KILL, left2kill);
                }

                node.put(DEPTH, depth);
                node.put(PROCESSED, processed);

                node.put(GIVEN, given);
                node.put(COMPLITED, completed);

                node.put(ITEM1, item1);
                node.put(ITEM2, item2);
            }

            bundle.put(NODE, node);
        }
    }

    {
        name = "Ash the goblin cook";
        spriteClass = GoblinAshSprite.class;
    }

    private static final String TXT_COMPLETED = "Please do not bother me! I'm working on a new recipient.";

    private static final String TXT_NICE2MEETYOU = "Please do not bother me! I'm working on a new recipient.";

    private static final String TXT_PAN1 = TXT_NICE2MEETYOU + " ";
    private static final String TXT_MUSHROOM1 = TXT_NICE2MEETYOU + " ";
    private static final String TXT_PAN2 = " ";
    private static final String TXT_MUSHROOM2 = " ";

    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public void add(final Buff buff) {
    }

    @Override
    public void damage(final int dmg, final Object src) {
    }

    @Override
    public int defenseSkill(final Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return "dodge";
    }

    @Override
    public String description() {
        return "He's a goblin. He's a pirate. He isn't ordinary. You can talk to him, "
                + "it's worth a shot. Worst case you run away, he will never catch you with a wooden leg.";
    }

    @Override
    public void interact() {

        sprite.turnTo(pos, Dungeon.hero.pos);
        if (Quest.completed) {
            tell(TXT_COMPLETED);
        } else if (Quest.given) {
            Item item = Quest.alternative ?
                    Dungeon.hero.belongings.getItem(RatSkull.class) :
                        Dungeon.hero.belongings.getItem(DriedRose.class);
                    if (item != null) {
                        GameScene.show(new WndAsh(this, item));
                    } else {
                        GameScene.show(new WndQuest(this, Quest.alternative ? TXT_PAN2 : TXT_MUSHROOM2));

                    }
        } else {
            GameScene.show(new WndQuest(this, Quest.alternative ? TXT_PAN1 : TXT_MUSHROOM1));
            // tell(TXT_NICE2MEETYOU);
            Quest.given = true;

            Journal.add(Journal.Feature.ASH);
        }
    }

    @Override
    public boolean reset() {
        return true;
    }

    private void tell(final String format, final Object... args) {
        GameScene.show(new WndQuest(this, Utils.format(format, args)));
    }
}
