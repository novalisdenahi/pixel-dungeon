/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Toth Daniel
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

import java.util.Collection;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.Rum;
import com.watabou.pixeldungeon.items.rings.RingOfAccuracy;
import com.watabou.pixeldungeon.items.rings.RingOfEvasion;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfSatiety;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Falchion;
import com.watabou.pixeldungeon.items.weapon.melee.Mace;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.melee.Rapier;
import com.watabou.pixeldungeon.items.weapon.melee.Sword;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Room.Type;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GoblinPirateSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndGoblinPirate;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GoblinPirate extends NPC {

  public static class Quest {

    private static boolean spawned;

    private static int counter = 0;

    private static final int RUM_NECCESARY = 3;

    private static boolean given;

    private static boolean completed;

    private static final String NODE = "pirate";

    private static final String SPAWNED = "spawned";

    private static final String COUNTER = "counter";
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
      Journal.remove(Journal.Feature.PIRATE);
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
        counter = node.getInt(COUNTER);
        completed = node.getBoolean(COMPLITED);

        item1 = (Item) node.get(ITEM1);
        item2 = (Item) node.get(ITEM2);
      } else {
        Quest.reset();
      }
    }

    public static void spawn(final Collection<Room> rooms) {
     // if (!spawned && (Dungeon.depth == 6)) {
      //TODO this is test
      if (!spawned && (Dungeon.depth == 2)) {
        Room goblinPirate = null;
        for (Room r : rooms) {
          if ((r.type == Type.STANDARD) && (r.width() > 4) && (r.height() > 4)) {
            goblinPirate = r;
            goblinPirate.type = Type.GOBLIN_PIRATE;

            spawned = true;

            given = false;
            completed = false;

            switch (Random.Int(5)) {
              case 0:
                item1 = new RingOfAccuracy();
                break;
              case 1:
                item1 = new RingOfEvasion();
                break;
              case 2:
                item1 = new RingOfHaste();
                break;
              case 3:
                item1 = new RingOfSatiety();
                break;
              case 4:
                item1 = new RingOfShadows();
                break;
            }
            item1.upgrade(Random.Int(2));

            switch (Random.Int(5)) {
              case 0:
                item2 = new Dagger();
                break;
              case 1:
                item2 = new Sword();
                break;
              case 2:
                item2 = new Mace();
                break;
              case 3:
                item2 = new Rapier();
                break;
              case 4:
                item2 = new Falchion();
                break;
            }
            item2.upgrade(Random.Int(2, 4));

            // lucky day bonus - maybe the random shuold be 365 :)
            if (Random.Int(10) == 0) {
              item1.upgrade(2);
              ((MeleeWeapon) (item2)).enchant();
            }
            break;
          }
        }
      }
    }

    public static void storeInBundle(final Bundle bundle) {

      Bundle node = new Bundle();

      node.put(SPAWNED, spawned);

      if (spawned) {

        node.put(COUNTER, counter);
        node.put(GIVEN, given);
        node.put(COMPLITED, completed);

        node.put(ITEM1, item1);
        node.put(ITEM2, item2);
      }

      bundle.put(NODE, node);
    }
  }

  private static final String TXT_RUM =
      "Yo Ho Ho! Come closer and don't be afraid. Yeah! I'm a Pirate Capt'n and a Goblin, but not your enemy adventurer. "
          + " My bloody crew sailed out without me! Arrgh! Maybe I was drunk.... a bit. I never said am the perfect Capt'n. "
          + "Therefore they will walk the plank! "
          + "Maybe, we could help each other. Aye! You just have to bring me " + Quest.RUM_NECCESARY
          + " bottles of _Goblin Rum_. I will pay the price. Just hurry! This old bones of mine are really thirsty. ";
  private static final String TXT_RUM_1 =
      "Aaarrgghh! Matey it's not enough! It will be enough until you bring more.";

  private static final String TXT_RUM_2 =
      "What are you doing here without my _Rum_?! I'll feed you the fish if you just play with me!";

  private static final String TXT_RUM_3 =
      "I need more _Rum_ or no business! Don't come here empty handed you land lubber!";

  private static final String TXT_RUM_4 =
      "Aaarrgghh! *hic* Yo Ho *hic-hic* Ho Sailor!";

  private static final String TXT_RUM_5 =
      "The King and his men stole the queen from *hic* her bed\n and bound her in her bones\n "
          + "The seas be ours and by the powers\n Where we will...we'll roam *hic* \n"
          + "Yo, Ho haul together, hoist the colours high\n Heave ho, *hic* thieves and beggars, never shall we die! *hic* ";

  {
    name = "Goblin Pirate";
    spriteClass = GoblinPirateSprite.class;
  }

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
    return "blocked";
  }

  @Override
  public String description() {
    return "It's a goblin. It's a pirate. He is a Goblin Pirate!. You can talk to him, "
        + "it's worth a shot. Worst case? You run away, he will never catch you with his wooden leg.";
  }

  @Override
  public void interact() {

    sprite.turnTo(pos, Dungeon.hero.pos);
    if (Quest.completed) {
      if (Random.Int(3) == 0) {
        tell(TXT_RUM_5);
      } else {
        tell(TXT_RUM_4);
      }
    } else if (Quest.given) {

      Item item = Dungeon.hero.belongings.getItem(Rum.class);

      if (item != null) {

        int detachAmount = getDetachNumber(item.quantity());
        while (detachAmount > 0) {
          item.detach(Dungeon.hero.belongings.backpack);
          Quest.counter++;
          detachAmount--;
        }
        if (Quest.counter >= Quest.RUM_NECCESARY) {

          GameScene.show(new WndGoblinPirate(this, item));
        } else {
          tell(TXT_RUM_1);
        }
      } else {
        if (Quest.counter == 0) {
          tell(TXT_RUM_2);
        } else {
          tell(TXT_RUM_3);
        }
      }

    } else {
      tell(TXT_RUM);
      Quest.given = true;

      Journal.add(Journal.Feature.PIRATE);
    }
  }

  private final int getDetachNumber(final int quantity){
    int stillNeed = Quest.RUM_NECCESARY - Quest.counter;
    return Math.min(quantity, stillNeed);
  }

  @Override
  public boolean reset() {
    return true;
  }

  private void tell(final String format, final Object... args) {
    GameScene.show(new WndQuest(this, Utils.format(format, args)));
  }
}
