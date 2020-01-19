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
package com.watabou.pixeldungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SystemTime;

import android.content.Context;
import hu.denahi.pixeldungeon.holy.quest.DungeonType;

public enum Rankings {

  INSTANCE;

  public static class Record implements Bundlable {

    private static final String REASON = "reason";
    private static final String WIN = "win";
    private static final String SCORE = "score";
    private static final String TIER = "tier";
    private static final String GAME = "gameFile";

    public String info;
    public boolean win;

    public HeroClass heroClass;
    public int armorTier;

    public int score;

    public String gameFile;

    @Override
    public void restoreFromBundle(final Bundle bundle) {

      info = bundle.getString(REASON);
      win = bundle.getBoolean(WIN);
      score = bundle.getInt(SCORE);

      heroClass = HeroClass.restoreInBundle(bundle);
      armorTier = bundle.getInt(TIER);

      gameFile = bundle.getString(GAME);
    }

    @Override
    public void storeInBundle(final Bundle bundle) {

      bundle.put(REASON, info);
      bundle.put(WIN, win);
      bundle.put(SCORE, score);

      heroClass.storeInBundle(bundle);
      bundle.put(TIER, armorTier);

      bundle.put(GAME, gameFile);
    }
  }

  public static final int TABLE_SIZE = 6;
  public static final String RANKINGS_FILE_YOG = "rankings.dat";
  public static final String RANKINGS_FILE_GOBLIN = "rankings_goblin.dat";

  public static final String DETAILS_FILE = "game_%d.dat";
  private static final String RECORDS = "records";
  private static final String LATEST = "latest";
  private static final String TOTAL = "total";

  private static final String WON = "won";

  private static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
    @Override
    public int compare(final Record lhs, final Record rhs) {
      return (int) Math.signum(rhs.score - lhs.score);
    }
  };

  private ArrayList<Record> records;

  public ArrayList<Record> gerRecords(){
    return records;
  }

  public int lastRecord;
  public int totalNumber;
  public int wonNumber;

  public void load(final int dungeonType) {
    records = new ArrayList<Rankings.Record>();

    try {
      Bundle bundle = loadRankingsFile(dungeonType);

      for (Bundlable record : bundle.getCollection(RECORDS)) {
        records.add((Record) record);
      }
      lastRecord = bundle.getInt(LATEST);

      totalNumber = bundle.getInt(TOTAL);
      if (totalNumber == 0) {
        totalNumber = records.size();
      }

      wonNumber = bundle.getInt(WON);
      if (wonNumber == 0) {
        for (Record rec : records) {
          if (rec.win) {
            wonNumber++;
          }
        }
      }

    } catch (Exception e) {
    }
  }

  private Bundle loadRankingsFile(final int dungeonType) throws IOException {
    InputStream input;
    switch (dungeonType) {
      case DungeonType.YOG:
        input = Game.instance.openFileInput(RANKINGS_FILE_YOG);
        break;
      case DungeonType.GOBLIN:
        input = Game.instance.openFileInput(RANKINGS_FILE_GOBLIN);
        break;
      default:
        // as always YOG
        input = Game.instance.openFileInput(RANKINGS_FILE_YOG);
        break;
    }

    Bundle bundle = Bundle.read(input);
    input.close();
    return bundle;
  }

  public void save(final int dungeonType) {
    Bundle bundle = new Bundle();
    bundle.put(RECORDS, records);
    bundle.put(LATEST, lastRecord);
    bundle.put(TOTAL, totalNumber);
    bundle.put(WON, wonNumber);

    try {
      saveRankingsFile(dungeonType, bundle);
    } catch (Exception e) {
    }
  }

  private void saveRankingsFile(final int dungeonType, final Bundle bundle) throws IOException {
    OutputStream output;
    switch (dungeonType) {
      case DungeonType.YOG:
        output = Game.instance.openFileOutput(RANKINGS_FILE_YOG, Context.MODE_PRIVATE);
        break;
      case DungeonType.GOBLIN:
        output = Game.instance.openFileOutput(RANKINGS_FILE_GOBLIN, Context.MODE_PRIVATE);
        break;
      default:
        // as always YOG
        output = Game.instance.openFileOutput(RANKINGS_FILE_YOG, Context.MODE_PRIVATE);
        break;
    }

    Bundle.write(bundle, output);
    output.close();
  }

  private int score(final boolean win, final int dungeonType) {
    return (Statistics.goldCollected
        + (Dungeon.hero.lvl * Statistics.getDeepestFloor(dungeonType) * 100))
        * (win ? 2 : 1);
  }

  public void submit(final boolean win) {

    load(Dungeon.dungeonType);

    Record rec = new Record();

    rec.info = Dungeon.resultDescription;
    rec.win = win;
    rec.heroClass = Dungeon.hero.heroClass;
    rec.armorTier = Dungeon.hero.tier();
    rec.score = score(win, Dungeon.dungeonType);

    String gameFile = Utils.format(DETAILS_FILE, SystemTime.now);
    try {
      Dungeon.saveGame(gameFile);
      rec.gameFile = gameFile;
    } catch (IOException e) {
      rec.gameFile = "";
    }

    records.add(rec);

    Collections.sort(records, scoreComparator);

    lastRecord = records.indexOf(rec);
    int size = records.size();
    if (size > TABLE_SIZE) {

      Record removedGame;
      if (lastRecord == (size - 1)) {
        removedGame = records.remove(size - 2);
        lastRecord--;
      } else {
        removedGame = records.remove(size - 1);
      }

      if (removedGame.gameFile.length() > 0) {
        Game.instance.deleteFile(removedGame.gameFile);
      }
    }

    totalNumber++;
    if (win) {
      wonNumber++;
    }

    Badges.validateGamesPlayed();

    save(Dungeon.dungeonType);
  }
}
