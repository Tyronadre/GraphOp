package de.henrik;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomSteam {

    public static void main(String[] args) {
        var s = createPermutation(List.of(List.of(0, 1, 2), List.of(2,3,4),List.of(5,6,7)));
        s.limit(1000).forEach(System.out::println);
    }

    private static Stream<List<Integer>> createPermutation(
            List<List<Integer>> list) {
        return Stream.generate(new Supplier<>() {
            private final List<Iterator<Integer>> iterators = list.stream().map(List::iterator).collect(Collectors.toList());

            private final List<Integer> current = new ArrayList<>();

            { //anonymous initializer - like a constructor
                current.add(null);
                for (int i = 1; i < iterators.size(); i++) {
                    current.add(iterators.get(i).next());
                }
            }

            @Override
            public List<Integer> get() {

                if (iterators.get(0).hasNext()) {
                    current.set(0, iterators.get(0).next());
                    return new ArrayList<>(current);
                }

                iterators.set(0, list.get(0).iterator());
                current.set(0, iterators.get(0).next());

                for (int i = 1; i < iterators.size(); i++) {
                    if (iterators.get(i).hasNext()) {
                        current.set(i, iterators.get(i).next());
                        return new ArrayList<>(current);
                    }
                    iterators.set(i, list.get(i).iterator());
                }

                return null;
            }
        });
    }
}
