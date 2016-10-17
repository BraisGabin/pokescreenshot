package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static com.braisgabin.pokescreenshot.processing.Utils.copyRecursive;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

public class Tess2Test {
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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private TessBaseAPI tessBaseAPI;
  private Ocr ocr;

  public void setUp(String file) throws IOException {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inMutable = true;
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open(file), null, options);

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
  public void testCp_white() throws Exception {
    setUp("screenshots/white.png");
    thrown.expect(ScreenshotReader.CpException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.cp();
  }

  @Test
  public void testCp_staryu() throws Exception {
    setUp("screenshots/staryu.png");
    thrown.expect(ScreenshotReader.CpException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.cp();
  }

  @Test
  public void testHp() throws Exception {
    setUp("screenshots/just_cp.png");
    thrown.expect(ScreenshotReader.HpException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.hp();
  }

  @Test
  public void testStardust() throws Exception {
    setUp("screenshots/just_cp.png");
    thrown.expect(ScreenshotReader.StardustException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.stardust();
  }

  @Test
  public void testCandy() throws Exception {
    setUp("screenshots/just_cp.png");
    thrown.expect(ScreenshotReader.CandyException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.candy();
  }

  @Test
  public void testEvolveCandy() throws Exception {
    setUp("screenshots/just_cp.png");
    assertThat(ocr.evolveCandy(), is(0));
  }

  @Test
  public void testName() throws Exception {
    setUp("screenshots/just_cp.png");
    thrown.expect(ScreenshotReader.NameException.class);
    thrown.expectCause(isA(Tess.TessException.class));
    thrown.expectMessage("Error from Tesseract");
    ocr.name();
  }
}
