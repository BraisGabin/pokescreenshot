package com.braisgabin.fileobservable;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.max;

class Ring<T> implements Collection<T> {
  private final Object[] array;
  private int count;
  private int index;


  Ring(int maxSize) {
    array = new Object[maxSize];
    count = 0;
    index = 0;
  }

  @Override
  public int size() {
    return count;
  }

  @Override
  public boolean isEmpty() {
    return count == 0;
  }

  @Override
  public boolean contains(Object o) {
    for (int i = 0; i < count; i++) {
      if (array[i].equals(o)) {
        return true;
      }
    }
    return false;
  }

  @NonNull
  @Override
  public Iterator<T> iterator() {
    throw new UnsupportedOperationException();
  }

  @NonNull
  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException();
  }

  @NonNull
  @Override
  public <T1> T1[] toArray(@NonNull T1[] t1s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean add(T t) {
    array[index] = t;
    index++;
    count = max(index, count);
    index = index % array.length;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> collection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(@NonNull Collection<? extends T> collection) {
    for (T item : collection) {
      add(item);
    }
    return true;
  }

  @Override
  public boolean removeAll(@NonNull Collection<?> collection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(@NonNull Collection<?> collection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    count = 0;
    index = 0;
  }

  @Override
  public boolean equals(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }
}
