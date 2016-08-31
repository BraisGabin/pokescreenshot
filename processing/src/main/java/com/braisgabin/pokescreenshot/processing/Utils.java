package com.braisgabin.pokescreenshot.processing;

import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
  public static void copyRecursive(AssetManager assets, String in, File out) throws IOException {
    final File file = new File(out, in);
    final String[] files = assets.list(in);
    if (files.length <= 0) {
      copy(assets.open(in), new FileOutputStream(file));
    } else {
      file.mkdir();
      for (String fileName : files) {
        copyRecursive(assets, in + "/" + fileName, out);
      }
    }
  }

  public static void copy(InputStream in, OutputStream out) throws IOException {
    try {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = in.read(buffer)) != -1) {
        out.write(buffer, 0, len);
      }
    } finally {
      in.close();
      out.close();
    }
  }
}
