package com.braisgabin.pokescreenshot.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Pokemon implements PokemonModel {
  public static final Factory<Pokemon> FACTORY = new Factory<>(new Creator<Pokemon>() {
    @Override
    public Pokemon create(long id, @NonNull String name, long atk, long def, long stam, @NonNull String candy) {
      return new AutoValue_Pokemon(id, name, atk, def, stam, candy);
    }
  });

  public static final RowMapper<Pokemon> MAPPER = FACTORY.select_by_candyMapper();
}
