package de.admir.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */

public class BinarySearch {
    public static final int NOT_FOUND = -1;

    public static <T, R extends Comparable<R>> List<T> search(List<T> list, R searchValue, Function<T, R> mapFunction,
                                                              Comparator<R> comparator) {
        int left = 0;
        int right = list.size() - 1;
        int index = binarySearch(list, searchValue, mapFunction, comparator, left, right);
        if (index == NOT_FOUND)
            return Collections.emptyList();
        return searchNeighbors(list, index, searchValue, mapFunction, comparator);
    }

    private static <T, R extends Comparable<R>> int binarySearch(List<T> list, R searchValue, Function<T, R> mapFunction,
                                                                 Comparator<R> comparator, int left, int right) {
        if (right < left) {
            return NOT_FOUND;
        }
        int mid = (left + right) >>> 1;
        if (comparator.compare(searchValue, mapFunction.apply(list.get(mid))) > 0) {
            return binarySearch(list, searchValue, mapFunction, comparator, mid + 1, right);
        } else if (comparator.compare(searchValue, mapFunction.apply(list.get(mid))) < 0) {
            return binarySearch(list, searchValue, mapFunction, comparator, left, mid - 1);
        } else {
            return mid;
        }
    }

    private static <T, R extends Comparable<R>> List<T> searchNeighbors(List<T> list, int index, R searchValue,
                                                                        Function<T, R> mapFunction, Comparator<R> comparator) {
        List<T> result = new ArrayList<>();
        result.add(list.get(index));

        for (int l = index - 1; l >= 0 && comparator.compare(searchValue, mapFunction.apply(list.get(l))) == 0; l--) {
            result.add(list.get(l));
        }
        for (int r = index + 1;
             r < list.size() && comparator.compare(searchValue, mapFunction.apply(list.get(r))) == 0; r++) {
            result.add(list.get(r));
        }
        return result;
    }
}

