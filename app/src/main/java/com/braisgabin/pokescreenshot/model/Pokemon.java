package com.braisgabin.pokescreenshot.model;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.braisgabin.pokescreenshot.Utils;
import com.braisgabin.pokescreenshot.processing.CoreStats;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import java.util.List;

@AutoValue
public abstract class Pokemon implements PokemonModel, CoreStats {
  public static final Factory<Pokemon> FACTORY = new Factory<>(new Creator<Pokemon>() {
    @Override
    public Pokemon create(long id, @NonNull String name, int atk, int def, int stam, @NonNull String candy, int evolveCandy) {
      return new AutoValue_Pokemon(id, name, atk, def, stam, candy, evolveCandy);
    }
  });

  public static final RowMapper<Pokemon> MAPPER = FACTORY.select_by_candyMapper();

  public static List<Pokemon> selectByCandy(SQLiteDatabase database, String candy) {
    return Utils.list(database, MAPPER, SELECT_BY_CANDY, candy);
  }

  public static List<Pokemon> selectByName(SQLiteDatabase database, String name) {
    return Utils.list(database, MAPPER, SELECT_BY_NAME, name);
  }
}
