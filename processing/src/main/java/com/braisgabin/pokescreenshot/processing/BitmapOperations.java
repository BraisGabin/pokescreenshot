package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

class BitmapOperations {

  static void filter(Context context, Bitmap bitmap, int value) {
    RenderScript rs = RenderScript.create(context);
    ScriptC_ocrpreprocess script = new ScriptC_ocrpreprocess(rs);
    Allocation allocation = Allocation.createFromBitmap(rs, bitmap);
    script.invoke_process(allocation, value);
    allocation.copyTo(bitmap);
  }
}
