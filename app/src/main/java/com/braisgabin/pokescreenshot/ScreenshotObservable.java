package com.braisgabin.pokescreenshot;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;

import com.pushtorefresh.storio.contentresolver.Changes;
import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;

import java.io.File;

import rx.Observable;
import rx.functions.Func1;

public class ScreenshotObservable {
  public static Observable<File> newScreenshot(final ContentResolver contentResolver) {
    StorIOContentResolver storIOContentResolver = DefaultStorIOContentResolver.builder()
        .contentResolver(contentResolver)
        .build();

    return storIOContentResolver
        .observeChangesOfUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        .flatMap(new Func1<Changes, Observable<File>>() {
          @Override
          public Observable<File> call(Changes changes) {
            final String path = getLastImageTaken(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (path != null && path.contains("reenshot")) {
              return Observable.just(new File(path));
            } else {
              return Observable.empty();
            }
          }
        });
  }

  // Extracted from: http://stackoverflow.com/a/27103868/842697
  private static String getLastImageTaken(ContentResolver contentResolver, Uri contentUri) {
    Cursor cursor = null;
    try {
      final String[] projection = {ImageColumns.DATA};
      final String selection = ImageColumns.DATE_ADDED + " > ?";
      final String[] selectionArgs = {"" + (-2 + System.currentTimeMillis() / 1000)};
      final String order = ImageColumns.DATE_ADDED + " DESC";
      cursor = contentResolver.query(contentUri, projection, selection, selectionArgs, order);
      final int columnIndex = cursor.getColumnIndexOrThrow(ImageColumns.DATA);
      if (cursor.moveToFirst()) {
        return cursor.getString(columnIndex);
      } else {
        return null;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}
