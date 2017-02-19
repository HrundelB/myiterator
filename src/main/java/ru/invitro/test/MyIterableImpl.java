package ru.invitro.test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sergey Afonin on 19.02.2017.
 */
public class MyIterableImpl<E> implements MyIterable<E> {

    private Stream<E> stream;

    public static <S> MyIterable<S> of(S... args) {
        List<S> list = Arrays.asList(args);
        return new MyIterableImpl<>(list.stream());
    }

    private MyIterableImpl(Stream<E> stream) {
        this.stream = stream;
    }

    public MyIterable<E> filter(@Nonnull final Predicate<E> predicate) {
        return new MyIterableImpl<E>(this.stream.filter(e -> predicate.apply(e)));
    }

    public <T> MyIterable<T> transform(@Nonnull Function<E, T> transformer) {
        Stream<T> newStream = this.stream.map(e -> transformer.apply(e));
        return new MyIterableImpl<T>(newStream);
    }

    public <T> T aggregate(@Nullable T initValue, @Nonnull Aggregator<E, T> aggregator) {
        T value = initValue;
        Iterator<E> iterator = this.stream.iterator();
        while (iterator.hasNext()) {
            E e = iterator.next();
            value = aggregator.apply(value, e);
        }
        return value;
    }

    public SortedSet<E> toSet(@Nonnull Comparator<E> comparator) {
        return this.stream.collect(Collectors.toCollection(()-> new TreeSet<E>(comparator)));
    }

    public List<E> toList() {
        return this.stream.collect(Collectors.toList());
    }

    @Nullable
    public E findFirst(@Nonnull Predicate<E> predicate) {
        return this.stream.filter(e -> predicate.apply(e)).findFirst().orElse(null);
    }

    @Override
    public Iterator<E> iterator() {
        return stream.iterator();
    }
}
