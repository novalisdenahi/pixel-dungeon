/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Toth Daniel
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
import com.watabou.pixeldungeon.actors.mobs.npcs.GoblinAsh;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.Pan;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class WndAsh extends Window {
  private static final String TXT_PAN =
      "Yes! Finally! Please hand it to me! " +
          "Just wait a minute. I'll make something for you. " +
          "Something soo good like no one ever tasted.";
  private static final String TXT_MUSHROOM =
      "What a beautiful piece of mushroom. I hope you didn't tried it, " +
          "because it's toxic. This is a challenge for me. If I wanna be the best chef then "
          + "I have to face every challenge along the way with courage. I'll make something for you.";
  private static final String TXT_BROTH = "Goblin Broth";
  private static final String TXT_STEW = "Mushroom Stew";

  private static final int WIDTH = 120;
  private static final int BTN_HEIGHT = 20;
  private static final float GAP = 2;

  public WndAsh(final GoblinAsh ash, final Item item) {

    super();

    IconTitle titlebar = new IconTitle();
    titlebar.icon(new ItemSprite(item.image(), null));
    titlebar.label(Utils.capitalize(item.name()));
    titlebar.setRect(0, 0, WIDTH, 0);
    add(titlebar);

    BitmapTextMultiline message =
        PixelScene.createMultiline(item instanceof Pan ? TXT_PAN : TXT_MUSHROOM, 8);
    message.maxWidth = WIDTH;
    message.measure();
    message.y = titlebar.bottom() + GAP;
    add(message);

    RedButton btnWeapon = new RedButton(TXT_BROTH) {
      @Override
      protected void onClick() {
        selectReward(ash, item, GoblinAsh.Quest.broth);
      }
    };
    btnWeapon.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
    add(btnWeapon);

    RedButton btnArmor = new RedButton(TXT_STEW) {
      @Override
      protected void onClick() {
        selectReward(ash, item, GoblinAsh.Quest.stew);
      }
    };
    btnArmor.setRect(0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT);
    add(btnArmor);

    resize(WIDTH, (int) btnArmor.bottom());
  }

  private void selectReward(final GoblinAsh ash, final Item item, final Item reward) {

    hide();

    item.detach(Dungeon.hero.belongings.backpack);

    if (reward.doPickUp(Dungeon.hero)) {
      GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.name());
    } else {
      Dungeon.level.drop(reward, ash.pos).sprite.drop();
    }

    ash.yell("Goodbye! and Bon appetite!");

    GoblinAsh.Quest.complete();
  }
}
