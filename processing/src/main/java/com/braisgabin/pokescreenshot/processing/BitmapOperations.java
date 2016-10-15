package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class BitmapOperations {

  public static void filter(Context context, Bitmap bitmap, int heightCp, int heightArc, int valueCp, int valueNoCp) {
    RenderScript rs = RenderScript.create(context);
    ScriptC_ocrpreprocess script = new ScriptC_ocrpreprocess(rs);
    Allocation allocation = Allocation.createFromBitmap(rs, bitmap);
    script.set_script(script);
    script.invoke_process(allocation, heightCp, heightArc, valueCp, valueNoCp);
    allocation.copyTo(bitmap);
  }
}
