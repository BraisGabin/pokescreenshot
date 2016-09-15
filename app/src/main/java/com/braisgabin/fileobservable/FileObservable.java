package com.braisgabin.fileobservable;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

// TODO move this to a separate library
public class FileObservable {

  public static Observable<File> newFiles(final File file) {
    final FF fileFilter = new FF();
    return Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
        .flatMap(new Func1<Long, Observable<File>>() {
          @Override
          public Observable<File> call(Long aLong) {
            final File[] files = file.listFiles(fileFilter);
            fileFilter.time = System.currentTimeMillis() / 1000;
            return Observable.from(files);
          }
        })
        .doOnNext(new Action1<File>() {
          @Override
          public void call(File file) {
            fileFilter.ring.add(file);
          }
        });
  }

  static class FF implements FileFilter {
    long time = Long.MAX_VALUE;
    final Ring<File> ring = new Ring<>(5);

    @Override
    public boolean accept(File file) {
      return file.lastModified() / 1000 >= time && !ring.contains(file);
    }
  }
}
