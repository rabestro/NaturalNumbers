package org.rabestro.numbers;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class NaturalNumber extends BigInteger {

    public NaturalNumber(String value) {
        super(value);
    }

    public static boolean isNatural(String value) {
        return value.chars().allMatch(Character::isDigit);
    }

    public NaturalNumber next() {
        return new NaturalNumber(this.add(ONE).toString());
    }

    public boolean isEven() {
        return mod(TWO).equals(ZERO);
    }

    public int lastDigit() {
        return Character.getNumericValue(toString().charAt(toString().length() - 1));
    }

    public int firstDigit() {
        return Character.getNumericValue(toString().charAt(0));
    }

    public IntStream digits() {
        return toString().chars().map(Character::getNumericValue);
    }

    public boolean dividedBy(long number) {
        return mod(BigInteger.valueOf(number)).equals(ZERO);
    }

    public String getDigits() {
        return toString();
    }

    public NaturalNumber reverse() {
        return new NaturalNumber(new StringBuilder(toString()).reverse().toString());
    }

    public NaturalNumber pow2() {
        return new NaturalNumber(pow(2).toString());
    }
}
