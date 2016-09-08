package com.braisgabin.pokescreenshot.processing;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class Pokemon {
  static Pokemon create(int id, String name, int atk, int def, int stam, String candy) {
    return new AutoValue_Pokemon(id, name, atk, def, stam, candy);
  }

  abstract int id();

  abstract String name();

  abstract int atk();

  abstract int def();

  abstract int stam();

  abstract String candy();
}
