package com.github.modmoonka.aptagraph;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cluster {
    private final int radius;
    private final Sequence center;
    private final int id;
    private Map<Sequence, Integer> sequences;

    public Cluster(final int id, final int radius, final Sequence center) {
        this.id = id;
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
            final EditDistance distance = new LevenshteinDistance();
            this.sequences = sequences.parallelStream()
                    .map(sequence -> {
                        final int editDistance = distance.of(center.aptamer(), sequence.aptamer());
                        return new AbstractMap.SimpleEntry<>(sequence, editDistance);
                    })
                    .filter(entry -> entry.getValue() <= this.radius)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return this.sequences.entrySet().stream();
    }

    public int id() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cluster)) return false;
        return equals((Cluster) o);
    }

    private boolean equals(Cluster cluster) {
        if (radius != cluster.radius) return false;
        if (id != cluster.id) return false;
        if (!center.equals(cluster.center)) return false;
        return sequences.equals(cluster.sequences);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + radius;
        result = 31 * result + center.hashCode();
        result = 31 * result + sequences.hashCode();
        return result;
    }
}
