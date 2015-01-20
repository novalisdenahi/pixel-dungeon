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
package com.watabou.pixeldungeon.scenes;

import hu.denahi.pixeldungeon.holy.quest.DungeonType;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.BannerSprites;
import com.watabou.pixeldungeon.effects.Fireball;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.PrefsButton;

public class QuestChooseScene extends PixelScene {

    private static class DashboardItem extends Button {

        public static final float SIZE = 48;

        private static final int IMAGE_SIZE = 32;

        private Image image;
        private BitmapText label;

        public DashboardItem(final String text, final int index) {
            super();

            image.frame(image.texture.uvRect(index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE));
            label.text(text);
            label.measure();

            setSize(SIZE, SIZE);
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image(Assets.QUESTBOARD);
            add(image);

            label = createText(9);
            add(label);
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = align(x + ((width - image.width()) / 2));
            image.y = align(y);

            label.x = align(x + ((width - label.width()) / 2));
            label.y = align(image.y + image.height() + 2);
        }

        @Override
        protected void onTouchDown() {
            image.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
        }

        @Override
        protected void onTouchUp() {
            image.resetColor();
        }
    }

    public static int curDungeonType = DungeonType.YOG; // Default is YOG

    private static final String TXT_DUNGEON_YOG_DZEWA = "Dungeon Of Yog-Dzewa";
    private static final String TXT_DUNGEON_GOBLINS = "Dungeon Of Goblins";
    private static final String TXT_DUNGEON_MAD_MAGE = "Dungeon Of The Made Mage";

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.play(Assets.THEME, true);
        Music.INSTANCE.volume(1f);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        float height = 150;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        Image title = BannerSprites.get(BannerSprites.Type.SELECT_DUNGEON);
        add(title);

        title.x = (w - title.width()) / 2;
        title.y = (h - height) / 2;

        placeTorch(title.x + 18, title.y - 10);
        placeTorch((title.x + title.width) - 18, title.y - 10);

        DashboardItem btnGoblins = new DashboardItem(TXT_DUNGEON_GOBLINS, 1) {
            @Override
            protected void onClick() {
                switchNoFadeIntoSelectedDungeon(DungeonType.GOBLIN);
            }
        };
        btnGoblins.setPos((w / 2) - (btnGoblins.width() / 2), ((h / 2) - DashboardItem.SIZE));
        add(btnGoblins);

        // TODO fix icon
        DashboardItem btnMadMage = new DashboardItem(TXT_DUNGEON_MAD_MAGE, 1) {
            @Override
            protected void onClick() {
                switchNoFadeIntoSelectedDungeon(DungeonType.YOG);
            }
        };
        btnMadMage.setPos((w / 2) - (btnMadMage.width() / 2), (h / 2));
        add(btnMadMage);

        DashboardItem btnYogDzewa = new DashboardItem(TXT_DUNGEON_YOG_DZEWA, 0) {
            @Override
            protected void onClick() {
                switchNoFadeIntoSelectedDungeon(DungeonType.YOG);
            }
        };
        btnYogDzewa.setPos((w / 2) - (btnYogDzewa.width() / 2), ((h / 2) + DashboardItem.SIZE));
        add(btnYogDzewa);

        BitmapText version = new BitmapText("v " + Game.version, font1x);
        version.measure();
        version.hardlight(0x888888);
        version.x = w - version.width();
        version.y = h - version.height();
        add(version);

        PrefsButton btnPrefs = new PrefsButton();
        btnPrefs.setPos(0, 0);
        add(btnPrefs);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        fadeIn();
    }

    private void placeTorch(final float x, final float y) {
        Fireball fb = new Fireball();
        fb.setPos(x, y);
        add(fb);
    }

    private void switchNoFadeIntoSelectedDungeon(final int selectedDungeonType) {
        Dungeon.hero = null;
        curDungeonType = selectedDungeonType;
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        Game.switchScene(IntroScene.class);
    }
}
