package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import com.googlecode.tesseract.android.TessBaseAPI;

import net.sf.jsefa.common.lowlevel.filter.HeaderAndFooterFilter;
import net.sf.jsefa.csv.CsvDeserializer;
import net.sf.jsefa.csv.CsvIOFactory;
import net.sf.jsefa.csv.config.CsvConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.braisgabin.pokescreenshot.processing.Utils.copyRecursive;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(value = Parameterized.class)
public class OcrTest {
  @Parameterized.Parameters
  public static Collection<Screenshot> data() throws IOException {
    final CsvConfiguration config = new CsvConfiguration();
    config.setLineFilter(new HeaderAndFooterFilter(1, false, true));

    final Context context = InstrumentationRegistry.getContext();
    final CsvDeserializer deserializer = CsvIOFactory.createFactory(config, Screenshot.class).createDeserializer();
    final Reader reader = new InputStreamReader(context.getAssets().open("screenshots.csv"));
    deserializer.open(reader);

    final List<Screenshot> screenshots = new ArrayList<>();
    while (deserializer.hasNext()) {
      screenshots.add((Screenshot) deserializer.next());
    }

    reader.close();

    return screenshots;
  }

  private final Screenshot screenshot;
  private final Ocr ocr;

  public OcrTest(Screenshot screenshot) throws Exception {
    this.screenshot = screenshot;
    final AssetManager assets = InstrumentationRegistry.getContext().getAssets();
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inMutable = true;
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open(screenshot.file()), null, options);

    final Context context = InstrumentationRegistry.getContext();
    final File root = context.getCacheDir();
    try {
      copyRecursive(context.getAssets(), "tesseract", root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final TessBaseAPI tess = new TessBaseAPI();
    tess.init(root.getAbsolutePath() + "/tesseract/", "eng");
    tess.readConfigFile("pokemon");
    tess.setImage(bitmap);

    this.ocr = Ocr.create(tess, bitmap, screenshot.density(), null);
  }
  @Test
  public void testOcr() throws Exception {
    final Pokemon pokemon = ocr.ocr();
    assertThat(pokemon.getCp(), is(screenshot.getCp()));
    assertThat(pokemon.getHp(), is(screenshot.getHp()));
    assertThat(pokemon.getStardust(), is(screenshot.getStardus()));
    assertThat(pokemon.getCandyName(), is(screenshot.getCandyName()));
    assertThat(pokemon.getName(), is(screenshot.getName()));
  }
}
