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

import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.WndError;
import com.watabou.pixeldungeon.windows.WndRanking;

import hu.denahi.pixeldungeon.holy.quest.DungeonType;

public class RankingsScene extends PixelScene {
  private class DungeonTypeItem extends Button {

    public static final float SIZE = 17;

    private static final int IMAGE_SIZE = 32;

    private Image image;
    public int dungeonType;

    public DungeonTypeItem(final int dungeonType) {
      super();
      this.dungeonType = dungeonType;
      image.frame(image.texture.uvRect(dungeonType * IMAGE_SIZE, 0, (dungeonType + 1) * IMAGE_SIZE,
          IMAGE_SIZE));
      image.scale.scale(0.5f);
      setSize(SIZE, SIZE);
    }

    @Override
    protected void createChildren() {
      super.createChildren();

      image = new Image(Assets.QUESTBOARD);
      add(image);

    }

    @Override
    protected void layout() {
      super.layout();
      image.x = PixelScene.align(x + ((width - image.width()) / 2));
      image.y = PixelScene.align(y);
    }

    @Override
    protected void onTouchDown() {
      image.brightness(1.5f);
      Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
      pages[selected].visible = false;
      icons[selected].image.scale.scale(0.9f);
      selected = dungeonType;
      this.remove(pages[selected]);
      pages[selected] = new RankingListPage(selected);
      add(pages[selected]);
      pages[selected].visible = true;
      icons[selected].image.scale.invScale(0.9f);
    }

    @Override
    protected void onTouchUp() {
      image.resetColor();
    }
  }

  private class RankingListPage extends Group {

    public RankingListPage(final int dungeonType) {
      Rankings.INSTANCE.load(dungeonType);

      if (Rankings.INSTANCE.gerRecords().size() > 0) {
        float rowHeight = PixelDungeon.landscape() ? ROW_HEIGHT_L : ROW_HEIGHT_P;

        float left = ((w - Math.min(MAX_ROW_WIDTH, w)) / 2) + GAP;
        float top = PixelScene.align((h - ((rowHeight * MAX_ROW_NUMBER))) / 2);
        int pos = 0;

        for (Rankings.Record rec : Rankings.INSTANCE.gerRecords()) {
          Record row = new Record(pos, pos == Rankings.INSTANCE.lastRecord, rec, dungeonType);
          row.setRect(left, top + (pos * rowHeight), w - (left * 2), rowHeight);
          add(row);

          pos++;
        }

        if (Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE) {
          BitmapText label = PixelScene.createText(TXT_TOTAL, 8);
          label.hardlight(DEFAULT_COLOR);
          label.measure();
          add(label);

          BitmapText won = PixelScene.createText(Integer.toString(Rankings.INSTANCE.wonNumber), 8);
          won.hardlight(Window.TITLE_COLOR);
          won.measure();
          add(won);

          BitmapText total = PixelScene.createText("/" + Rankings.INSTANCE.totalNumber, 8);
          total.hardlight(DEFAULT_COLOR);
          total.measure();
          total.x = PixelScene.align((w - total.width()) / 2);
          total.y = PixelScene.align(top + (pos * rowHeight) + GAP);
          add(total);

          float tw = label.width() + won.width() + total.width();
          label.x = PixelScene.align((w - tw) / 2);
          won.x = label.x + label.width();
          total.x = won.x + won.width();
          label.y = won.y = total.y = PixelScene.align(top + (pos * rowHeight) + GAP);
        }

      } else {

        BitmapText titleNoGames = PixelScene.createText(TXT_NO_GAMES, 8);
        titleNoGames.hardlight(DEFAULT_COLOR);
        titleNoGames.measure();
        titleNoGames.x = PixelScene.align((w - titleNoGames.width()) / 2);
        titleNoGames.y = PixelScene.align((h - titleNoGames.height()) / 2);
        add(titleNoGames);

      }
    }

  }


  public static class Record extends Button {

    private static final float GAP = 4;

    private static final int TEXT_WIN = 0xFFFF88;
    private static final int TEXT_LOSE = 0xCCCCCC;
    private static final int FLARE_WIN = 0x888866;
    private static final int FLARE_LOSE = 0x666666;

    private String gameFileName;

    private ItemSprite shield;
    private Flare flare;
    private BitmapText position;
    private BitmapTextMultiline desc;
    private Image classIcon;

    public Record(final int pos, final boolean latest, final Rankings.Record rec, final int dungeonType) {
      super();

      gameFileName = rec.gameFile;

      if (latest) {
        flare = new Flare(6, 24);
        flare.angularSpeed = 90;
        flare.color(rec.win ? FLARE_WIN : FLARE_LOSE);
        addToBack(flare);
      }

      position.text(Integer.toString(pos + 1));
      position.measure();

      desc.text(rec.info);
      desc.measure();

      if (rec.win) {

        if(dungeonType == DungeonType.GOBLIN){
          shield.view(ItemSpriteSheet.SYMBOLOFESTERA, null);
        }else{
          shield.view(ItemSpriteSheet.AMULET, null);
        }
        position.hardlight(TEXT_WIN);
        desc.hardlight(TEXT_WIN);
      } else {
        position.hardlight(TEXT_LOSE);
        desc.hardlight(TEXT_LOSE);
      }

      classIcon.copy(Icons.get(rec.heroClass));
    }

