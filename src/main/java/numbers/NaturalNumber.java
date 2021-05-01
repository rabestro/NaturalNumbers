package numbers;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class NaturalNumber extends BigInteger {

    public NaturalNumber(String value) {
        super(value);
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

    public boolean hasDigit(int digit) {
        return toString().indexOf(Character.forDigit(digit, 10)) != -1;
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

    public static boolean isNatural(String value) {
        return value.chars().allMatch(Character::isDigit);
    }

    public static NaturalNumber next(NaturalNumber index) {
        return (NaturalNumber) index.add(ONE);
    }
}
