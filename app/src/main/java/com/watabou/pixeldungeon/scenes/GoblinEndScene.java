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

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.utils.Random;

public class GoblinEndScene extends PixelScene {

  private static final String TXT_EXIT = "Blessed day by Estera";

  private static final int WIDTH = 120;
  private static final int BTN_HEIGHT = 18;
  private static final float LARGE_GAP = 8;

  private static final String TXT =
      "The kidnapped priestess looks like went through a lot of things, but without a serious " +
              "injury. She is able to follow you up to the surface. The temple of Estera is " +
              "already cleaned from the intruders. The High Priest of Estera thankful for your " +
              "help. She promises you, they will help to fight against the evil which lives under " +
              "the city.  A new goblin king will rise to the throne, but there is a more ancient " +
              "evil waiting in the dungeons.";

  private Image priestAvatar;

  private float timer = 0;


  @Override
  public void create() {
    super.create();

    BitmapTextMultiline text = null;

      text = PixelScene.createMultiline(TXT, 8);
      text.maxWidth = WIDTH;
      text.measure();
      add(text);


    priestAvatar = new Image(Assets.HIGH_PRIEST);
    add(priestAvatar);

    RedButton btnExit = new RedButton(TXT_EXIT) {
      @Override
      protected void onClick() {
        win();
      }
    };
    btnExit.setSize(WIDTH, BTN_HEIGHT);
    add(btnExit);

    float height = priestAvatar.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height();

    priestAvatar.x = PixelScene.align((Camera.main.width - priestAvatar.width) / 2);
    priestAvatar.y = PixelScene.align((Camera.main.height - height) / 2);

    text.x = PixelScene.align((Camera.main.width - text.width()) / 2);
    text.y = priestAvatar.y + priestAvatar.height + LARGE_GAP;

    btnExit.setPos((Camera.main.width - btnExit.width()) / 2, text.y + text.height() + LARGE_GAP);


    new Flare(8, 48).color(0xFFDDBB, true).show(priestAvatar, 0).angularSpeed = +30;

    fadeIn();
  }

  private void win(){
    Badges.validateHolyQuestVictory();
    Dungeon.win(ResultDescriptions.WIN_GOBLIN);
    Dungeon.deleteGame(Dungeon.hero.heroClass, true);
    Game.switchScene(RankingsScene.class);
  }

  @Override
  protected void onBackPressed() {
    win();
  }

  @Override
  public void update() {
    super.update();

    if ((timer -= Game.elapsed) < 0) {
      timer = Random.Float(0.5f, 5f);

      Speck star = (Speck) recycle(Speck.class);
      star.reset(0, priestAvatar.x + 10.5f, priestAvatar.y + 5.5f, Speck.DISCOVER);
      add(star);
    }

  }
}
