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

import hu.denahi.pixeldungeon.holy.quest.DungeonType;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.windows.WndStory;

public class IntroScene extends PixelScene {
    private static final String TEXT =
            "Many heroes of all kinds ventured into the Dungeon before you. Some of them have returned with treasures and magical "
                    + "artifacts, most have never been heard of since. But none have succeeded in retrieving the Amulet of Yendor, "
                    + "which is told to be hidden in the depths of the Dungeon.\n\n"
                    + "You consider yourself ready for the challenge, but most importantly, you feel that fortune smiles on you. "
                    + "It's time to start your own adventure!";

    private static final String TEXT_GOBLIN =
            "You was passing the Temple of Estera when a priestess running out. She told you the temple was attacked by goblins. "
                    + "The goblins not left empty-handed the palce. They captured one of the High Priest Of Estera. "
                    + "";

    @Override
    public void create() {
        super.create();
        // TODO ADD MAD MAGE
        switch (QuestChooseScene.curDungeonType) {
        case DungeonType.YOG:
            add(createDungeonWndStory(TEXT));
            break;
        case DungeonType.GOBLIN:
            add(createDungeonWndStory(TEXT_GOBLIN));
            break;

        default:
            // Default is YOG
            add(createDungeonWndStory(TEXT));
            break;
        }

        fadeIn();
    }

    private WndStory createDungeonWndStory(final String wndStoryText) {
        return new WndStory(wndStoryText) {
            @Override
            public void hide() {
                super.hide();
                Game.switchScene(InterlevelScene.class);
            }
        };
    }
}
