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
package com.watabou.pixeldungeon.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.SnipersMark;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Degradation;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.MissileSprite;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class Item implements Bundlable {

  private static final String TXT_PACK_FULL = "Your pack is too full for the %s";

  private static final String TXT_BROKEN = "Because of frequent use, your %s has broken.";
  private static final String TXT_GONNA_BREAK =
      "Because of frequent use, your %s is going to break soon.";

  private static final String TXT_TO_STRING = "%s";
  private static final String TXT_TO_STRING_X = "%s x%d";
  private static final String TXT_TO_STRING_LVL = "%s%+d";
  private static final String TXT_TO_STRING_LVL_X = "%s%+d x%d";

  private static final float DURABILITY_WARNING_LEVEL = 1 / 6f;

  protected static final float TIME_TO_THROW = 1.0f;
  protected static final float TIME_TO_PICK_UP = 1.0f;
  protected static final float TIME_TO_DROP = 0.5f;

  public static final String AC_DROP = "DROP";
  public static final String AC_THROW = "THROW";

  private static Comparator<Item> itemComparator = new Comparator<Item>() {
    @Override
    public int compare(final Item lhs, final Item rhs) {
      return Generator.Category.order(lhs) - Generator.Category.order(rhs);
    }
  };

  private static final String QUANTITY = "quantity";
  private static final String LEVEL = "level";

  private static final String LEVEL_KNOWN = "levelKnown";
  private static final String CURSED = "cursed";

  private static final String CURSED_KNOWN = "cursedKnown";
  private static final String DURABILITY = "durability";
  protected static Hero curUser = null;

  protected static Item curItem = null;
  protected static CellSelector.Listener thrower = new CellSelector.Listener() {
    @Override
    public void onSelect(final Integer target) {
      if (target != null) {
        curItem.cast(curUser, target);
      }
    }

    @Override
    public String prompt() {
      return "Choose direction of throw";
    }
  };

  public static void evoke(final Hero hero) {
    hero.sprite.emitter().burst(Speck.factory(Speck.EVOKE), 5);
  }

  public static Item virtual(final Class<? extends Item> cl) {
    try {

      Item item = cl.newInstance();
      item.quantity = 0;
      return item;

    } catch (Exception e) {
      return null;
    }
  }

  public String defaultAction;

  protected String name = "smth";

  protected int image = 0;

  public boolean stackable = false;

  protected int quantity = 1;

  public int level = 0;

  public boolean levelKnown = false;

  private int durability = maxDurability();

  public boolean cursed;

  public boolean cursedKnown;

  public boolean unique = false;

  public ArrayList<String> actions(final Hero hero) {
    ArrayList<String> actions = new ArrayList<String>();
    actions.add(AC_DROP);
    actions.add(AC_THROW);
    return actions;
  }

  public void cast(final Hero user, final int dst) {

    final int cell = Ballistica.cast(user.pos, dst, false, true);
    user.sprite.zap(cell);
    user.busy();

    Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

    Char enemy = Actor.findChar(cell);
    QuickSlot.target(this, enemy);

    // FIXME!!!
    float delay = TIME_TO_THROW;
    if (this instanceof MissileWeapon) {
      delay *= ((MissileWeapon) this).speedFactor(user);
      if (enemy != null) {
        SnipersMark mark = user.buff(SnipersMark.class);
        if (mark != null) {
          if (mark.object == enemy.id()) {
            delay *= 0.5f;
          }
          user.remove(mark);
        }
      }
    }
    final float finalDelay = delay;

    ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).reset(user.pos, cell, this,
        new Callback() {
          @Override
          public void call() {
            Item.this.detach(user.belongings.backpack).onThrow(cell);
            user.spendAndNext(finalDelay);
          }
        });
  }

  public boolean collect() {
    return collect(Dungeon.hero.belongings.backpack);
  }

  public boolean collect(final Bag container) {

    ArrayList<Item> items = container.items;

    if (items.contains(this)) {
      return true;
    }

    for (Item item : items) {
      if ((item instanceof Bag) && ((Bag) item).grab(this)) {
        return collect((Bag) item);
      }
    }

    if (stackable) {

      Class<?> c = getClass();
      for (Item item : items) {
        if (item.getClass() == c) {
          item.quantity += quantity;
          item.updateQuickslot();
          return true;
        }
      }
    }

    if (items.size() < container.size) {

      if ((Dungeon.hero != null) && Dungeon.hero.isAlive()) {
        Badges.validateItemLevelAquired(this);
      }

      items.add(this);
      QuickSlot.refresh();
      Collections.sort(items, itemComparator);
      return true;

    } else {

      GLog.n(TXT_PACK_FULL, name());
      return false;

    }
  }

  public int considerState(int price) {
    if (cursed && cursedKnown) {
      price /= 2;
    }
    if (levelKnown) {
      if (level > 0) {
        price *= (level + 1);
        if (isBroken()) {
          price /= 2;
        }
      } else if (level < 0) {
        price /= (1 - level);
      }
    }
    if (price < 1) {
      price = 1;
    }

    return price;
  }

  public Item degrade() {

    level--;
    fix();

    return this;
  }

  final public Item degrade(final int n) {
    for (int i = 0; i < n; i++) {
      degrade();
    }

    return this;
  }

  public String desc() {
    return "";
  }

  public final Item detach(final Bag container) {

    if (quantity <= 0) {

      return null;

    } else if (quantity == 1) {

      return detachAll(container);

    } else {

      quantity--;
      updateQuickslot();

      try {
        Item detached = getClass().newInstance();
        detached.onDetach();
        return detached;
      } catch (Exception e) {
        return null;
      }
    }
  }

  public final Item detachAll(final Bag container) {

    for (Item item : container.items) {
      if (item == this) {
        container.items.remove(this);
        item.onDetach();
        QuickSlot.refresh();
        return this;
      } else if (item instanceof Bag) {
        Bag bag = (Bag) item;
        if (bag.contains(this)) {
          return detachAll(bag);
        }
      }
    }

    return this;
  }

  public void doDrop(final Hero hero) {
    hero.spendAndNext(TIME_TO_DROP);
    Dungeon.level.drop(detachAll(hero.belongings.backpack), hero.pos).sprite.drop(hero.pos);
  }

  public boolean doPickUp(final Hero hero) {
    if (collect(hero.belongings.backpack)) {

      GameScene.pickUp(this);
      Sample.INSTANCE.play(Assets.SND_ITEM);
      hero.spendAndNext(TIME_TO_PICK_UP);
      return true;

    } else {
      return false;
    }
  }

  public void doThrow(final Hero hero) {
    GameScene.selectCell(thrower);
  }

  public int durability() {
    return durability;
  }

  public int effectiveLevel() {
    return isBroken() ? 0 : level;
  }

  public void execute(final Hero hero) {
    execute(hero, defaultAction);
  }

  public void execute(final Hero hero, final String action) {

    curUser = hero;
    curItem = this;

    if (action.equals(AC_DROP)) {

      doDrop(hero);

    } else if (action.equals(AC_THROW)) {

      doThrow(hero);

    }
  }

  public void fix() {
    durability = maxDurability();
  }

  public void getBroken() {
  }

  public ItemSprite.Glowing glowing() {
    return null;
  }

  public Item identify() {

    levelKnown = true;
    cursedKnown = true;

    return this;
  }

  public int image() {
    return image;
  }

  public String info() {
    return desc();
  }

  public boolean isBroken() {
    return durability <= 0;
  }

  public boolean isEquipped(final Hero hero) {
    return false;
  }

  public boolean isIdentified() {
    return levelKnown && cursedKnown;
  }

  public boolean isUpgradable() {
    return true;
  }

  public int level() {
    return level;
  }

  public void level(final int value) {
    level = value;
  }

  final public int maxDurability() {
    return maxDurability(level);
  }

  public int maxDurability(final int lvl) {
    return 1;
  }

  public String name() {
    return name;
  }

  protected void onDetach() {
  }

  protected void onThrow(final int cell) {
    Heap heap = Dungeon.level.drop(this, cell);
    if (!heap.isEmpty()) {
      heap.sprite.drop(cell);
    }
  }

  public void polish() {
    if (durability < maxDurability()) {
      durability++;
    }
  }

  public int price() {
    return 0;
  }

  public int quantity() {
    return quantity;
  }

  public void quantity(final int value) {
    quantity = value;
  }

  public Item random() {
    return this;
  }

  @Override
  public void restoreFromBundle(final Bundle bundle) {
    quantity = bundle.getInt(QUANTITY);
    levelKnown = bundle.getBoolean(LEVEL_KNOWN);
    cursedKnown = bundle.getBoolean(CURSED_KNOWN);

    int level = bundle.getInt(LEVEL);
    if (level > 0) {
      upgrade(level);
    } else if (level < 0) {
      degrade(-level);
    }

    cursed = bundle.getBoolean(CURSED);

    if (isUpgradable()) {
      durability = bundle.getInt(DURABILITY);
    }
    if (durability <= 0) {
      durability = maxDurability(level);
    }

    QuickSlot.restore(bundle, this);
  }

  public String status() {
    return quantity != 1 ? Integer.toString(quantity) : null;
  }

  @Override
  public void storeInBundle(final Bundle bundle) {
    bundle.put(QUANTITY, quantity);
    bundle.put(LEVEL, level);
    bundle.put(LEVEL_KNOWN, levelKnown);
    bundle.put(CURSED, cursed);
    bundle.put(CURSED_KNOWN, cursedKnown);
    if (isUpgradable()) {
      bundle.put(DURABILITY, durability);
    }
    QuickSlot.save(bundle, this);
  }

  @Override
  public String toString() {

    if (levelKnown && (level != 0)) {
      if (quantity > 1) {
        return Utils.format(TXT_TO_STRING_LVL_X, name(), level, quantity);
      } else {
        return Utils.format(TXT_TO_STRING_LVL, name(), level);
      }
    } else {
      if (quantity > 1) {
        return Utils.format(TXT_TO_STRING_X, name(), quantity);
      } else {
        return Utils.format(TXT_TO_STRING, name());
      }
    }
  }

  public final String trueName() {
    return name;
  }

  public void updateQuickslot() {

    if (stackable) {
      Class<? extends Item> cl = getClass();
      if ((QuickSlot.primaryValue == cl) || (QuickSlot.secondaryValue == cl)) {
        QuickSlot.refresh();
      }
    } else if ((QuickSlot.primaryValue == this) || (QuickSlot.secondaryValue == this)) {
      QuickSlot.refresh();
    }
  }

  public Item upgrade() {

    cursed = false;
    cursedKnown = true;

    level++;
    fix();

    return this;
  }

  final public Item upgrade(final int n) {
    for (int i = 0; i < n; i++) {
      upgrade();
    }

    return this;
  }

  public void use() {
    if ((level > 0) && !isBroken()) {
      int threshold = (int) (maxDurability() * DURABILITY_WARNING_LEVEL);
      if ((durability-- >= threshold) && (threshold > durability) && levelKnown) {
        GLog.w(TXT_GONNA_BREAK, name());
      }
      if (isBroken()) {
        getBroken();
        if (levelKnown) {
          GLog.n(TXT_BROKEN, name());
          Dungeon.hero.interrupt();

          CharSprite sprite = Dungeon.hero.sprite;
          PointF point = sprite.center().offset(0, -16);
          if (this instanceof Weapon) {
            sprite.parent.add(Degradation.weapon(point));
          } else if (this instanceof Armor) {
            sprite.parent.add(Degradation.armor(point));
          } else if (this instanceof Ring) {
            sprite.parent.add(Degradation.ring(point));
          } else if (this instanceof Wand) {
            sprite.parent.add(Degradation.wand(point));
          }
          Sample.INSTANCE.play(Assets.SND_DEGRADE);
        }
      }
    }
  }

  public boolean visiblyBroken() {
    return levelKnown && isBroken();
  }

  public boolean visiblyCursed() {
    return cursed && cursedKnown;
  }

  public int visiblyUpgraded() {
    return levelKnown ? level : 0;
  }
}