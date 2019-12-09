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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ShopkeeperSprite;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndTradeItem;

public class Shopkeeper extends NPC {

  public static WndBag sell() {
    return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE, "Select an item to sell");
  }

  {
    name = "shopkeeper";
    spriteClass = ShopkeeperSprite.class;
  }

  private static WndBag.Listener itemSelector = new WndBag.Listener() {
    @Override
    public void onSelect(final Item item) {
      if (item != null) {
        WndBag parentWnd = Shopkeeper.sell();
        GameScene.show(new WndTradeItem(item, parentWnd));
      }
    }
  };

  @Override
  protected boolean act() {

    throwItem();

    sprite.turnTo(pos, Dungeon.hero.pos);
    spend(TICK);
    return true;
  }

  @Override
  public void add(final Buff buff) {
    flee();
  }

  @Override
  public void damage(final int dmg, final Object src) {
    flee();
  }

  @Override
  public String description() {
    return "This stout guy looks more appropriate for a trade district in some large city " +
        "than for a dungeon. His prices explain why he prefers to do business here.";
  }

  protected void flee() {
    for (Heap heap : Dungeon.level.heaps.values()) {
      if (heap.type == Heap.Type.FOR_SALE) {
        CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
        heap.destroy();
      }
    }

    destroy();

    sprite.killAndErase();
    CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
  }

  @Override
  public void interact() {
    Shopkeeper.sell();
  }

  @Override
  public boolean reset() {
    return true;
  }
}
