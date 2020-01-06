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
package com.watabou.pixeldungeon.actors.hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.GamesInProgress;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Barkskin;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Bless;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Combo;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Drunk;
import com.watabou.pixeldungeon.actors.buffs.Fear;
import com.watabou.pixeldungeon.actors.buffs.Fury;
import com.watabou.pixeldungeon.actors.buffs.GasesImmunity;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.buffs.Ooze;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Regeneration;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.SnipersMark;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.MobType;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.effects.CheckedCell;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Amulet;
import com.watabou.pixeldungeon.items.Ankh;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Heap.Type;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.keys.GoldenKey;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.items.keys.Key;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.potions.PotionOfMight;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.RingOfAccuracy;
import com.watabou.pixeldungeon.items.rings.RingOfDetection;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.items.rings.RingOfEvasion;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.levels.features.AlchemyPot;
import com.watabou.pixeldungeon.levels.features.Chasm;
import com.watabou.pixeldungeon.levels.features.Sign;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.SurfaceScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.ui.AttackIndicator;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndMessage;
import com.watabou.pixeldungeon.windows.WndResurrect;
import com.watabou.pixeldungeon.windows.WndTradeItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hero extends Char {

  public static interface Doom {
    public void onDeath();
  }

  private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";

  private static final String TXT_LEVEL_UP = "level up!";

  private static final String TXT_NEW_LEVEL =
      "Welcome to level %d! Now you are healthier and more focused. " +
          "It's easier for you to hit enemies and dodge their attacks.";

  public static final String TXT_YOU_NOW_HAVE = "You now have %s";
  private static final String TXT_SOMETHING_ELSE = "There is something else here";
  private static final String TXT_LOCKED_CHEST =
      "This chest is locked and you don't have matching key";
  private static final String TXT_LOCKED_DOOR = "You don't have a matching key";

  private static final String TXT_NOTICED_SMTH = "You noticed something";
  private static final String TXT_WAIT = "...";

  private static final String TXT_SEARCH = "search";

  public static final int STARTING_STR = 10;
  private static final float TIME_TO_REST = 1f;

  private static final float TIME_TO_SEARCH = 2f;
  private static final String ATTACK = "attackSkill";

  private static final String DEFENSE = "defenseSkill";
  private static final String STRENGTH = "STR";

  private static final String LEVEL = "lvl";

  private static final String EXPERIENCE = "exp";
  private static final float BLESS_BUFF_BONUS = 1.4f;

  public static void preview(final GamesInProgress.Info info, final Bundle bundle) {
    info.level = bundle.getInt(LEVEL);
  }

  public static void reallyDie(final Object cause) {

    int length = Level.LENGTH;
    int[] map = Dungeon.level.map;
    boolean[] visited = Dungeon.level.visited;
    boolean[] discoverable = Level.discoverable;

    for (int i = 0; i < length; i++) {

      int terr = map[i];

      if (discoverable[i]) {

        visited[i] = true;
        if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
          Level.set(i, Terrain.discover(terr));
          GameScene.updateMap(i);
        }
      }
    }

    Bones.leave();

    Dungeon.observe();

    Dungeon.hero.belongings.identify();

    int pos = Dungeon.hero.pos;

    ArrayList<Integer> passable = new ArrayList<Integer>();
    for (Integer ofs : Level.NEIGHBOURS8) {
      int cell = pos + ofs;
      if ((Level.passable[cell] || Level.avoid[cell]) && (Dungeon.level.heaps.get(cell) == null)) {
        passable.add(cell);
      }
    }
    Collections.shuffle(passable);

    ArrayList<Item> items = new ArrayList<Item>(Dungeon.hero.belongings.backpack.items);
    for (Integer cell : passable) {
      if (items.isEmpty()) {
        break;
      }

      Item item = Random.element(items);
      Dungeon.level.drop(item, cell).sprite.drop(pos);
      items.remove(item);
    }

    GameScene.gameOver();

    if (cause instanceof Hero.Doom) {
      ((Hero.Doom) cause).onDeath();
    }

    Dungeon.deleteGame(Dungeon.hero.heroClass, true);
  }

  public HeroClass heroClass = HeroClass.ROGUE;

  public HeroSubClass subClass = HeroSubClass.NONE;

  private int attackSkill = 10;
  private int defenseSkill = 5;

  public boolean ready = false;
  public HeroAction curAction = null;

  public HeroAction lastAction = null;

  private Char enemy;
  public Armor.Glyph killerGlyph = null;

  private Item theKey;

  public boolean restoreHealth = false;

  public MissileWeapon rangedWeapon = null;

  public Belongings belongings;
  public int STR;
  public boolean weakened = false;
  public float awareness;
  public int lvl = 1;

  public int exp = 0;

  private ArrayList<Mob> visibleEnemies;

  public Hero() {
    super();
    name = "you";

    HP = HT = 20;
    STR = STARTING_STR;
    awareness = 0.1f;

    belongings = new Belongings(this);

    visibleEnemies = new ArrayList<Mob>();
  }

  @Override
  public boolean act() {

    super.act();

    if (paralysed) {

      curAction = null;

      spendAndNext(TICK);
      return false;
    }

    checkVisibleMobs();
    AttackIndicator.updateState();

    if (curAction == null) {

      if (restoreHealth) {
        if (isStarving() || (HP >= HT)) {
          restoreHealth = false;
        } else {
          spend(TIME_TO_REST);
          next();
          return false;
        }
      }

      ready();
      return false;

    } else {

      restoreHealth = false;

      ready = false;
      Fear fear = buff(Fear.class);
      if ((fear != null) && !(curAction instanceof HeroAction.Move)) {
        int calculateRunAway = calculateRunAway(fear);
        curAction = new HeroAction.Move(calculateRunAway);
      }

      if (curAction instanceof HeroAction.Move) {

        return actMove((HeroAction.Move) curAction);

      } else if (curAction instanceof HeroAction.Interact) {

        return actInteract((HeroAction.Interact) curAction);

      } else if (curAction instanceof HeroAction.Buy) {

        return actBuy((HeroAction.Buy) curAction);

      } else if (curAction instanceof HeroAction.PickUp) {

        return actPickUp((HeroAction.PickUp) curAction);

      } else if (curAction instanceof HeroAction.OpenChest) {

        return actOpenChest((HeroAction.OpenChest) curAction);

      } else if (curAction instanceof HeroAction.Unlock) {

        return actUnlock((HeroAction.Unlock) curAction);

      } else if (curAction instanceof HeroAction.Descend) {

        return actDescend((HeroAction.Descend) curAction);

      } else if (curAction instanceof HeroAction.Ascend) {

        return actAscend((HeroAction.Ascend) curAction);

      } else if (curAction instanceof HeroAction.Attack) {

        return actAttack((HeroAction.Attack) curAction);

      } else if (curAction instanceof HeroAction.Cook) {

        return actCook((HeroAction.Cook) curAction);

      }
    }

    return false;
  }

  private boolean actAscend(final HeroAction.Ascend action) {
    int stairs = action.dst;
    if ((pos == stairs) && (pos == Dungeon.level.entrance)) {

      if (Dungeon.depth == 1) {

        if (belongings.getItem(Amulet.class) == null) {
          GameScene.show(new WndMessage(TXT_LEAVE));
          ready();
        } else {
          Dungeon.win(ResultDescriptions.WIN);
          Dungeon.deleteGame(Dungeon.hero.heroClass, true);
          Game.switchScene(SurfaceScene.class);
        }

      } else {

        curAction = null;

        Hunger hunger = buff(Hunger.class);
        if ((hunger != null) && !hunger.isStarving()) {
          hunger.satisfy(-Hunger.STARVING / 10);
        }

        InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
        Game.switchScene(InterlevelScene.class);
      }

      return false;

    } else if (getCloser(stairs)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actAttack(final HeroAction.Attack action) {

    enemy = action.target;

    // TODO add fear buff check too! or not..

    if (Level.adjacent(pos, enemy.pos) && enemy.isAlive() && !isCharmedBy(enemy)) {

      spend(attackDelay());
      sprite.attack(enemy.pos);

      return false;

    } else {

      if (Level.fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

        return true;

      } else {
        ready();
        return false;
      }

    }
  }

  private boolean actBuy(final HeroAction.Buy action) {
    int dst = action.dst;
    if ((pos == dst) || Level.adjacent(pos, dst)) {

      ready();

      Heap heap = Dungeon.level.heaps.get(dst);
      if ((heap != null) && (heap.type == Type.FOR_SALE) && (heap.size() == 1)) {
        GameScene.show(new WndTradeItem(heap, true));
      }

      return false;

    } else if (getCloser(dst)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actCook(final HeroAction.Cook action) {
    int dst = action.dst;
    if (Dungeon.visible[dst]) {

      ready();
      AlchemyPot.operate(this, dst);
      return false;

    } else if (getCloser(dst)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actDescend(final HeroAction.Descend action) {
    int stairs = action.dst;
    if ((pos == stairs) && (pos == Dungeon.level.exit)) {

      curAction = null;

      Hunger hunger = buff(Hunger.class);
      if ((hunger != null) && !hunger.isStarving()) {
        hunger.satisfy(-Hunger.STARVING / 10);
      }

      InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
      Game.switchScene(InterlevelScene.class);

      return false;

    } else if (getCloser(stairs)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actInteract(final HeroAction.Interact action) {

    NPC npc = action.npc;

    if (Level.adjacent(pos, npc.pos)) {

      ready();
      sprite.turnTo(pos, npc.pos);
      npc.interact();
      return false;

    } else {

      if (Level.fieldOfView[npc.pos] && getCloser(npc.pos)) {

        return true;

      } else {
        ready();
        return false;
      }

    }
  }

  private boolean actMove(final HeroAction.Move action) {

    if (getCloser(action.dst)) {

      return true;

    } else {
      if (Dungeon.level.map[pos] == Terrain.SIGN) {
        Sign.read(pos);
      }
      ready();

      return false;
    }
  }

  private boolean actOpenChest(final HeroAction.OpenChest action) {
    int dst = action.dst;
    if (Level.adjacent(pos, dst) || (pos == dst)) {

      Heap heap = Dungeon.level.heaps.get(dst);
      if ((heap != null) && ((heap.type != Type.HEAP) && (heap.type != Type.FOR_SALE))) {

        theKey = null;

        if ((heap.type == Type.LOCKED_CHEST) || (heap.type == Type.CRYSTAL_CHEST)) {

          theKey = belongings.getKey(GoldenKey.class, Dungeon.depth);

          if (theKey == null) {
            GLog.w(TXT_LOCKED_CHEST);
            ready();
            return false;
          }
        }

        switch (heap.type) {
          case TOMB:
            Sample.INSTANCE.play(Assets.SND_TOMB);
            Camera.main.shake(1, 0.5f);
            break;
          case SKELETON:
            break;
          default:
            Sample.INSTANCE.play(Assets.SND_UNLOCK);
        }

        spend(Key.TIME_TO_UNLOCK);
        sprite.operate(dst);

      } else {
        ready();
      }

      return false;

    } else if (getCloser(dst)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actPickUp(final HeroAction.PickUp action) {
    int dst = action.dst;
    if (pos == dst) {

      Heap heap = Dungeon.level.heaps.get(pos);
      if (heap != null) {
        Item item = heap.pickUp();
        if (item.doPickUp(this)) {

          if (item instanceof Dewdrop) {
            // Do nothing
          } else {
            boolean important =
                (((item instanceof ScrollOfUpgrade) || (item instanceof ScrollOfEnchantment))
                    && ((Scroll) item)
                        .isKnown())
                    ||
                    (((item instanceof PotionOfStrength) || (item instanceof PotionOfMight))
                        && ((Potion) item)
                            .isKnown());
            if (important) {
              GLog.p(TXT_YOU_NOW_HAVE, item.name());
            } else {
              GLog.i(TXT_YOU_NOW_HAVE, item.name());
            }
          }

          if (!heap.isEmpty()) {
            GLog.i(TXT_SOMETHING_ELSE);
          }
          curAction = null;
        } else {
          Dungeon.level.drop(item, pos).sprite.drop();
          ready();
        }
      } else {
        ready();
      }

      return false;

    } else if (getCloser(dst)) {

      return true;

    } else {
      ready();
      return false;
    }
  }

  private boolean actUnlock(final HeroAction.Unlock action) {
    int doorCell = action.dst;
    if (Level.adjacent(pos, doorCell)) {

      theKey = null;
      int door = Dungeon.level.map[doorCell];

      if (door == Terrain.LOCKED_DOOR) {

        theKey = belongings.getKey(IronKey.class, Dungeon.depth);

      } else if (door == Terrain.LOCKED_EXIT) {

        theKey = belongings.getKey(SkeletonKey.class, Dungeon.depth);

      }

      if (theKey != null) {

        spend(Key.TIME_TO_UNLOCK);
        sprite.operate(doorCell);

        Sample.INSTANCE.play(Assets.SND_UNLOCK);

      } else {
        GLog.w(TXT_LOCKED_DOOR);
        ready();
      }

      return false;

    } else if (getCloser(doorCell)) {

      return true;

    } else {
      ready();
      return false;
    }
  };

  @Override
  public void add(final Buff buff) {
    super.add(buff);

    if (sprite != null) {
      if (buff instanceof Burning) {
        GLog.w("You catch fire!");
        interrupt();
      } else if (buff instanceof Paralysis) {
        GLog.w("You are paralysed!");
        interrupt();
      } else if (buff instanceof Poison) {
        GLog.w("You are poisoned!");
        interrupt();
      } else if (buff instanceof Ooze) {
        GLog.w("Caustic ooze eats your flesh. Wash away it!");
      } else if (buff instanceof Roots) {
        GLog.w("You can't move!");
      } else if (buff instanceof Weakness) {
        GLog.w("You feel weakened!");
      } else if (buff instanceof Blindness) {
        GLog.w("You are blinded!");
      } else if (buff instanceof Fury) {
        GLog.w("You become furious!");
        sprite.showStatus(CharSprite.POSITIVE, "furious");
      } else if (buff instanceof Charm) {
        GLog.w("You are charmed!");
      } else if (buff instanceof Cripple) {
        GLog.w("You are crippled!");
      } else if (buff instanceof Bleeding) {
        GLog.w("You are bleeding!");
      } else if (buff instanceof Vertigo) {
        GLog.w("Everything is spinning around you!");
        interrupt();
      }

      else if (buff instanceof Light) {
        sprite.add(CharSprite.State.ILLUMINATED);
      }
    }

    BuffIndicator.refreshHero();
  }

  // @Override
  // public boolean attack(final Char enemy) {
  // Fear buffFear = buff(Fear.class);
  // if (buffFear != null) {
  // // TODO this is not good.
  // calculazeRunAway(buffFear);
  // return false;
  // }
  // return super.attack(enemy);
  // }

  public float attackDelay() {
    KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
    if (wep != null) {

      return wep.speedFactor(this);

    } else {
      return 1f;
    }
  }

  @Override
  public int attackProc(final Char enemy, int damage) {
    KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
    if (wep != null) {

      wep.proc(this, enemy, damage);
      if ((heroClass == HeroClass.PRIEST) && (((Mob) enemy).mobType == MobType.UNDEAD)) {
        if (wep instanceof MeleeWeapon) {
          float priestLvlModifier = 1 + (lvl / 3);
          damage += ((Math.pow(priestLvlModifier, 2) - priestLvlModifier) + 10) / 4;
        }
      }

      switch (subClass) {
        case GLADIATOR:
          if (wep instanceof MeleeWeapon) {
            damage += Buff.affect(this, Combo.class).hit(enemy, damage);
          }
          break;
        case PALADIN:
          if ((wep instanceof MeleeWeapon) && (((Mob) enemy).mobType == MobType.DEMON)) {
            float priestLvlModifier = 1 + (lvl / 3);
            damage += ((Math.pow(priestLvlModifier, 2) - priestLvlModifier) + 10) / 4;
          }
          break;
        case BATTLEMAGE:
          if (wep instanceof Wand) {
            Wand wand = (Wand) wep;
            if (wand.curCharges >= wand.maxCharges) {

              wand.use();

            } else if (damage > 0) {

              wand.curCharges++;
              wand.updateQuickslot();

              ScrollOfRecharging.charge(this);
            }
            damage += wand.curCharges;
          }
        case SNIPER:
          if (rangedWeapon != null) {
            Buff.prolong(this, SnipersMark.class, attackDelay() * 1.1f).object = enemy.id();
          }
          break;
        default:
      }
    }

    return damage;
  }

  @Override
  public int attackSkill(final Char target) {

    int bonus = 0;
    for (Buff buff : buffs(RingOfAccuracy.Accuracy.class)) {
      bonus += ((RingOfAccuracy.Accuracy) buff).level;
    }
    float accuracy = (bonus == 0) ? 1 : (float) Math.pow(1.4, bonus);
    if (target instanceof Mob) {
      if ((heroClass == HeroClass.PRIEST) && (((Mob) target).mobType == MobType.UNDEAD)) {
        accuracy += 0.5f; // TODO nerf? Math.sqrt(lvl) -
      }
      if ((subClass == HeroSubClass.PALADIN) && (((Mob) target).mobType == MobType.DEMON)) {
        accuracy += 0.5f; // TODO nerf? Math.sqrt(lvl) -
      }
    }
    if (buff(Bless.class) != null) {
      accuracy *= BLESS_BUFF_BONUS;
    }

    if ((rangedWeapon != null) && (Level.distance(pos, target.pos) == 1)) {
      accuracy *= 0.5f;
    }
    // DRUNK
    for (Buff buff : buffs(Drunk.class)) {
      accuracy = (accuracy <= 1) ? accuracy - 0.3f : accuracy - 1;
    }
    KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
    if (wep != null) {
      return (int) (attackSkill * accuracy * wep.acuracyFactor(this));
    } else {
      return (int) (attackSkill * accuracy);
    }
  }

  public void busy() {
    ready = false;
  }

  private int calculateRunAway(final Fear buffFear) {
    int runAwayStep = Dungeon.flee(this, pos, buffFear.theFearfulEnemy.pos,
        Level.passable,
        Level.fieldOfView);
    if (runAwayStep != -1) {
      return runAwayStep;
      // super.move(runAwayStep);
    } else {
      spend(TICK);
      sprite.showStatus(CharSprite.NEGATIVE, Fear.NO_WAY_TO_RUN);
      // TODO this is wrong i gues
      return 0;
    }
  }

  private void checkVisibleMobs() {
    ArrayList<Mob> visible = new ArrayList<Mob>();

    boolean newMob = false;

    for (Mob m : Dungeon.level.mobs) {
      if (Level.fieldOfView[m.pos] && m.hostile) {
        visible.add(m);
        if (!visibleEnemies.contains(m)) {
          newMob = true;
        }
      }
    }

    if (newMob) {
      interrupt();
      restoreHealth = false;
    }

    visibleEnemies = visible;
  }

  public String className() {
    return (subClass == null) || (subClass == HeroSubClass.NONE) ? heroClass.title()
        : subClass.title();
  }

  @Override
  public void damage(final int dmg, final Object src) {
    restoreHealth = false;
    super.damage(dmg, src);

    if ((subClass == HeroSubClass.BERSERKER) && (0 < HP) && (HP <= (HT * Fury.LEVEL))) {
      Buff.affect(this, Fury.class);
    }
  }

  @Override
  public int damageRoll() {
    KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
    int dmg;
    if (wep != null) {
      dmg = wep.damageRoll(this);
    } else {
      dmg = STR() > 10 ? Random.IntRange(1, STR() - 9) : 1;
    }
    return buff(Fury.class) != null ? (int) (dmg * 1.5f) : dmg;
  }

  @Override
  public int defenseSkill(final Char enemy) {
    int bonus = 0;
    for (Buff buff : buffs(RingOfEvasion.Evasion.class)) {
      bonus += ((RingOfEvasion.Evasion) buff).level;
    }
    float evasion = bonus == 0 ? 1 : (float) Math.pow(1.2, bonus);

    if ((heroClass == HeroClass.PRIEST) && (((Mob) enemy).mobType == MobType.UNDEAD)) {
      evasion += 0.5f; // TODO nerf? Math.sqrt(lvl) -
    }
    if ((subClass == HeroSubClass.PALADIN) && (((Mob) enemy).mobType == MobType.DEMON)) {
      evasion += 0.5f; // TODO nerf? Math.sqrt(lvl) -
    }

    if (buff(Bless.class) != null) {
      evasion *= BLESS_BUFF_BONUS;
    }

    // DRUNK
    for (Buff buff : buffs(Drunk.class)) {
      evasion = (evasion <= 1) ? evasion - 0.3f : evasion - 1;
    }
    if (paralysed) {
      evasion /= 2;
    }

    int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
    // TODO sure about this FIXME i think the PALADIN bonus need to be refactored
    if (subClass == HeroSubClass.PALADIN) {
      aEnc--;
    }

    if (aEnc > 0) {
      return (int) ((defenseSkill * evasion) / Math.pow(1.5, aEnc));
    } else {

      if (heroClass == HeroClass.ROGUE) {

        if ((curAction != null) && (subClass == HeroSubClass.FREERUNNER) && !isStarving()) {
          evasion *= 2;
        }

        return (int) ((defenseSkill - aEnc) * evasion);
      } else {
        return (int) (defenseSkill * evasion);
      }
    }
  }

  @Override
  public void die(final Object cause) {

    curAction = null;

    DewVial.autoDrink(this);
    if (isAlive()) {
      new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
      return;
    }

    Actor.fixTime();
    super.die(cause);

    Ankh ankh = belongings.getItem(Ankh.class);
    if (ankh == null) {

      reallyDie(cause);

    } else {

      Dungeon.deleteGame(Dungeon.hero.heroClass, false);
      GameScene.show(new WndResurrect(ankh, cause));

    }
  }

  @Override
  public int dr() {
    int dr = belongings.armor != null ? Math.max(belongings.armor.DR(), 0) : 0;
    Barkskin barkskin = buff(Barkskin.class);
    if (barkskin != null) {
      dr += barkskin.level();
    }
    return dr;
  }

  public void earnExp(final int exp) {

    this.exp += exp;

    boolean levelUp = false;
    while (this.exp >= maxExp()) {
      this.exp -= maxExp();
      lvl++;

      HT += 5;
      HP += 5;
      attackSkill++;
      defenseSkill++;

      if (lvl < 10) {
        updateAwareness();
      }

      levelUp = true;
    }

    if (levelUp) {

      GLog.p(TXT_NEW_LEVEL, lvl);
      sprite.showStatus(CharSprite.POSITIVE, TXT_LEVEL_UP);
      Sample.INSTANCE.play(Assets.SND_LEVELUP);

      Badges.validateLevelReached();
    }

    if (subClass == HeroSubClass.WARLOCK) {

      int value = Math.min(HT - HP, 1 + ((Dungeon.depth - 1) / 5));
      if (value > 0) {
        HP += value;
        sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
      }

      buff(Hunger.class).satisfy(10);
    }
  }

  private boolean getCloser(final int target) {

    if (rooted) {
      Camera.main.shake(1, 1f);
      return false;
    }

    int step = -1;

    if (Level.adjacent(pos, target)) {

      if (Actor.findChar(target) == null) {
        if (Level.pit[target] && !flying && !Chasm.jumpConfirmed) {
          Chasm.heroJump(this);
          interrupt();
          return false;
        }
        if (Level.passable[target] || Level.avoid[target]) {
          step = target;
        }
      }

    } else {

      int len = Level.LENGTH;
      boolean[] p = Level.passable;
      boolean[] v = Dungeon.level.visited;
      boolean[] m = Dungeon.level.mapped;
      boolean[] passable = new boolean[len];
      for (int i = 0; i < len; i++) {
        passable[i] = p[i] && (v[i] || m[i]);
      }

      step = Dungeon.findPath(this, pos, target, passable, Level.fieldOfView);
    }

    if (step != -1) {

      int oldPos = pos;
      move(step);
      sprite.move(oldPos, pos);
      spend(1 / speed());

      return true;

    } else {

      return false;

    }

  }

  public boolean handle(final int cell) {

    if (cell == -1) {
      return false;
    }

    Char ch;
    Heap heap;

    if ((Dungeon.level.map[cell] == Terrain.ALCHEMY) && (cell != pos)) {

      curAction = new HeroAction.Cook(cell);

    } else if (Level.fieldOfView[cell] && ((ch = Actor.findChar(cell)) instanceof Mob)) {

      if (ch instanceof NPC) {
        curAction = new HeroAction.Interact((NPC) ch);
      } else {
        curAction = new HeroAction.Attack(ch);
      }

    } else if (Level.fieldOfView[cell] && ((heap = Dungeon.level.heaps.get(cell)) != null)
        && (heap.type != Heap.Type.HIDDEN)) {

      switch (heap.type) {
        case HEAP:
          curAction = new HeroAction.PickUp(cell);
          break;
        case FOR_SALE:
          curAction = (heap.size() == 1) && (heap.peek().price() > 0) ? new HeroAction.Buy(cell)
              : new HeroAction.PickUp(cell);
          break;
        default:
          curAction = new HeroAction.OpenChest(cell);
      }

    } else if ((Dungeon.level.map[cell] == Terrain.LOCKED_DOOR)
        || (Dungeon.level.map[cell] == Terrain.LOCKED_EXIT)) {

      curAction = new HeroAction.Unlock(cell);

    } else if (cell == Dungeon.level.exit) {

      curAction = new HeroAction.Descend(cell);

    } else if (cell == Dungeon.level.entrance) {

      curAction = new HeroAction.Ascend(cell);

    } else {

      curAction = new HeroAction.Move(cell);
      lastAction = null;

    }

    return act();
  }

  @Override
  public HashSet<Class<?>> immunities() {
    GasesImmunity buff = buff(GasesImmunity.class);
    return buff == null ? super.immunities() : GasesImmunity.IMMUNITIES;
  }

  public void interrupt() {
    if (isAlive() && (curAction != null) && (curAction.dst != pos)) {
      lastAction = curAction;
    }
    curAction = null;
  }

  public boolean isStarving() {
    return buff(Hunger.class).isStarving();
  }

  public void live() {
    Buff.affect(this, Regeneration.class);
    Buff.affect(this, Hunger.class);
  }

  public int maxExp() {
    return 5 + (lvl * 5);
  }

  @Override
  public void move(final int step) {
    super.move(step);

    if (!flying) {

      if (Level.water[pos]) {
        Sample.INSTANCE.play(Assets.SND_WATER, 1, 1, Random.Float(0.8f, 1.25f));
      } else {
        Sample.INSTANCE.play(Assets.SND_STEP);
      }
      Dungeon.level.press(pos, this);
    }
  }

  @Override
  public void next() {
    super.next();
  }

  @Override
  public void onAttackComplete() {

    AttackIndicator.target(enemy);

    attack(enemy);
    curAction = null;

    Invisibility.dispel();

    super.onAttackComplete();
  }

  @Override
  public void onMotionComplete() {
    Dungeon.observe();
    search(false);

    super.onMotionComplete();
  }

  @Override
  public void onOperateComplete() {

    if (curAction instanceof HeroAction.Unlock) {

      if (theKey != null) {
        theKey.detach(belongings.backpack);
        theKey = null;
      }

      int doorCell = ((HeroAction.Unlock) curAction).dst;
      int door = Dungeon.level.map[doorCell];

      Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
      GameScene.updateMap(doorCell);

    } else if (curAction instanceof HeroAction.OpenChest) {

      if (theKey != null) {
        theKey.detach(belongings.backpack);
        theKey = null;
      }

      Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) curAction).dst);
      if (heap.type == Type.SKELETON) {
        Sample.INSTANCE.play(Assets.SND_BONES);
      }
      heap.open(this);
    }
    curAction = null;

    super.onOperateComplete();
  }

  private void ready() {
    sprite.idle();
    curAction = null;
    ready = true;

    GameScene.ready();
  }

  @Override
  public void remove(final Buff buff) {
    super.remove(buff);

    if (buff instanceof Light) {
      sprite.remove(CharSprite.State.ILLUMINATED);
    }

    BuffIndicator.refreshHero();
  }

  @Override
  public HashSet<Class<?>> resistances() {
    RingOfElements.Resistance r = buff(RingOfElements.Resistance.class);
    return r == null ? super.resistances() : r.resistances();
  }

  public void rest(final boolean tillHealthy) {
    spendAndNext(TIME_TO_REST);
    if (!tillHealthy) {
      sprite.showStatus(CharSprite.DEFAULT, TXT_WAIT);
    }
    restoreHealth = tillHealthy;
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    super.restoreFromBundle(bundle);

    heroClass = HeroClass.restoreInBundle(bundle);
    subClass = HeroSubClass.restoreInBundle(bundle);

    attackSkill = bundle.getInt(ATTACK);
    defenseSkill = bundle.getInt(DEFENSE);

    STR = bundle.getInt(STRENGTH);
    updateAwareness();

    lvl = bundle.getInt(LEVEL);
    exp = bundle.getInt(EXPERIENCE);

    belongings.restoreFromBundle(bundle);
  }

  public void resume() {
    curAction = lastAction;
    lastAction = null;
    act();
  }

  public void resurrect(final int resetLevel) {

    HP = HT;
    Dungeon.gold = 0;
    exp = 0;

    belongings.resurrect(resetLevel);

    live();
  }

  public boolean search(final boolean intentional) {

    boolean smthFound = false;

    int positive = 0;
    int negative = 0;
    for (Buff buff : buffs(RingOfDetection.Detection.class)) {
      int bonus = ((RingOfDetection.Detection) buff).level;
      if (bonus > positive) {
        positive = bonus;
      } else if (bonus < 0) {
        negative += bonus;
      }
    }
    int distance = 1 + positive + negative;

    float level = intentional ? ((2 * awareness) - (awareness * awareness)) : awareness;
    if (distance <= 0) {
      level /= 2 - distance;
      distance = 1;
    }

    int cx = pos % Level.WIDTH;
    int cy = pos / Level.WIDTH;
    int ax = cx - distance;
    if (ax < 0) {
      ax = 0;
    }
    int bx = cx + distance;
    if (bx >= Level.WIDTH) {
      bx = Level.WIDTH - 1;
    }
    int ay = cy - distance;
    if (ay < 0) {
      ay = 0;
    }
    int by = cy + distance;
    if (by >= Level.HEIGHT) {
      by = Level.HEIGHT - 1;
    }

    for (int y = ay; y <= by; y++) {
      for (int x = ax, p = ax + (y * Level.WIDTH); x <= bx; x++, p++) {

        if (Dungeon.visible[p]) {

          if (intentional) {
            sprite.parent.addToBack(new CheckedCell(p));
          }

          if (Level.secret[p] && (intentional || (Random.Float() < level))) {

            int oldValue = Dungeon.level.map[p];

            GameScene.discoverTile(p, oldValue);

            Level.set(p, Terrain.discover(oldValue));

            GameScene.updateMap(p);

            ScrollOfMagicMapping.discover(p);

            smthFound = true;
          }

          if (intentional) {
            Heap heap = Dungeon.level.heaps.get(p);
            if ((heap != null) && (heap.type == Type.HIDDEN)) {
              heap.open(this);
              smthFound = true;
            }
          }
        }
      }
    }

    if (intentional) {
      sprite.showStatus(CharSprite.DEFAULT, TXT_SEARCH);
      sprite.operate(pos);
      if (smthFound) {
        spendAndNext(Random.Float() < level ? TIME_TO_SEARCH : TIME_TO_SEARCH * 2);
      } else {
        spendAndNext(TIME_TO_SEARCH);
      }

    }

    if (smthFound) {
      GLog.w(TXT_NOTICED_SMTH);
      Sample.INSTANCE.play(Assets.SND_SECRET);
      interrupt();
    }

    return smthFound;
  }

  public boolean shoot(final Char enemy, final MissileWeapon wep) {

    rangedWeapon = wep;
    boolean result = attack(enemy);
    rangedWeapon = null;

    return result;
  }

  @Override
  public float speed() {

    int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
    if (aEnc > 0) {

      return (float) (super.speed() * Math.pow(1.3, -aEnc));

    } else {

      float speed = super.speed();
      return ((HeroSprite) sprite).sprint((subClass == HeroSubClass.FREERUNNER) && !isStarving())
          ? 1.6f * speed
          : speed;

    }
  }

  @Override
  public void spend(final float time) {
    int hasteLevel = 0;
    for (Buff buff : buffs(RingOfHaste.Haste.class)) {
      hasteLevel += ((RingOfHaste.Haste) buff).level;
    }
    super.spend(hasteLevel == 0 ? time : (float) (time * Math.pow(1.1, -hasteLevel)));
  }

  public void spendAndNext(final float time) {
    busy();
    spend(time);
    next();
  }

  @Override
  public int stealth() {
    int stealth = super.stealth();
    for (Buff buff : buffs(RingOfShadows.Shadows.class)) {
      stealth += ((RingOfShadows.Shadows) buff).level;
    }
    return stealth;
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    super.storeInBundle(bundle);

    heroClass.storeInBundle(bundle);
    subClass.storeInBundle(bundle);

    bundle.put(ATTACK, attackSkill);
    bundle.put(DEFENSE, defenseSkill);

    bundle.put(STRENGTH, STR);

    bundle.put(LEVEL, lvl);
    bundle.put(EXPERIENCE, exp);

    belongings.storeInBundle(bundle);
  }

  public int STR() {
    return weakened ? STR - 2 : STR;
  }

  public int tier() {
    return belongings.armor == null ? 0 : belongings.armor.tier;
  }

  void updateAwareness() {
    awareness = (float) (1 - Math.pow(
        (heroClass == HeroClass.ROGUE ? 0.85 : 0.90),
        (1 + Math.min(lvl, 9)) * 0.5));
  }

  public int visibleEnemies() {
    return visibleEnemies.size();
  }

  public Mob visibleEnemy(final int index) {
    return visibleEnemies.get(index % visibleEnemies.size());
  }
}