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

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Fear;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.GoblinSewerBossLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.AlphaWorgSprite;
import com.watabou.utils.Random;

public class AlphaWorg extends Mob {

    {
        name = Dungeon.depth == Statistics.getDeepestFloor(Dungeon.dungeonType) ? "Alpha Worg"
                : "cub of the Alpha Worg";
        spriteClass = AlphaWorgSprite.class;

        HP = HT = 55;
        defenseSkill = 9;
        baseSpeed = 2f;

        EXP = 10;

    }

    private static final float DELAY_OF_HOWLS = 1f;

    @Override
    public int attackSkill(final Char target) {
        return 13;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 12);
    }

    @Override
    public String defenseVerb() {
        return "dodge";
    }

    @Override
    public String description() { // TODO FIXME
        return
                "Worgs are like the surface wolf. Except they are bigger, strongger and more bloodthirsty. "
                + "The goblins often tame them with the purpose of riding. The worg meat is not realy tasty, "
                + "but eatable if you are hungry. ";
    }

    @Override
    public void die(final Object cause) {

        super.die(cause);

        ((GoblinSewerBossLevel) Dungeon.level).unseal();

        GameScene.bossSlain();
        Dungeon.level.drop(new SkeletonKey(), pos).sprite.drop();

        // Badges.validateBossSlain(); // TODO fix

        yell("AWOOUUuuuu...");
    }

    @Override
    protected boolean doAttack(final Char enemy) {
        if (Random.Int(5) > 0) {
            // Normal attack
            return super.doAttack(enemy);
        } else {
            // TODO add howls sound
            // Sample.INSTANCE.play(Assets.SND_LULLABY);
            sprite.centerEmitter().start(Speck.factory(Speck.BLACK_NOTE), 0.3f, 3);
            ((AlphaWorgSprite) sprite).howls();
            yell("AWOOUUUUU!!");
            Buff.affect(enemy, Fear.class).setTheFearfulEnemy(this);
            spend(DELAY_OF_HOWLS);
            return true;
        }

    }

    @Override
    public int dr() {
        return 4;
    }
}
