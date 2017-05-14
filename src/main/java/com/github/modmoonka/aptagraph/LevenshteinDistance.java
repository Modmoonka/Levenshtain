package com.github.modmoonka.aptagraph;

public class LevenshteinDistance implements EditDistance {
    @Override
    public int of(final CharSequence left, final CharSequence right) {
        int m = left.length();
        int n = right.length();
        int[] Dlina1;
        int[] Dlina2 = new int [n + 1];

        for (int i = 0; i <= n; i++) Dlina2[i] = i;
            for (int i =1; i <= m; i++) {
                Dlina1 = Dlina2;
                Dlina2 = new int[n + 1];
                for (int j = 0; j <= n; j++) {
                    if (j == 0) Dlina2[j] = i;
                    else {
                        int dist = (left.charAt(i - 1) != right.charAt(j - 1)) ? 1 : 0;
                        if(Dlina2[j-1] < Dlina1[j] && Dlina2[j-1] < Dlina1[j-1] + dist)
                            Dlina2[j] = Dlina2[j - 1] + 1;
                        else
                            if (Dlina1[j] < Dlina1[j -1] + dist)
                                Dlina2[j] = Dlina1[j] + 1;
                        else
                            Dlina2[j] = Dlina1[j - 1] + dist;
                    }
                }
            }
              return Dlina2[n];
    }
}
