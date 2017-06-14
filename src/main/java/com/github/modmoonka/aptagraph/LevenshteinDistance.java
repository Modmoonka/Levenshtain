package com.github.modmoonka.aptagraph;

public class LevenshteinDistance implements EditDistance {
    @Override
    public int of(final CharSequence left, final CharSequence right) {
        int leftLength = left.length(); 
        int rightLength = right.length();
        int[] previousColumn;
        int[] currentColumn = new int[rightLength + 1];

        for (int i = 0; i <= rightLength; i++) currentColumn[i] = i;
        for (int i = 1; i <= leftLength; i++) {
            previousColumn = currentColumn;
            currentColumn = new int[rightLength + 1];
            for (int j = 0; j <= rightLength; j++) {
                if (j == 0) currentColumn[j] = i;
                else {
                    int dist = (left.charAt(i - 1) != right.charAt(j - 1)) ? 1 : 0;
                    if (currentColumn[j - 1] < previousColumn[j] && currentColumn[j - 1] < previousColumn[j - 1] + dist)
                        currentColumn[j] = currentColumn[j - 1] + 1;
                    else if (previousColumn[j] < previousColumn[j - 1] + dist)
                        currentColumn[j] = previousColumn[j] + 1;
                    else
                        currentColumn[j] = previousColumn[j - 1] + dist;
                }
            }
        }
        return currentColumn[rightLength];
    }
}

/**
 * poolP - большой файл
 * poolM - файл с меньшим количеством последовательностей
 * poolM разбиваем на к-меры, которые ищем в poolP
 * sequence | k-mer | k-mer
 */