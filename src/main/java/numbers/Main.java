package numbers;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Pattern PARAMETERS_SEPARATOR = Pattern.compile("[, ]+");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printHelp();

        while (true) {
            System.out.printf("%nEnter a request: ");
            final var data = PARAMETERS_SEPARATOR
                    .split(scanner.nextLine().toUpperCase(), 3);
            System.out.println();
            if (data[0].isBlank()) {
                printHelp();
                continue;
            }
            if ("0".equals(data[0])) {
                break;
            }
            if (!NaturalNumber.isNatural(data[0])) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }
            final var start = new NaturalNumber(data[0]);
            if (data.length == 1) {
                System.out.print(Property.getCard(start));
                continue;
            }
            if (!NaturalNumber.isNatural(data[1])) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            }
            final var count = Long.parseLong(data[1]);
            if (data.length == 2) {
                printList(start, count, Collections.emptySet());
                continue;
            }

            final var names = PARAMETERS_SEPARATOR
                    .splitAsStream(data[2])
                    .collect(Collectors.toUnmodifiableSet());

            final var parameters = names.stream()
                    .map(name -> name.startsWith("-") ? name.substring(1) : name)
                    .collect(Collectors.toUnmodifiableSet());

            final var isAllParametersCorrect = Property.NAMES.containsAll(parameters);

            if (!isAllParametersCorrect) {
                final var wrongParameters = new HashSet<>(parameters);
                wrongParameters.removeAll(Property.NAMES);
                System.out.println(MessageFormat.format(
                        "The {1,choice,1#property|1<properties} {0} {1,choice,1#is|1<are} wrong.",
                        wrongParameters, wrongParameters.size())
                );
                System.out.println("Available properties: " + Arrays.toString(Property.values()));
                continue;
            }

            Property.MUTUALLY_EXCLUSIVE
                    .stream()
                    .filter(names::containsAll)
                    .findAny()
                    .ifPresentOrElse(
                            Main::mutuallyExclusiveError,
                            () -> printList(start, count, names)
                    );

        }
        System.out.println("Goodbye!");
    }

    private static void printList(NaturalNumber start, long count, Set<String> names) {
        final var query = names
                .stream()
                .map(name -> name.charAt(0) == '-'
                        ? Property.valueOf(name.substring(1)).negate()
                        : Property.valueOf(name))
                .reduce(number -> true, Predicate::and);

        Stream.iterate(start, NaturalNumber::next)
                .filter(query)
                .limit(count)
                .map(Property::allInLine)
                .forEach(System.out::println);
    }

    private static void mutuallyExclusiveError(final Set<String> exclusiveProperties) {
        System.out.println("The request contains mutually exclusive properties: " + exclusiveProperties);
        System.out.println("There are no numbers with all these properties at once.");
    }

    private static void printHelp() {
        System.out.println();
        System.out.println("Supported requests:");
        System.out.println("- one natural number to print the card;");
        System.out.println("- two natural numbers to print the list;:");
        System.out.println("  - a starting number for the list;");
        System.out.println("  - a count of numbers in the list;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("  - if a property name is preceded by a minus, that ");
        System.out.println("    property must not be present in the number.");
        System.out.println("- 0 for the exit. ");
    }
}