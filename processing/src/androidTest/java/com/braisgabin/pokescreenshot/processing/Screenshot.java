package com.braisgabin.pokescreenshot.processing;

import android.content.res.AssetManager;
import android.graphics.Point;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.braisgabin.pokescreenshot.processing.TestUtils.find;

class Screenshot {
  static List<Screenshot> loadScreenshots(AssetManager assets) throws IOException {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

    final List<String> paths = find(assets, "screenshots", Pattern.compile(".+\\.yml"));

    final List<Screenshot> screenshots = new ArrayList<>(paths.size());
    for (String path : paths) {
      final Screenshot screenshot = mapper.readValue(assets.open(path), Screenshot.class);
      screenshot.setFile(path.replace(".yml", ".png"));
      screenshots.add(screenshot);
    }

    return screenshots;
  }

  private String file;

  private Point initialPoint;

  private int trainerLvl;

  private float pokemonLvl;

  private int cp;

  private int hp;

  private int stardust;

  private String candyType;

  private Integer candyEvolve;

  private String name;

  public String file() {
    return file;
  }

  public Point initialPoint() {
    return initialPoint;
  }

  public int getTrainerLvl() {
    return trainerLvl;
  }

  public float getLvl() {
    return pokemonLvl;
  }

  public int getCp() {
    return cp;
  }

  public int getHp() {
    return hp;
  }

  public int getStardust() {
    return stardust;
  }

  public String getCandy() {
    return candyType;
  }

  public int getEvolveCandy() {
    return candyEvolve == null ? 0 : candyEvolve;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return file.substring("screenshots/".length(), file.length() - 4);
  }

  public void setFile(String file) {
    this.file = file;
  }
}
