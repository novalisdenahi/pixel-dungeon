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
package com.watabou.pixeldungeon.actors.mobs;

public enum MobType {
    NONE(null, null),
    UNDEAD("undead",
            "Just bones, rothing flash and evil aura. Nothing living."),
    DEMON("demon",
            "Creature from the Nine Hell. Pure evil or worse.");

    private String title;

    private String desc;

    private static final String MOBTYPE = "mobType";

    private MobType(final String title, final String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public String title() {
        return title;
    }

}