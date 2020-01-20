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
package com.watabou.pixeldungeon.scenes;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.Window;

import android.content.Intent;
import android.net.Uri;

public class AboutScene extends PixelScene {

  private static final String TXT =
                  "Code & graphics: Novalis Denahi\n" +
                  "Original Code & graphics: Watabou\n" +
                  "Music: Cube_Code\n\n" +
                  "This game is inspired by Watabou's Pixel Dungeon. " +
                  "Basically it's the same game with a few extra stuff. " +
                  "I hope you will enjoy them.\n\n" +
                  "Please consider to support the original game creator and check " +
                          "out the awesome inspiring works of Watabou:";

  private static final String LINK_ITCH = "watabou.itch.io/";
  private static final String LINK_PATREON = "patreon.com/watawatabou";

  @Override
  public void create() {
    super.create();

    Image wata = Icons.WATA.get();
    Image estera = Icons.PRIEST.get();

    BitmapTextMultiline text = PixelScene.createMultiline(TXT, 8);
    text.maxWidth = Math.min(Camera.main.width, 120);
    text.measure();
    add(text);

    text.x = PixelScene.align((Camera.main.width - text.width()) / 2);
    text.y = PixelScene.align((Camera.main.height - text.height()) / 2 - wata.height());

    BitmapTextMultiline linkITCH = PixelScene.createMultiline(LINK_ITCH, 8);
    linkITCH.maxWidth = Math.min(Camera.main.width, 120);
    linkITCH.measure();
    linkITCH.hardlight(Window.TITLE_COLOR);
    add(linkITCH);

    linkITCH.x = text.x;
    linkITCH.y = text.y + text.height();

    TouchArea hotAreaITCH = new TouchArea(linkITCH) {
      @Override
      protected void onClick(final Touch touch) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+LINK_ITCH));
        Game.instance.startActivity(intent);
      }
    };
    add(hotAreaITCH);

    BitmapTextMultiline linkPatreon = PixelScene.createMultiline(LINK_PATREON, 8);
    linkPatreon.maxWidth = Math.min(Camera.main.width, 120);
    linkPatreon.measure();
    linkPatreon.hardlight(Window.TITLE_COLOR);
    add(linkPatreon);

    linkPatreon.x = text.x;
    linkPatreon.y = linkITCH.y + linkITCH.height();

    TouchArea hotAreaPatreon = new TouchArea(linkPatreon) {
      @Override
      protected void onClick(final Touch touch) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+LINK_PATREON));
        Game.instance.startActivity(intent);
      }
    };
    add(hotAreaPatreon);


    wata.x = PixelScene.align((Camera.main.width - wata.width) / 2);
    wata.y = text.y - wata.height - 8;
    add(wata);

    new Flare(7, 64).color(0x112233, true).show(wata, 0).angularSpeed = +20;

    //TODO add medal with colors and stuff. or own icon
    estera.x = PixelScene.align((Camera.main.width - estera.width) / 2);
    estera.y = linkITCH.y + estera.height + 8;
    add(estera);

    new Flare(7, 64).color(0x666666, true).show(estera, 0).angularSpeed = +20;

    Archs archs = new Archs();
    archs.setSize(Camera.main.width, Camera.main.height);
    addToBack(archs);

    ExitButton btnExit = new ExitButton();
    btnExit.setPos(Camera.main.width - btnExit.width(), 0);
    add(btnExit);

    fadeIn();
  }

  @Override
  protected void onBackPressed() {
    PixelDungeon.switchNoFade(TitleScene.class);
  }
}
