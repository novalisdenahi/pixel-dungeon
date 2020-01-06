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
package com.watabou.pixeldungeon.actors.mobs;

import java.util.ArrayList;
import java.util.HashSet;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Ooze;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.BurningFistSprite;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.LarvaSprite;
import com.watabou.pixeldungeon.sprites.RottingFistSprite;
import com.watabou.pixeldungeon.sprites.YogSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Yog extends Mob {

  public static class BurningFist extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
      RESISTANCES.add(ToxicGas.class);
      RESISTANCES.add(Death.class);
      RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
      IMMUNITIES.add(Amok.class);
      IMMUNITIES.add(Sleep.class);
      IMMUNITIES.add(Terror.class);
      IMMUNITIES.add(Burning.class);
    }

    {
      name = "burning fist";
      spriteClass = BurningFistSprite.class;

      mobType = MobType.DEMON;

      HP = HT = 200;
      defenseSkill = 25;

      EXP = 0;

      state = WANDERING;
    }

    public BurningFist() {
      super();
      fistsCount++;
    }

    @Override
    public boolean act() {
      for (int element : Level.NEIGHBOURS9) {
        GameScene.add(Blob.seed(pos + element, 2, Fire.class));
      }

      return super.act();
    }

    @Override
    public boolean attack(final Char enemy) {

      if (!Level.adjacent(pos, enemy.pos)) {
        spend(attackDelay());

        if (Char.hit(this, enemy, true)) {

          int dmg = damageRoll();
          enemy.damage(dmg, this);

          enemy.sprite.bloodBurstA(sprite.center(), dmg);
          enemy.sprite.flash();

          if (!enemy.isAlive() && (enemy == Dungeon.hero)) {
            Dungeon.fail(Utils.format(ResultDescriptions.BOSS, name, Dungeon.depth));
            GLog.n(TXT_KILL, name);
          }
          return true;

        } else {

          enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
          return false;
        }
      } else {
        return super.attack(enemy);
      }
    }

    @Override
    public int attackSkill(final Char target) {
      return 36;
    }

    @Override
    protected boolean canAttack(final Char enemy) {
      return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    public int damageRoll() {
      return Random.NormalIntRange(20, 32);
    }

    @Override
    public String description() {
      return TXT_DESC;

    }

    @Override
    public void die(final Object cause) {
      super.die(cause);
      fistsCount--;
    }

    @Override
    public int dr() {
      return 15;
    }

    @Override
    public HashSet<Class<?>> immunities() {
      return IMMUNITIES;
    }

    @Override
    public HashSet<Class<?>> resistances() {
      return RESISTANCES;
    }
  }

  public static class Larva extends Mob {

    {
      name = "god's larva";
      spriteClass = LarvaSprite.class;

      mobType = MobType.DEMON;

      HP = HT = 25;
      defenseSkill = 20;

      EXP = 0;

      state = HUNTING;
    }

    @Override
    public int attackSkill(final Char target) {
      return 30;
    }

    @Override
    public int damageRoll() {
      return Random.NormalIntRange(15, 20);
    }

    @Override
    public String description() {
      return TXT_DESC;

    }

    @Override
    public int dr() {
      return 8;
    }
  }

  public static class RottingFist extends Mob {

    private static final int REGENERATION = 4;

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
      RESISTANCES.add(ToxicGas.class);
      RESISTANCES.add(Death.class);
      RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
      IMMUNITIES.add(Amok.class);
      IMMUNITIES.add(Sleep.class);
      IMMUNITIES.add(Terror.class);
      IMMUNITIES.add(Poison.class);
      IMMUNITIES.add(Vertigo.class);
    }

    {
      name = "rotting fist";
      spriteClass = RottingFistSprite.class;

      mobType = MobType.DEMON;

      HP = HT = 300;
      defenseSkill = 25;

      EXP = 0;

      state = WANDERING;
    }

    public RottingFist() {
      super();
      fistsCount++;
    }

    @Override
    public boolean act() {

      if (Level.water[pos] && (HP < HT)) {
        sprite.emitter().burst(ShadowParticle.UP, 2);
        HP += REGENERATION;
      }

      return super.act();
    }

    @Override
    public int attackProc(final Char enemy, final int damage) {
      if (Random.Int(3) == 0) {
        Buff.affect(enemy, Ooze.class);
        enemy.sprite.burst(0xFF000000, 5);
      }

      return damage;
    }

    @Override
    public int attackSkill(final Char target) {
      return 36;
    }

    @Override
    public int damageRoll() {
      return Random.NormalIntRange(24, 36);
    }

    @Override
    public String description() {
      return TXT_DESC;

    }

    @Override
    public void die(final Object cause) {
      super.die(cause);
      fistsCount--;
    }

    @Override
    public int dr() {
      return 15;
    }

    @Override
    public HashSet<Class<?>> immunities() {
      return IMMUNITIES;
    }

    @Override
    public HashSet<Class<?>> resistances() {
      return RESISTANCES;
    }
  }

  private static final String TXT_DESC =
      "Yog-Dzewa is an Old God, a powerful entity from the realms of chaos. A century ago, the ancient dwarves "
          +
          "barely won the war against its army of demons, but were unable to kill the god itself. Instead, they then "
          +
          "imprisoned it in the halls below their city, believing it to be too weak to rise ever again.";

  private static int fistsCount = 0;

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

  static {

    IMMUNITIES.add(Death.class);
    IMMUNITIES.add(Terror.class);
    IMMUNITIES.add(Amok.class);
    IMMUNITIES.add(Charm.class);
    IMMUNITIES.add(Sleep.class);
    IMMUNITIES.add(Burning.class);
    IMMUNITIES.add(ToxicGas.class);
    IMMUNITIES.add(ScrollOfPsionicBlast.class);
  }

  {
    name = Dungeon.depth == Statistics.getDeepestFloor(Dungeon.dungeonType) ? "Yog-Dzewa"
        : "echo of Yog-Dzewa";
    spriteClass = YogSprite.class;

    HP = HT = 300;

    EXP = 50;

    state = PASSIVE;
  }

  public Yog() {
    super();
  }

  @Override
  public void beckon(final int cell) {
  }

  @Override
  public void damage(int dmg, final Object src) {

    if (fistsCount > 0) {

      for (Mob mob : Dungeon.level.mobs) {
        if ((mob instanceof BurningFist) || (mob instanceof RottingFist)) {
          mob.beckon(pos);
        }
      }

      dmg >>= fistsCount;
    }

    super.damage(dmg, src);
  }

  @Override
  public int defenseProc(final Char enemy, final int damage) {

    ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

    for (int element : Level.NEIGHBOURS8) {
      int p = pos + element;
      if ((Actor.findChar(p) == null) && (Level.passable[p] || Level.avoid[p])) {
        spawnPoints.add(p);
      }
    }

    if (spawnPoints.size() > 0) {
      Larva larva = new Larva();
      larva.pos = Random.element(spawnPoints);

      GameScene.add(larva);
      Actor.addDelayed(new Pushing(larva, pos, larva.pos), -1);
    }

    return super.defenseProc(enemy, damage);
  }

  @Override
  public String description() {
    return TXT_DESC;

  }

  @SuppressWarnings("unchecked")
  @Override
  public void die(final Object cause) {

    for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
      if ((mob instanceof BurningFist) || (mob instanceof RottingFist)) {
        mob.die(cause);
      }
    }

    GameScene.bossSlain();
    Dungeon.level.drop(new SkeletonKey(), pos).sprite.drop();
    super.die(cause);

    yell("...");
  }

  @Override
  public HashSet<Class<?>> immunities() {
    return IMMUNITIES;
  }

  @Override
  public void notice() {
    super.notice();
    yell("Hope is an illusion...");
  }

  public void spawnFists() {
    RottingFist fist1 = new RottingFist();
    BurningFist fist2 = new BurningFist();

    do {
      fist1.pos = pos + Level.NEIGHBOURS8[Random.Int(8)];
      fist2.pos = pos + Level.NEIGHBOURS8[Random.Int(8)];
    } while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || (fist1.pos == fist2.pos));

    GameScene.add(fist1);
    GameScene.add(fist2);
  }
}