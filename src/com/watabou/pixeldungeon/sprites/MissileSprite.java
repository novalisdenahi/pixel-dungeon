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
package com.watabou.pixeldungeon.sprites;

import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

    private static final float SPEED = 240f;

    private Callback callback;

    public MissileSprite() {
        super();
        originToCenter();
    }

    @Override
    public void onComplete(final Tweener tweener) {
        kill();
        if (callback != null) {
            callback.call();
        }
    }

    public void reset(final int from, final int to, final int image, final Glowing glowing, final Callback listener) {
        revive();

        view(image, glowing);

        callback = listener;

        point(DungeonTilemap.tileToWorld(from));
        PointF dest = DungeonTilemap.tileToWorld(to);

        PointF d = PointF.diff(dest, point());
        speed.set(d).normalize().scale(SPEED);

        if ((image == 31) || (image == 108) || (image == 109) || (image == 110)) {

            angularSpeed = 0;
            angle = 135 - (float) ((Math.atan2(d.x, d.y) / 3.1415926) * 180);

        } else {

            angularSpeed = (image == 15) || (image == 106) ? 1440 : 720;

        }

        PosTweener tweener = new PosTweener(this, dest, d.length() / SPEED);
        tweener.listener = this;
        parent.add(tweener);
    }

    public void reset(final int from, final int to, final Item item, final Callback listener) {
        if (item == null) {
            reset(from, to, 0, null, listener);
        } else {
            reset(from, to, item.image(), item.glowing(), listener);
        }
    }
}
