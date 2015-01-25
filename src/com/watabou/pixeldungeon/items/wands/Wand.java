/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.items.wands;

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.rings.RingOfPower.Power;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class Wand extends KindOfWeapon {

    protected class Charger extends Buff {

        private static final float TIME_TO_CHARGE = 40f;

        @Override
        public boolean act() {

            if (curCharges < maxCharges) {
                curCharges++;
                updateQuickslot();
            }

            delay();

            return true;
        }

        @Override
        public boolean attachTo(final Char target) {
            super.attachTo(target);
            delay();

            return true;
        }

        protected void delay() {
            float time2charge = ((Hero) target).heroClass == HeroClass.MAGE ?
                    TIME_TO_CHARGE / (float) Math.sqrt(1 + level) :
                    TIME_TO_CHARGE;
            spend(time2charge);
        }
    }

    public static final String AC_ZAP = "ZAP";
    private static final String TXT_WOOD = "This thin %s wand is warm to the touch. Who knows what it will do when used?";
    private static final String TXT_DAMAGE = "When this wand is used as a melee weapon, its average damage is %d points per hit.";

    private static final String TXT_WEAPON = "You can use this wand as a melee weapon.";
    private static final String TXT_FIZZLES = "your wand fizzles; it must be out of charges for now";

    private static final String TXT_SELF_TARGET = "You can't target yourself";

    private static final float TIME_TO_ZAP = 1f;

    @SuppressWarnings("unchecked")
    public static void initWoods() {
        handler = new ItemStatusHandler<Wand>((Class<? extends Wand>[]) wands, woods, images);
    }

    @SuppressWarnings("unchecked")
    public static void restore(final Bundle bundle) {
        handler = new ItemStatusHandler<Wand>((Class<? extends Wand>[]) wands, woods, images, bundle);
    }

    public static void save(final Bundle bundle) {
        handler.save(bundle);
    }

    public int maxCharges = initialCharges();

    public int curCharges = maxCharges;
    protected Charger charger;
    private boolean curChargeKnown = false;

    protected boolean hitChars = true;

    private static final Class<?>[] wands = {
            WandOfTeleportation.class,
            WandOfSlowness.class,
            WandOfFirebolt.class,
            WandOfPoison.class,
            WandOfRegrowth.class,
            WandOfBlink.class,
            WandOfLightning.class,
            WandOfAmok.class,
            WandOfTelekinesis.class,
            WandOfFlock.class,
            WandOfDisintegration.class,
            WandOfAvalanche.class
    };

    private static final String[] woods =
    { "holly", "yew", "ebony", "cherry", "teak", "rowan", "willow", "mahogany", "bamboo", "purpleheart", "oak", "birch" };

    private static final Integer[] images = {
            ItemSpriteSheet.WAND_HOLLY,
            ItemSpriteSheet.WAND_YEW,
            ItemSpriteSheet.WAND_EBONY,
            ItemSpriteSheet.WAND_CHERRY,
            ItemSpriteSheet.WAND_TEAK,
            ItemSpriteSheet.WAND_ROWAN,
            ItemSpriteSheet.WAND_WILLOW,
            ItemSpriteSheet.WAND_MAHOGANY,
            ItemSpriteSheet.WAND_BAMBOO,
            ItemSpriteSheet.WAND_PURPLEHEART,
            ItemSpriteSheet.WAND_OAK,
            ItemSpriteSheet.WAND_BIRCH };

    private static ItemStatusHandler<Wand> handler;

    private static final String MAX_CHARGES = "maxCharges";

    private static final String CUR_CHARGES = "curCharges";

    private static final String CUR_CHARGE_KNOWN = "curChargeKnown";

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(final Integer target) {

            if (target != null) {

                if (target == curUser.pos) {
                    GLog.i(TXT_SELF_TARGET);
                    return;
                }

                final Wand curWand = (Wand) Item.curItem;

                curWand.setKnown();

                final int cell = Ballistica.cast(curUser.pos, target, true, curWand.hitChars);
                curUser.sprite.zap(cell);

                QuickSlot.target(curItem, Actor.findChar(cell));

                if (curWand.curCharges > 0) {

                    curUser.busy();

                    curWand.fx(cell, new Callback() {
                        @Override
                        public void call() {
                            curWand.onZap(cell);
                            curWand.wandUsed();
                        }
                    });

                    Invisibility.dispel();

                } else {

                    curUser.spendAndNext(TIME_TO_ZAP);
                    GLog.w(TXT_FIZZLES);
                    curWand.levelKnown = true;

                    curWand.updateQuickslot();
                }

            }
        }

        @Override
        public String prompt() {
            return "Choose direction to zap";
        }
    };

    public static boolean allKnown() {
        return handler.known().size() == wands.length;
    }

    private String wood;

    {
        defaultAction = AC_ZAP;
    }

    public Wand() {
        super();

        calculateDamage();

        try {
            image = handler.image(this);
            wood = handler.label(this);
        } catch (Exception e) {
            // Wand of Magic Missile
        }
    };

    @Override
    public ArrayList<String> actions(final Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if ((curCharges > 0) || !curChargeKnown) {
            actions.add(AC_ZAP);
        }
        if (hero.heroClass != HeroClass.MAGE) {
            actions.remove(AC_EQUIP);
            actions.remove(AC_UNEQUIP);
        }
        return actions;
    }

    @Override
    public void activate(final Hero hero) {
        charge(hero);
    }

    private void calculateDamage() {
        int tier = 1 + (level / 3);
        MIN = tier;
        MAX = ((((tier * tier) - tier) + 10) / 2) + level;
    }

    public void charge(final Char owner) {
        (charger = new Charger()).attachTo(owner);
    }

    @Override
    public boolean collect(final Bag container) {
        if (super.collect(container)) {
            if (container.owner != null) {
                charge(container.owner);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Item degrade() {
        super.degrade();

        updateLevel();
        updateQuickslot();

        return this;
    }

    @Override
    public boolean doUnequip(final Hero hero, final boolean collect) {
        onDetach();
        return super.doUnequip(hero, collect);
    }

    @Override
    public void execute(final Hero hero, final String action) {
        if (action.equals(AC_ZAP)) {

            curUser = hero;
            curItem = this;
            GameScene.selectCell(zapper);

        } else {

            super.execute(hero, action);

        }
    }

    protected void fx(final int cell, final Callback callback) {
        MagicMissile.blueLight(curUser.sprite.parent, curUser.pos, cell, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public Item identify() {

        setKnown();
        curChargeKnown = true;
        super.identify();

        updateQuickslot();

        return this;
    }

    @Override
    public String info() {
        StringBuilder info = new StringBuilder(isKnown() ? desc() : String.format(TXT_WOOD, wood));
        if (Dungeon.hero.heroClass == HeroClass.MAGE) {
            info.append("\n\n");
            if (levelKnown) {
                info.append(String.format(TXT_DAMAGE, MIN + ((MAX - MIN) / 2)));
            } else {
                info.append(String.format(TXT_WEAPON));
            }
        }
        return info.toString();
    }

    protected int initialCharges() {
        return 2;
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown() && curChargeKnown;
    }

    protected boolean isKnown() {
        return handler.isKnown(this);
    }

    public int level() {
        if (charger != null) {
            Power power = charger.target.buff(Power.class);
            return power == null ? level : Math.max(level + power.level, 0);
        } else {
            return level;
        }
    }

    @Override
    public String name() {
        return isKnown() ? name : wood + " wand";
    }

    @Override
    public void onDetach() {
        stopCharging();
    }

    protected abstract void onZap(int cell);

    @Override
    public int price() {
        int price = 50;
        if (cursed && cursedKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level > 0) {
                price *= (level + 1);
            } else if (level < 0) {
                price /= (1 - level);
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    @Override
    public Item random() {
        if (Random.Float() < 0.5f) {
            upgrade();
            if (Random.Float() < 0.15f) {
                upgrade();
            }
        }

        return this;
    }

    @Override
    public void restoreFromBundle(final Bundle bundle) {
        super.restoreFromBundle(bundle);
        maxCharges = bundle.getInt(MAX_CHARGES);
        curCharges = bundle.getInt(CUR_CHARGES);
        curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
    }

    public void setKnown() {
        if (!isKnown()) {
            handler.know(this);
        }

        Badges.validateAllWandsIdentified();
    }

    @Override
    public String status() {
        if (levelKnown) {
            return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
        } else {
            return null;
        }
    }

    public void stopCharging() {
        if (charger != null) {
            charger.detach();
            charger = null;
        }
    }

    @Override
    public void storeInBundle(final Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_CHARGES, maxCharges);
        bundle.put(CUR_CHARGES, curCharges);
        bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(super.toString());

        String status = status();
        if (status != null) {
            sb.append(" (" + status + ")");
        }

        return sb.toString();
    }

    protected void updateLevel() {
        maxCharges = Math.min(initialCharges() + level, 9);
        curCharges = Math.min(curCharges, maxCharges);

        calculateDamage();
    }

    @Override
    public Item upgrade() {

        super.upgrade();

        updateLevel();
        curCharges = Math.min(curCharges + 1, maxCharges);
        updateQuickslot();

        return this;
    }

    protected void wandUsed() {
        curCharges--;
        updateQuickslot();

        curUser.spendAndNext(TIME_TO_ZAP);
    }
}
