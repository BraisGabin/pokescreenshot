package com.braisgabin.pokescreenshot.model;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.braisgabin.pokescreenshot.Utils;
import com.braisgabin.pokescreenshot.processing.CoreStats;
import com.braisgabin.pokescreenshot.processing.Measures;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import java.util.List;

@AutoValue
public abstract class Pokemon implements PokemonModel, CoreStats, Measures {
  public static final Factory<Pokemon> FACTORY = new Factory<>(new Creator<Pokemon>() {
    @Override
    public Pokemon create(long id, @NonNull String name, int atk, int def, int stam, @NonNull String candy, float weight, float height) {
      return new AutoValue_Pokemon(id, name, atk, def, stam, candy, weight, height);
    }
  });

  public static final RowMapper<Pokemon> MAPPER = FACTORY.select_by_candyMapper();

  public static List<Pokemon> selectByCandy(SQLiteDatabase database, String candy) {
    return Utils.list(database, MAPPER, SELECT_BY_CANDY, candy);
  }
}
