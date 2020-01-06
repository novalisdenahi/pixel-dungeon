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
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.HealthBar;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;

public class IconTitle extends Component {

  private static final float FONT_SIZE = 9;

  private static final float GAP = 2;

  protected Image imIcon;
  protected BitmapTextMultiline tfLabel;
  protected HealthBar health;

  private float healthLvl = Float.NaN;

  public IconTitle() {
    super();
  }

  public IconTitle(final Image icon, final String label) {
    super();

    icon(icon);
    label(label);
  }

  public IconTitle(final Item item) {
    this(
        new ItemSprite(item.image(), item.glowing()),
        Utils.capitalize(item.toString()));
  }

  public void color(final int color) {
    tfLabel.hardlight(color);
  }

  @Override
  protected void createChildren() {
    imIcon = new Image();
    add(imIcon);

    tfLabel = PixelScene.createMultiline(FONT_SIZE);
    tfLabel.hardlight(Window.TITLE_COLOR);
    add(tfLabel);

    health = new HealthBar();
    add(health);
  }

  public void health(final float value) {
    health.level(healthLvl = value);
    layout();
  }

  public void icon(final Image icon) {
    remove(imIcon);
    add(imIcon = icon);
  }

  public void label(final String label) {
    tfLabel.text(label);
  }

  public void label(final String label, final int color) {
    tfLabel.text(label);
    tfLabel.hardlight(color);
  }

  @Override
  protected void layout() {

    health.visible = !Float.isNaN(healthLvl);

    imIcon.x = x;
    imIcon.y = y;

    tfLabel.x = PixelScene.align(PixelScene.uiCamera, imIcon.x + imIcon.width() + GAP);
    tfLabel.maxWidth = (int) (width - tfLabel.x);
    tfLabel.measure();
    tfLabel.y = PixelScene.align(PixelScene.uiCamera,
        imIcon.height > tfLabel.height() ? imIcon.y + ((imIcon.height() - tfLabel.baseLine()) / 2)
            : imIcon.y);

    if (health.visible) {
      health.setRect(tfLabel.x,
          Math.max(tfLabel.y + tfLabel.height(), (imIcon.y + imIcon.height()) - health.height()),
          tfLabel.maxWidth, 0);
      height = health.bottom();
    } else {
      height = Math.max(imIcon.y + imIcon.height(), tfLabel.y + tfLabel.height());
    }
  }
}