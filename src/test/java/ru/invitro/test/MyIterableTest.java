package ru.invitro.test;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class MyIterableTest {

    @Test
    public void testGeneralBehavior() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);

        MyIterable<Integer> iterable = MyIterableImpl.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        MyIterable<Integer> filtered = iterable.filter(new MyIterable.Predicate<Integer>() {
            @Override
            public boolean apply(Integer element) {
                counter.incrementAndGet();
                return element % 2 == 0;
            }
        });

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        MyIterable<Integer> transformed = filtered.transform(new MyIterable.Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer element) {
                counter.incrementAndGet();
                return element / 2;
            }
        });

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        int result = transformed.aggregate(0, new MyIterable.Aggregator<Integer, Integer>() {
            @Override
            public Integer apply(Integer aggregator, Integer element) {
                counter.incrementAndGet();
                return aggregator + element;
            }
        });

        Assert.assertEquals("Operation count is correct", 20, counter.get());

        Assert.assertEquals("Result is correct", 15, result);

    }

    @Test
    public void testLazyIterator() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);

        MyIterable<Integer> iterable = MyIterableImpl.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        MyIterable<Integer> filtered = iterable.filter(new MyIterable.Predicate<Integer>() {
            @Override
            public boolean apply(Integer element) {
                counter.incrementAndGet();
                return element % 2 == 0;
            }
        });

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        MyIterable<Integer> transformed = filtered.transform(new MyIterable.Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer element) {
                counter.incrementAndGet();
                return element / 2;
            }
        });

        Assert.assertEquals("Operation count is correct", 0, counter.get());

        Integer result = transformed.findFirst(new MyIterable.Predicate<Integer>() {
            @Override
            public boolean apply(Integer element) {
                counter.incrementAndGet();
                return element % 3 == 0;
            }
        });

        Assert.assertEquals("Operation count is correct", 12, counter.get());

        Assert.assertEquals("Result is correct", Integer.valueOf(3), result);

    }

    @Test
    public void testSort() {
        MyIterable<Integer> myIterable = MyIterableImpl.of(5,4,3,2,1,6);
        SortedSet<Integer> integers = myIterable.toSet(Comparator.naturalOrder());
        assertThat(integers, IsIterableContainingInOrder.contains(1,2,3,4,5,6));
    }

    @Test
    public void testToList() {
        MyIterable<Integer> myIterable = MyIterableImpl.of(0,2,1,3,5);
        List<Integer> list = myIterable.toList();
        System.out.println(list);
    }


}