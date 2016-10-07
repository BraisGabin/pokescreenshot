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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class TessTest {
  private static File root;

  @BeforeClass
  public static void setUpClass() {
    final Context context = InstrumentationRegistry.getContext();
    root = context.getCacheDir();
    try {
      copyRecursive(context.getAssets(), "tesseract", root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Parameterized.Parameters(name="{0}[{index}")
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
  private TessBaseAPI tessBaseAPI;
  private Ocr ocr;

  public TessTest(Screenshot screenshot) {
    this.screenshot = screenshot;
  }

  @Before
  public void setUp() throws IOException {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inMutable = true;
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open(screenshot.file()), null, options);

    tessBaseAPI = new TessBaseAPI();
    tessBaseAPI.init(root.getAbsolutePath() + "/tesseract/", "eng");
    tessBaseAPI.readConfigFile("pokemon");
    tessBaseAPI.setImage(bitmap);

    this.ocr = new Ocr(Tess.create(tessBaseAPI, context, bitmap, null));
  }

  @After
  public void shutDown() {
    tessBaseAPI.end();
  }

  @Test
  public void testOcr() throws Exception {
    assertThat(ocr.cp(), is(screenshot.getCp()));
    assertThat(ocr.hp(), is(screenshot.getHp()));
    assertThat(ocr.weight(), is(screenshot.getWeight()));
    assertThat(ocr.stardust(), is(screenshot.getStardus()));
    assertThat(ocr.candy(), is(screenshot.getCandy()));
    assertThat(ocr.name(), is(screenshot.getName()));
  }
}
