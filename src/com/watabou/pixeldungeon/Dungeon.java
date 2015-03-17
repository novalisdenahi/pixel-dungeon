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
package com.watabou.pixeldungeon;

//import hu.denahi.pixeldungeon.holy.quest.DungeonTips; //TODO check new sign class
import hu.denahi.pixeldungeon.holy.quest.DungeonType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import android.content.Context;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.npcs.Blacksmith;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinPirate;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.watabou.pixeldungeon.items.Ankh;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.CavesBossLevel;
import com.watabou.pixeldungeon.levels.CavesLevel;
import com.watabou.pixeldungeon.levels.CityBossLevel;
import com.watabou.pixeldungeon.levels.CityLevel;
import com.watabou.pixeldungeon.levels.DeadEndLevel;
import com.watabou.pixeldungeon.levels.GoblinSewerBossLevel;
import com.watabou.pixeldungeon.levels.GoblinSewerLevel;
import com.watabou.pixeldungeon.levels.HallsBossLevel;
import com.watabou.pixeldungeon.levels.HallsLevel;
import com.watabou.pixeldungeon.levels.LastLevel;
import com.watabou.pixeldungeon.levels.LastShopLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.PrisonBossLevel;
import com.watabou.pixeldungeon.levels.PrisonLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.SewerBossLevel;
import com.watabou.pixeldungeon.levels.SewerLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.QuestChooseScene;
import com.watabou.pixeldungeon.scenes.StartScene;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndResurrect;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public class Dungeon {

    // private static final String NO_TIPS = "The text  is indecipherable...";
    public static int potionOfStrength;
    public static int scrollsOfUpgrade;
    public static int scrollsOfEnchantment;
    public static boolean dewVial; // true if the dew vial can be spawned
    public static int transmutation; // depth number for a well of transmutation

    // private static final String TXT_DEAD_END =
    // "What are you doing here?!";
    public static int challenges;

    public static int dungeonType;

    private static final String DUNGEON_TYPE = "dungeonType";

    // private static String[] tips;

    public static Hero hero;

    public static Level level;

    public static int depth;

    // public static String tip() {
    //
    // if (level instanceof DeadEndLevel) {
    //
    // return TXT_DEAD_END;
    //
    // } else {
    //
    // int index = depth - 1;
    //
    // if (index < tips.length) {
    // return tips[index];
    // } else {
    // return NO_TIPS;
    // }
    // }
    // }

    public static int gold;
    public static String resultDescription;

    public static HashSet<Integer> chapters;
    public static boolean[] visible = new boolean[Level.LENGTH];
    // Reason of death
    public static boolean nightMode;

    public static SparseArray<ArrayList<Item>> droppedItems;

    // Hero's field of view
    private static final String RG_GAME_FILE = "game.dat";

    private static final String RG_DEPTH_FILE = "depth%d.dat";

    private static final String WR_GAME_FILE = "warrior.dat";

    private static final String WR_DEPTH_FILE = "warrior%d.dat";

    private static final String MG_GAME_FILE = "mage.dat";

    private static final String MG_DEPTH_FILE = "mage%d.dat";

    private static final String RN_GAME_FILE = "ranger.dat";

    private static final String RN_DEPTH_FILE = "ranger%d.dat";

    private static final String PR_GAME_FILE = "priest.dat";

    private static final String PR_DEPTH_FILE = "priest%d.dat";

    private static final String VERSION = "version";

    private static final String CHALLENGES = "challenges";

    private static final String HERO = "hero";

    private static final String GOLD = "gold";

    private static final String DEPTH = "depth";

    private static final String LEVEL = "level";

    private static final String DROPPED = "dropped%d";

    private static final String POS = "potionsOfStrength";

    private static final String SOU = "scrollsOfEnhancement";
    private static final String SOE = "scrollsOfEnchantment";

    private static final String DV = "dewVial";
    private static final String WT = "transmutation";

    private static final String CHAPTERS = "chapters";
    private static final String QUESTS = "quests";

    private static final String BADGES = "badges";
    private static boolean[] passable = new boolean[Level.LENGTH];

    public static boolean bossLevel() {
        return Dungeon.bossLevel(depth);
    }

    public static boolean bossLevel(final int depth) {
        return (depth == 5) || (depth == 10) || (depth == 15) || (depth == 20) || (depth == 25);
    }

    private static boolean chance(final int[] quota, final int number) {

        for (int i = 0; i < quota.length; i += 2) {
            int qDepth = quota[i];
            if (depth <= qDepth) {
                int qNumber = quota[i + 1];
                return Random.Float() < ((float) (qNumber - number) / ((qDepth - depth) + 1));
            }
        }

        return false;
    }

    public static void deleteGame(final HeroClass cl, final boolean deleteLevels) {

        Game.instance.deleteFile(Dungeon.gameFile(cl));

        if (deleteLevels) {
            int depth = 1;
            while (Game.instance.deleteFile(Utils.format(Dungeon.depthFile(cl), depth))) {
                depth++;
            }
        }

        GamesInProgress.delete(cl);
    }

    private static String depthFile(final HeroClass cl) {
        switch (cl) {
        case WARRIOR:
            return WR_DEPTH_FILE;
        case MAGE:
            return MG_DEPTH_FILE;
        case HUNTRESS:
            return RN_DEPTH_FILE;
        case PRIEST:
            return PR_DEPTH_FILE;
        default:
            return RG_DEPTH_FILE;
        }
    }

    public static void dropToChasm(final Item item) {
        int depth = Dungeon.depth + 1;
        ArrayList<Item> dropped = Dungeon.droppedItems.get(depth);
        if (dropped == null) {
            Dungeon.droppedItems.put(depth, dropped = new ArrayList<Item>());
        }
        dropped.add(item);
    }

    public static void fail(final String desc) {
        resultDescription = desc;
        if (hero.belongings.getItem(Ankh.class) == null) {
            Rankings.INSTANCE.submit(false);
        }
    }

    public static int findPath(final Char ch, final int from, final int to, final boolean pass[],
            final boolean[] visible) {

        if (Level.adjacent(from, to)) {
            return (Actor.findChar(to) == null) && (pass[to] || Level.avoid[to]) ? to : -1;
        }

        if (ch.flying || (ch.buff(Amok.class) != null)) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Actor actor : Actor.all()) {
            if (actor instanceof Char) {
                int pos = ((Char) actor).pos;
                if (visible[pos]) {
                    passable[pos] = false;
                }
            }
        }

        return PathFinder.getStep(from, to, passable);

    }

    public static int flee(final Char ch, final int cur, final int from, final boolean pass[], final boolean[] visible) {

        if (ch.flying) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Actor actor : Actor.all()) {
            if (actor instanceof Char) {
                int pos = ((Char) actor).pos;
                if (visible[pos]) {
                    passable[pos] = false;
                }
            }
        }
        passable[cur] = true;

        return PathFinder.getStepBack(cur, from, passable);

    }

    public static Bundle gameBundle(final String fileName) throws IOException {

        InputStream input = Game.instance.openFileInput(fileName);
        Bundle bundle = Bundle.read(input);
        input.close();

        return bundle;
    }

    public static String gameFile(final HeroClass cl) {
        switch (cl) {
        case WARRIOR:
            return WR_GAME_FILE;
        case MAGE:
            return MG_GAME_FILE;
        case HUNTRESS:
            return RN_GAME_FILE;
        case PRIEST:
            return PR_GAME_FILE;
        default:
            return RG_GAME_FILE;
        }
    }

    public static void init() {

        challenges = PixelDungeon.challenges();

        Actor.clear();

        PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);

        Scroll.initLabels();
        Potion.initColors();
        Wand.initWoods();
        Ring.initGems();

        Statistics.reset();
        Journal.reset();

        depth = 0;
        gold = 0;

        droppedItems = new SparseArray<ArrayList<Item>>();

        potionOfStrength = 0;
        scrollsOfUpgrade = 0;
        scrollsOfEnchantment = 0;
        dewVial = true;
        transmutation = Random.IntRange(6, 14);

        chapters = new HashSet<Integer>();

        Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        Imp.Quest.reset();
        GoblinPirate.Quest.reset();

        Room.shuffleTypes();

        QuickSlot.primaryValue = null;
        QuickSlot.secondaryValue = null;

        hero = new Hero();
        hero.live();

        Badges.reset();

        StartScene.curClass.initHero(hero);
        dungeonType = QuestChooseScene.curDungeonType;
        // tips = DungeonTips.getTips(dungeonType);
    }

    public static boolean isChallenged(final int mask) {
        return (challenges & mask) != 0;
    }

    public static void loadGame(final HeroClass cl) throws IOException {
        Dungeon.loadGame(Dungeon.gameFile(cl), true);
    }

    public static void loadGame(final String fileName) throws IOException {
        Dungeon.loadGame(fileName, false);
    }

    public static void loadGame(final String fileName, final boolean fullLoad) throws IOException {

        Bundle bundle = Dungeon.gameBundle(fileName);

        Dungeon.challenges = bundle.getInt(CHALLENGES);

        Dungeon.level = null;
        Dungeon.depth = -1;

        if (fullLoad) {
            PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);
        }

        Scroll.restore(bundle);
        Potion.restore(bundle);
        Wand.restore(bundle);
        Ring.restore(bundle);

        potionOfStrength = bundle.getInt(POS);
        scrollsOfUpgrade = bundle.getInt(SOU);
        scrollsOfEnchantment = bundle.getInt(SOE);
        dewVial = bundle.getBoolean(DV);
        transmutation = bundle.getInt(WT);

        dungeonType = bundle.getInt(DUNGEON_TYPE);

        if (fullLoad) {
            chapters = new HashSet<Integer>();
            int ids[] = bundle.getIntArray(CHAPTERS);
            if (ids != null) {
                for (int id : ids) {
                    chapters.add(id);
                }
            }

            Bundle quests = bundle.getBundle(QUESTS);
            if (!quests.isNull()) {
                Ghost.Quest.restoreFromBundle(quests);
                Wandmaker.Quest.restoreFromBundle(quests);
                Blacksmith.Quest.restoreFromBundle(quests);
                Imp.Quest.restoreFromBundle(quests);
                GoblinPirate.Quest.restoreFromBundle(quests);
            } else {
                Ghost.Quest.reset();
                Wandmaker.Quest.reset();
                Blacksmith.Quest.reset();
                Imp.Quest.reset();
                GoblinPirate.Quest.reset();
            }

            Room.restoreRoomsFromBundle(bundle);
        }

        Bundle badges = bundle.getBundle(BADGES);
        if (!badges.isNull()) {
            Badges.loadLocal(badges);
        } else {
            Badges.reset();
        }

        QuickSlot.restore(bundle);

        @SuppressWarnings("unused")
        String version = bundle.getString(VERSION);

        hero = null;
        hero = (Hero) bundle.get(HERO);

        QuickSlot.compress();

        gold = bundle.getInt(GOLD);
        depth = bundle.getInt(DEPTH);

        Statistics.restoreFromBundle(bundle);
        Journal.restoreFromBundle(bundle);

        droppedItems = new SparseArray<ArrayList<Item>>();
        for (int i = 2; i <= (Statistics.getDeepestFloor(dungeonType) + 1); i++) {
            ArrayList<Item> dropped = new ArrayList<Item>();
            for (Bundlable b : bundle.getCollection(String.format(DROPPED, i))) {
                dropped.add((Item) b);
            }
            if (!dropped.isEmpty()) {
                droppedItems.put(i, dropped);
            }
        }
    }

    public static Level loadLevel(final HeroClass cl) throws IOException {

        Dungeon.level = null;
        Actor.clear();

        InputStream input = Game.instance.openFileInput(Utils.format(Dungeon.depthFile(cl), depth));
        Bundle bundle = Bundle.read(input);
        input.close();

        return (Level) bundle.get("level");
    }

    private static Level newGoblinLevel() {
        Level level;
        switch (depth) {
        case 1:
        case 2:
        case 3:
        case 4:
            level = new GoblinSewerLevel();
            break;
        case 5:
            level = new GoblinSewerBossLevel();
            break;
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
            level = new LastLevel();
            break;
        default:
            level = new DeadEndLevel();
            Statistics.decDeepestFloor(dungeonType);
        }
        return level;
    }

    public static Level newLevel() {
        Dungeon.level = null;
        Actor.clear();

        depth++;
        if (depth > Statistics.getDeepestFloor(dungeonType)) {

            Statistics.setDeepestFloor(dungeonType, depth);

            if (Statistics.qualifiedForNoKilling) {
                Statistics.completedWithNoKilling = true;
            } else {
                Statistics.completedWithNoKilling = false;
            }
        }

        Arrays.fill(visible, false);

        Level level;
        // int dungeonType = DungeonType.YOG; // DEFAULT
        switch (dungeonType) {
        case DungeonType.YOG:
            level = Dungeon.newYogLevel();
            break;
        case DungeonType.GOBLIN:
            level = Dungeon.newGoblinLevel();
            break;
        case DungeonType.MAD_MAGE:
            level = Dungeon.newYogLevel(); // TODO replace with mad_mage
            break;
        default:
            // DEFAULT is YOG
            level = Dungeon.newYogLevel();
            break;
        }

        level.create();

        Statistics.qualifiedForNoKilling = !Dungeon.bossLevel();

        return level;
    }

    private static Level newYogLevel() {
        Level level;
        switch (depth) {
        case 1:
        case 2:
        case 3:
        case 4:
            level = new SewerLevel();
            break;
        case 5:
            level = new SewerBossLevel();
            break;
        case 6:
        case 7:
        case 8:
        case 9:
            level = new PrisonLevel();
            break;
        case 10:
            level = new PrisonBossLevel();
            break;
        case 11:
        case 12:
        case 13:
        case 14:
            level = new CavesLevel();
            break;
        case 15:
            level = new CavesBossLevel();
            break;
        case 16:
        case 17:
        case 18:
        case 19:
            level = new CityLevel();
            break;
        case 20:
            level = new CityBossLevel();
            break;
        case 21:
            level = new LastShopLevel();
            break;
        case 22:
        case 23:
        case 24:
            level = new HallsLevel();
            break;
        case 25:
            level = new HallsBossLevel();
            break;
        case 26:
            level = new LastLevel();
            break;
        default:
            level = new DeadEndLevel();
            Statistics.decDeepestFloor(dungeonType);
        }
        return level;
    }

    public static void observe() {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero);
        System.arraycopy(Level.fieldOfView, 0, visible, 0, visible.length);

        BArray.or(level.visited, visible, level.visited);

        GameScene.afterObserve();
    }

    public static boolean posNeeded() {
        int[] quota = { 4, 2, 9, 4, 14, 6, 19, 8, 24, 9 };
        return Dungeon.chance(quota, potionOfStrength);
    }

    public static void preview(final GamesInProgress.Info info, final Bundle bundle) {
        info.depth = bundle.getInt(DEPTH);
        info.challenges = (bundle.getInt(CHALLENGES) != 0);
        if (info.depth == -1) {
            info.depth = bundle.getInt("maxDepth"); // FIXME
        }
        Hero.preview(info, bundle.getBundle(HERO));
    }

    public static void resetLevel() {

        Actor.clear();

        Arrays.fill(visible, false);

        level.reset();
        Dungeon.switchLevel(level, level.entrance);
    }

    public static void saveAll() throws IOException {
        if (hero.isAlive()) {

            Actor.fixTime();
            Dungeon.saveGame(Dungeon.gameFile(hero.heroClass));
            Dungeon.saveLevel();

            GamesInProgress.set(hero.heroClass, depth, hero.lvl, challenges != 0);

        } else if (WndResurrect.instance != null) {

            WndResurrect.instance.hide();
            Hero.reallyDie(WndResurrect.causeOfDeath);

        }
    }

    public static void saveGame(final String fileName) throws IOException {
        try {
            Bundle bundle = new Bundle();

            bundle.put(VERSION, Game.version);
            bundle.put(CHALLENGES, challenges);
            bundle.put(HERO, hero);
            bundle.put(GOLD, gold);
            bundle.put(DEPTH, depth);
            bundle.put(DUNGEON_TYPE, dungeonType);

            for (int d : droppedItems.keyArray()) {
                bundle.put(String.format(DROPPED, d), droppedItems.get(d));
            }

            bundle.put(POS, potionOfStrength);
            bundle.put(SOU, scrollsOfUpgrade);
            bundle.put(SOE, scrollsOfEnchantment);
            bundle.put(DV, dewVial);
            bundle.put(WT, transmutation);

            int count = 0;
            int ids[] = new int[chapters.size()];
            for (Integer id : chapters) {
                ids[count++] = id;
            }
            bundle.put(CHAPTERS, ids);

            Bundle quests = new Bundle();
            Ghost.Quest.storeInBundle(quests);
            Wandmaker.Quest.storeInBundle(quests);
            Blacksmith.Quest.storeInBundle(quests);
            Imp.Quest.storeInBundle(quests);
            GoblinPirate.Quest.storeInBundle(quests);
            bundle.put(QUESTS, quests);

            Room.storeRoomsInBundle(bundle);

            Statistics.storeInBundle(bundle);
            Journal.storeInBundle(bundle);

            QuickSlot.save(bundle);

            Scroll.save(bundle);
            Potion.save(bundle);
            Wand.save(bundle);
            Ring.save(bundle);

            Bundle badges = new Bundle();
            Badges.saveLocal(badges);
            bundle.put(BADGES, badges);

            OutputStream output = Game.instance.openFileOutput(fileName, Context.MODE_PRIVATE);
            Bundle.write(bundle, output);
            output.close();

        } catch (Exception e) {

            GamesInProgress.setUnknown(hero.heroClass);
        }
    }

    public static void saveLevel() throws IOException {
        Bundle bundle = new Bundle();
        bundle.put(LEVEL, level);

        OutputStream output = Game.instance.openFileOutput(Utils.format(Dungeon.depthFile(hero.heroClass), depth),
                Context.MODE_PRIVATE);
        Bundle.write(bundle, output);
        output.close();
    }

    public static boolean shopOnLevel() {
        // TODO add dungeon type too
        return (depth == 6) || (depth == 11) || (depth == 16);
    }

    public static boolean soeNeeded() {
        return Random.Int(12 * (1 + scrollsOfEnchantment)) < depth;
    }

    public static boolean souNeeded() {
        int[] quota = { 5, 3, 10, 6, 15, 9, 20, 12, 25, 13 };
        return Dungeon.chance(quota, scrollsOfUpgrade);
    }

    @SuppressWarnings("deprecation")
    public static void switchLevel(final Level level, final int pos) {

        nightMode = new Date().getHours() < 7;

        Dungeon.level = level;
        Actor.init();

        Actor respawner = level.respawner();
        if (respawner != null) {
            Actor.add(level.respawner());
        }

        hero.pos = pos != -1 ? pos : level.exit;

        Light light = hero.buff(Light.class);
        hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

        Dungeon.observe();
    }

    public static void win(final String desc) {

        hero.belongings.identify();

        if (challenges != 0) {
            Badges.validateChampion();
        }

        resultDescription = desc;
        Rankings.INSTANCE.submit(true);
    }

}
