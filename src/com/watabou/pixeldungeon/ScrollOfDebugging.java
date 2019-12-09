package com.watabou.pixeldungeon;

import java.util.ArrayList;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;

public class ScrollOfDebugging extends Item {

  {
    name = "Scroll of Debugging";
    image = 40;
  }

  public static final String AC_READ = "READ";

  @Override
  public ArrayList<String> actions(final Hero hero) {
    ArrayList<String> actions = super.actions(hero);
    actions.add(AC_READ);
    return actions;
  }

  @Override
  public void execute(final Hero hero, final String action) {
    if (action.equals(AC_READ)) {

      new ScrollOfEnchantment().collect();

    } else {

      super.execute(hero, action);

    }
  }

  @Override
  public boolean isIdentified() {
    return true;
  }

  @Override
  public boolean isUpgradable() {
    return false;
  }
}
