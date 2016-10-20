package com.braisgabin.pokescreenshot;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;

import com.pushtorefresh.storio.contentresolver.Changes;
import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;

import rx.Observable;
import rx.functions.Func1;

public class ScreenshotObservable {
  public static Observable<Uri> newScreenshot(final ContentResolver contentResolver) {
    StorIOContentResolver storIOContentResolver = DefaultStorIOContentResolver.builder()
        .contentResolver(contentResolver)
        .build();

    return storIOContentResolver
        .observeChangesOfUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        .flatMap(new Func1<Changes, Observable<Uri>>() {
          @Override
          public Observable<Uri> call(Changes changes) {
            final Uri uri = getLastImageTaken(contentResolver);
            if (uri != null) {
              return Observable.just(uri);
            } else {
              return Observable.empty();
            }
          }
        });
  }

  // Extracted from: http://stackoverflow.com/a/27103868/842697
  private static Uri getLastImageTaken(ContentResolver contentResolver) {
    Cursor cursor = null;
    try {
      final String[] projection = {ImageColumns._ID};
      final String selection = ImageColumns.DATE_ADDED + " > ? AND " + ImageColumns.DATA + " LIKE '%reenshot%'";
      final String[] selectionArgs = {"" + (-2 + System.currentTimeMillis() / 1000)};
      final String order = ImageColumns.DATE_ADDED + " DESC";
      cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, order);
      final int columnIndex = cursor.getColumnIndexOrThrow(ImageColumns._ID);
      if (cursor.moveToFirst()) {
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getLong(columnIndex));
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
