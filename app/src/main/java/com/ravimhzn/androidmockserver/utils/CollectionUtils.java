package com.ravimhzn.androidmockserver.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

//TODO -> Work in progress
public class CollectionUtils {

    public static <T> T first(Collection<T> unfiltered, Predicate<? super T> predicate) {
        Collection<T> collection = filter(unfiltered, predicate);
        return collection.isEmpty() ? null : collection.iterator().next();
    }

    public static <T> Collection<T> filter(Collection<T> unfiltered, Predicate<? super T> predicate) {
        Collection<T> result = new ArrayList<>();
        if (predicate != null) {
            for (T element : emptyIfNull(unfiltered)) {
                if (predicate.apply(element)) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.emptyList() : iterable;
    }

    public interface Predicate<T> {
        boolean apply(T type);
    }
}
