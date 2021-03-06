package com.djrapitops.plan.utilities.analysis;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;
import java.util.OptionalDouble;
import java.util.stream.Stream;

/**
 * @author Rsl1122
 */
public class MathUtils {

    private static final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);

    /**
     * Constructor used to hide the public constructor
     */
    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets the average of a Stream of Integers.
     * If there are no components in the Stream, it will return 0.
     *
     * @param values The Stream of Integers.
     * @return The average
     */
    public static double averageInt(Stream<Integer> values) {
        OptionalDouble average = values.mapToInt(i -> i).average();

        return average.isPresent() ? average.getAsDouble() : 0;
    }

    /**
     * Gets the average of a Collection with Long as Entry.
     * If the collection is empty, it will return 0.
     *
     * @param values The Collection with Long as the Entry.
     * @return The average
     * @see #averageLong(Stream)
     */
    public static long averageLong(Collection<Long> values) {
        return averageLong(values.stream());
    }

    /**
     * Gets the average of a Stream of Longs.
     * If there are no components in the Stream, it will return 0.
     *
     * @param values The Stream of Longs.
     * @return The average
     * @see #averageLong(Collection)
     */
    public static long averageLong(Stream<Long> values) {
        OptionalDouble average = values.mapToLong(i -> i).average();

        return average.isPresent() ? (long) average.getAsDouble() : 0L;

    }

    /**
     * Gets the average of a Stream of Double.
     * If there are no components in the Stream, it will return 0.
     *
     * @param values The Stream of Double.
     * @return The average
     */
    public static double averageDouble(Stream<Double> values) {
        OptionalDouble average = values.mapToDouble(i -> i).average();

        return average.isPresent() ? average.getAsDouble() : 0;
    }

    /**
     * Calculates the average
     *
     * @param total The total summed amount of all Integers
     * @param size  The amount of all Integers that were summed
     * @return The average
     * @see #averageLong(long, long)
     */
    public static double average(int total, int size) {
        return (double) total / size;
    }

    /**
     * Calculates the average
     *
     * @param total The total summed amount of all Longs
     * @param size  The amount of all Longs that were summed
     * @return The average
     * @see #average(int, int)
     */
    public static long averageLong(long total, long size) {
        if (size == 0) {
            return -1L;
        }
        return total / size;
    }

    /**
     * Counts all Booleans that are true in the Stream of Booleans
     *
     * @param values The Stream of Booleans
     * @return The amount of Booleans that are true
     */
    public static long countTrueBoolean(Stream<Boolean> values) {
        return values.filter(value -> value).count();
    }

    /**
     * Sums all Integers in a Stream of Serializable
     *
     * @param values The Stream of Serializable
     * @return The sum
     * @see #sumLong(Stream)
     * @see #sumDouble(Stream)
     */
    public static int sumInt(Stream<Serializable> values) {
        return values
                .mapToInt(value -> (Integer) value)
                .sum();
    }

    /**
     * Sums all Longs in a Stream of Serializable
     *
     * @param values The Stream of Serializable
     * @return The sum
     * @see #sumInt(Stream)
     * @see #sumDouble(Stream)
     */
    public static long sumLong(Stream<Serializable> values) {
        return values
                .mapToLong(value -> (Long) value)
                .sum();
    }

    /**
     * Sums all Doubles in a Stream of Serializable
     *
     * @param values The Stream of Serializable
     * @return The sum
     * @see #sumLong(Stream)
     * @see #sumInt(Stream)
     */
    public static double sumDouble(Stream<Serializable> values) {
        return values
                .mapToDouble(value -> (Double) value)
                .sum();
    }

    /**
     * Rounds the double to a double with two digits at the end.
     * Output: #.##
     *
     * @param number The number that's rounded
     * @return The rounded number
     */
    public static double round(double number) {
        return Double.valueOf(decimalFormat.format(number));
    }

    public static double averageDouble(double amount, double size) {
        if (size == 0) {
            return -1;
        }
        return amount / size;
    }
}
