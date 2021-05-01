package org.rabestro.numbers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Property implements Predicate<NaturalNumber> {
    EVEN(NaturalNumber::isEven),
    ODD(Predicate.not(NaturalNumber::isEven)),
    BUZZ(number -> number.dividedBy(7) || number.lastDigit() == 7),
    DUCK(number -> number.getDigits().indexOf('0') != 0),
    PALINDROMIC(number -> new StringBuilder(number.getDigits()).reverse().toString().equals(number.getDigits())),
    GAPFUL(number -> number.compareTo(BigInteger.valueOf(99)) > 0 &&
            number.dividedBy(number.firstDigit() * 10L + number.lastDigit())),
    SPY(number -> number.digits().sum() == number.digits().reduce(1, (a, b) -> a * b)),
    HARSHAD(number -> number.dividedBy(number.digits().sum())),
    ARMSTRONG(number -> {
        final var power = number.toString().length();
        final var sum = number.digits()
                .mapToObj(BigInteger::valueOf)
                .map(digit -> digit.pow(power))
                .reduce(BigInteger.ZERO, BigInteger::add);
        return number.equals(sum);
    }),
    JUMPING(n -> {
        final var digits = n.toString().toCharArray();
        for (int i = 0, previous = digits[i]; ++i < digits.length; previous = digits[i]) {
            int delta = digits[i] - previous;
            if (delta != 1 && delta != -1) {
                return false;
            }
        }
        return true;
    }),
    PANDIGITAL(nummber -> nummber.digits().distinct().count() == 10),
    DISARIUM(x -> {
        final var number = x.toString();
        return IntStream.range(0, number.length())
                .mapToObj(i-> BigInteger.valueOf(Character.getNumericValue(number.charAt(i))).pow(i+1))
                .reduce(BigInteger.ZERO, BigInteger::add).equals(x);
    }),
    AUTOMORPHIC(number -> number.multiply(number).toString().endsWith(number.toString()));

    public static final Set<Set<String>> MUTUALLY_EXCLUSIVE = Stream.concat(
            Arrays.stream(values()).map(Enum::name).map(name -> Set.of(name, "-" + name)),
            Stream.of(Set.of(EVEN.name(), ODD.name()), Set.of(DUCK.name(), SPY.name()), Set.of("-" + EVEN, "-" + ODD))
    ).collect(Collectors.toUnmodifiableSet());

    public static final Set<String> NAMES = Arrays.stream(values())
            .map(Enum::name).collect(Collectors.toUnmodifiableSet());
    private final Predicate<NaturalNumber> hasProperty;
    private final Pattern pattern = Pattern.compile(
            name() + "\\s*[:-]\\s*(?<value>true|false)",
            Pattern.CASE_INSENSITIVE
    );

    Property(Predicate<NaturalNumber> hasProperty) {
        this.hasProperty = hasProperty;
    }

    public static String getCard(NaturalNumber number) {
        return Arrays.stream(Property.values())
                .map(property -> String.format("%12s: %b%n", property.name().toLowerCase(), property.test(number)))
                .collect(Collectors.joining("", String.format("Properties of %,d%n", number), ""));
    }

    public static String allInLine(NaturalNumber number) {
        return Arrays.stream(Property.values())
                .filter(property -> property.test(number))
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", ", String.format("%,16d is ", number), ""));
    }

    @Override
    public boolean test(NaturalNumber number) {
        return hasProperty.test(number);
    }
}
