package com.braisgabin.pokescreenshot.processing;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestUtils {
  static List<String> find(AssetManager assets, String root, Pattern pattern) throws IOException {
    final List<String> files = new ArrayList<>();
    find(assets, root, pattern, files);
    return files;
  }

  private static void find(AssetManager assets, String root, Pattern pattern, List<String> files) throws IOException {
    for (String name : assets.list(root)) {
      final String path = root + "/" + name;
      find(assets, path, pattern, files);
      if (pattern.matcher(name).matches()) {
        files.add(path);
      }
    }
  }
}
