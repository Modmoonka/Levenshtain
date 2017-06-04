package com.github.modmoonka.aptagraph;

import java.util.*;
import java.util.stream.Stream;

public class Cluster {
    private final int radius;
    private final Sequence center;
    private Map<Sequence, Integer> sequences;
    private final int id;

    public Cluster(final int id, final int radius, final Sequence center) {
        this. id = id;
        this.radius = radius;
        this.center = center;
    }

    /**
     * Кеширует резульат в свйоство {@code sequences}
     * (ленивое выполеннеи, не расчитывает до первого обращения)
     *
     * @return все поледовательности входящие в этот кластер
     */
    public Stream<Map.Entry<Sequence, Integer>> sequences(final Set<Sequence> sequences) {
        if (Objects.isNull(this.sequences)) {
            this.sequences = new HashMap<>();
            final EditDistance distance = new LevenshteinDistance();
            this.sequences.put(center, 0);
            sequences.forEach(sequence -> {
                final int editDistance = distance.of(center.aptamer(), sequence.aptamer());
                if (editDistance < radius) {
                    this.sequences.put(sequence, editDistance);
                }
            });
        }
        return this.sequences.entrySet().stream();
    }

    public int id() {
        return this.id;
    }
}
