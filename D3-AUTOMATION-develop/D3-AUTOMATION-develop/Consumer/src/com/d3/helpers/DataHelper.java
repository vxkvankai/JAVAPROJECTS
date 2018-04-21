package com.d3.helpers;

import java.util.List;
import javax.annotation.Nullable;

public class DataHelper {

    private DataHelper() {
    }

    /**
     * Converts a List of a List of an Object to a 2d array of Objects e.g. List<List<Object>> -> Object[][]
     *
     * @param initialArrays initial List of Lists
     * @return Object[][]
     */
    @Nullable
    public static <T> Object[][] convertListTo2DArray(List<List<T>> initialArrays) {
        if (initialArrays == null || initialArrays.isEmpty()) {
            return null;
        }

        // oh god im sorry
        Object[][] newArray = new Object[initialArrays.size()][initialArrays.get(0).size()];

        for (List<T> outArray : initialArrays) {
            for (T innerArray : outArray) {
                newArray[initialArrays.indexOf(outArray)][outArray.indexOf(innerArray)] = innerArray;
            }
        }

        return newArray;
    }

    public static <T> Object[][] convert1DListTo2DArray(List<T> initialArrays) {
        if (initialArrays == null || initialArrays.isEmpty()) {
            return new Object[0][];
        }

        // oh god im sorry
        Object[][] newArray = new Object[initialArrays.size()][1];

        for (T object : initialArrays) {
            newArray[initialArrays.indexOf(object)][0] = object;
        }

        return newArray;
    }
}
