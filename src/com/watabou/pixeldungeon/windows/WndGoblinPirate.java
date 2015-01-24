/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014 Tóth Dániel
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
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinPirate;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class WndGoblinPirate extends Window {

    private static final String TXT_MESSAGE =
            "Blimey! Aye! You are a the pirate! If my crew come back i'll count you in if you want it. "
                    + "Aaarrgghhh! Don't say am  cheat out belongings from you. Choose your price!";
    private static final String TXT_BOOTY = "Pirate Booty";
    private static final String TXT_CUTLASS = "The Capt'n's cutlass";

    private static final String TXT_FARAWELL = "Farewll matey! *hic* Don't forgett, you can join to my crew anytime!";

    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 18;
    private static final float GAP = 2;

    public WndGoblinPirate(final GoblinPirate goblinPirate, final Item item) {

        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(item.image(), null));
        titlebar.label(Utils.capitalize(item.name()));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE, 6);
        message.maxWidth = WIDTH;
        message.measure();
        message.y = titlebar.bottom() + GAP;
        add(message);

        RedButton btnBattle = new RedButton(TXT_BOOTY) {
            @Override
            protected void onClick() {
                selectReward(goblinPirate, item, GoblinPirate.Quest.item1);
            }
        };
        btnBattle.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
        add(btnBattle);

        RedButton btnNonBattle = new RedButton(TXT_CUTLASS) {
            @Override
            protected void onClick() {
                selectReward(goblinPirate, item, GoblinPirate.Quest.item2);
            }
        };
        btnNonBattle.setRect(0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
        add(btnNonBattle);

        resize(WIDTH, (int) btnNonBattle.bottom());
    }

    private void selectReward(final GoblinPirate goblinPirate, final Item item, final Item reward) {

        hide();

        item.detach(Dungeon.hero.belongings.backpack);

        reward.identify();
        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.name());
        } else {
            Dungeon.level.drop(reward, goblinPirate.pos).sprite.drop();
        }

        // GoblinPirate don't die. Stay and being drunk.
        GoblinPirate.Quest.complete();

        goblinPirate.yell(Utils.format(TXT_FARAWELL));

    }
}
