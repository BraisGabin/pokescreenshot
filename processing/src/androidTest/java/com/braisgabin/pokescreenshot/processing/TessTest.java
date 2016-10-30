package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

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

  @Parameterized.Parameters(name = "{0}[{index}")
  public static Collection<Screenshot> data() throws IOException {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    return Screenshot.loadScreenshots(assets);
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
    assertThat(ocr.stardust(), is(screenshot.getStardust()));
    assertThat(ocr.candy(), is(screenshot.getCandy()));
    assertThat(ocr.evolveCandy(), is(screenshot.getEvolveCandy()));
    assertThat(ocr.name(), is(screenshot.getName()));
  }
}
