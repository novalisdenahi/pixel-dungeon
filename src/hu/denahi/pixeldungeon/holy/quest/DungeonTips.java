package hu.denahi.pixeldungeon.holy.quest;

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Tóth Dániel
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
public class DungeonTips {
  // TODO ADD MAD MAGE
  private static final String[] TIPS_GOBLIN = {
      "Don't overestimate your strength, use weapons and armor you can handle.",
      "Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
      "Remember, that raising your strength is not the only way to access better equipment, you can go "
          +
          "the other way lowering its strength requirement with Scrolls of Upgrade.",
      "You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 6th level.",

      "Beware of the Alfa Worg!",

      "Goblin-Mart - all you need for successful adventure!",
      "Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
          "when you actually need them.",
      "Being hungry doesn't hurt, but starving does hurt.",
      "Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
          "a closed door when you know it is approaching.",

      "There is a reason why he is the Goblin King! Not let your guard down!"
  };

  private static final String[] TIPS = {
      "Don't overestimate your strength, use weapons and armor you can handle.",
      "Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
      "Remember, that raising your strength is not the only way to access better equipment, you can go "
          +
          "the other way lowering its strength requirement with Scrolls of Upgrade.",
      "You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 6th level.",

      "Beware of Goo!",

      "Pixel-Mart - all you need for successful adventure!",
      "Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
          "when you actually need them.",
      "Being hungry doesn't hurt, but starving does hurt.",
      "Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
          "a closed door when you know it is approaching.",

      "Don't let The Tengu out!",

      "Pixel-Mart. Spend money. Live longer.",
      "When you're attacked by several monsters at the same time, try to retreat behind a door.",
      "If you are burning, you can't put out the fire in the water while levitating.",
      "There is no sense in possessing more than one Ankh at the same time, because you will lose them upon resurrecting.",

      "DANGER! Heavy machinery can cause injury, loss of limbs or death!",

      "Pixel-Mart. A safer life in dungeon.",
      "When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
      "In a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
      "The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",

      "No weapons allowed in the presence of His Majesty!",

      "Pixel-Mart. Special prices for demon hunters!",
      "The text is written in demonic language.",
      "The text is written in demonic language.",
      "The text is written in demonic language."
  };

  public static String[] getTips(final int dungeonType) {
    switch (dungeonType) {
      case DungeonType.YOG:
        return TIPS;
      case DungeonType.GOBLIN:
        return TIPS_GOBLIN;
      default:
        return TIPS; // default YOG Tips
    }
  }
}
