package com.braisgabin.fileobservable;

import android.os.Build;

import com.github.phajduk.rxfileobserver.FileEvent;
import com.github.phajduk.rxfileobserver.RxFileObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FileObservable {

  public static Observable<File> newFiles(File file, Action1<Object> action) {
    final Observable<File> observable;

    if (Build.VERSION.SDK_INT == 23) {
      observable = newFiles23(file, action);
    } else {
      observable = newFilesOther(file, action);
    }

    return observable;
  }

  private static Observable<File> newFilesOther(final File file, final Action1<Object> action) {
    return RxFileObserver.create(file)
        .filter(new Func1<FileEvent, Boolean>() {
          private final Set<String> set = new HashSet<>();

          @Override
          public Boolean call(FileEvent fileEvent) {
            boolean filter = false;
            final String path = fileEvent.getPath();
            if (fileEvent.isCreate()) {
              set.add(path);
              action.call(path);
            } else if (fileEvent.isCloseWrite()) {
              if (set.remove(path)) {
                filter = true;
              }
            }
            return filter;
          }
        })
        .map(new Func1<FileEvent, File>() {
          @Override
          public File call(FileEvent fileEvent) {
            return new File(file, fileEvent.getPath());
          }
        });
  }

  private static Observable<File> newFiles23(final File file, Action1<Object> action) {
    final FF fileFilter = new FF();
    return Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
        .flatMap(new Func1<Long, Observable<File>>() {
          @Override
          public Observable<File> call(Long aLong) {
            final File[] files = file.listFiles(fileFilter);
            fileFilter.time = System.currentTimeMillis() / 1000;
            final Observable<File> observable;
            if (files == null) {
              observable = Observable.empty();
            } else {
              observable = Observable.from(files);
              fileFilter.ring.addAll(Arrays.asList(files));
            }
            return observable;
          }
        })
        .doOnNext(action);
  }

  private static class FF implements FileFilter {
    long time = Long.MAX_VALUE;
    final Ring<File> ring = new Ring<>(5);

    @Override
    public boolean accept(File file) {
      return file.lastModified() / 1000 >= time && !ring.contains(file);
    }
  }
}