    @Override
    protected void createChildren() {

      super.createChildren();

      shield = new ItemSprite(ItemSpriteSheet.TOMB, null);
      add(shield);

      position = new BitmapText(PixelScene.font1x);
      add(position);

      desc = PixelScene.createMultiline(9);
      add(desc);

      classIcon = new Image();
      add(classIcon);
    }

    @Override
    protected void layout() {

      super.layout();

      shield.x = x;
      shield.y = y + ((height - shield.height) / 2);

      position.x = PixelScene.align(shield.x + ((shield.width - position.width()) / 2));
      position.y = PixelScene.align(shield.y + ((shield.height - position.height()) / 2) + 1);

      if (flare != null) {
        flare.point(shield.center());
      }

      classIcon.x = PixelScene.align((x + width) - classIcon.width);
      classIcon.y = shield.y;

      desc.x = shield.x + shield.width + GAP;
      desc.maxWidth = (int) (classIcon.x - desc.x);
      desc.measure();
      desc.y = (position.y + position.baseLine()) - desc.baseLine();
    }

    @Override
    protected void onClick() {
      if (this.gameFileName.length() > 0) {
        parent.add(new WndRanking(gameFileName));
      } else {
        parent.add(new WndError(TXT_NO_INFO));
      }
    }
  }

  private static final int DEFAULT_COLOR = 0xCCCCCC;
  private static final String TXT_TITLE = "Top Rankings";

  private static final String TXT_TOTAL = "Games played: ";

  private static final String TXT_NO_GAMES = "No games have been played yet.";
  private static final String TXT_NO_INFO = "No additional information";

  private static final float ROW_HEIGHT_L = 22;

  private static final float ROW_HEIGHT_P = 28;

  private static final float MAX_ROW_WIDTH = 180;

  private static final float MAX_ROW_NUMBER = 6;

  private static final float GAP = 4;

  private Archs archs;

  private int w;

  private int h;

  private int selected;

  private DungeonTypeItem[] icons = new DungeonTypeItem[2];
  private Group[] pages = new Group[2];

  @Override
  protected void onMenuPressed() {
    super.onMenuPressed();
  }

  @Override
  public void update() {
    super.update();
  }

  @Override
  public void create() {

    super.create();

    w = Camera.main.width;
    h = Camera.main.height;

    Music.INSTANCE.play(Assets.THEME, true);
    Music.INSTANCE.volume(1f);

    uiCamera.visible = false;

    archs = new Archs();
    archs.setSize(w, h);
    add(archs);

    BitmapText title = PixelScene.createText(TXT_TITLE, 9);
    title.hardlight(Window.TITLE_COLOR);
    title.measure();
    title.x = PixelScene.align((w - title.width()) / 2);
    title.y = PixelScene.align((15 - title.baseLine()) / 2);
    add(title);

    DungeonTypeItem yogDungeonTypeItem = new DungeonTypeItem(DungeonType.YOG);
    add(yogDungeonTypeItem);
    icons[DungeonType.YOG] = yogDungeonTypeItem;
    DungeonTypeItem goblinDungeonTypeItem = new DungeonTypeItem(DungeonType.GOBLIN);
    add(goblinDungeonTypeItem);
    icons[DungeonType.GOBLIN] = goblinDungeonTypeItem;

    float dungeonTypeItemsY = title.y + GAP + title.height();
    yogDungeonTypeItem.setPos((w / 2) - (DungeonTypeItem.SIZE / 2) - (DungeonTypeItem.SIZE / 2) - GAP,
        dungeonTypeItemsY);
    goblinDungeonTypeItem.setPos((w / 2) - (DungeonTypeItem.SIZE / 2) + (DungeonTypeItem.SIZE / 2)+ GAP,
            dungeonTypeItemsY);

    pages[DungeonType.YOG] = new RankingListPage(DungeonType.YOG);
    add(pages[DungeonType.YOG]);
    pages[DungeonType.YOG].visible = false;
    pages[DungeonType.GOBLIN] = new RankingListPage(DungeonType.GOBLIN);
    add(pages[DungeonType.GOBLIN]);
    pages[DungeonType.GOBLIN].visible = false;
    selected = Dungeon.dungeonType;

    this.remove(pages[selected]);
    pages[selected] = new RankingListPage(selected);
    add(pages[selected]);
    pages[selected].visible = true;
    icons[selected].image.scale.invScale(0.9f);

    this.bringToFront(pages[selected]);

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
