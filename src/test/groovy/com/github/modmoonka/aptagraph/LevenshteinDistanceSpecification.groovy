package com.github.modmoonka.aptagraph

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
class LevenshteinDistanceSpecification extends Specification {
    LevenshteinDistance levenshteinDistance

    def setup() {
        levenshteinDistance = new LevenshteinDistance()
    }

    @Unroll
    def "Data set"() {
        expect:
        levenshteinDistance.of(left, right) == distance

        where:
        left          | right   || distance
        ""            | ""      || 0
        ""            | "a"     || 1
        "apple"       | ""      || 5
        "application" | "apple" || 7
    }
}
