package numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RunUI {
    private final String AVAILABLE_PROPERTIES = String.format("Available properties: %s",
            Arrays.asList(Property.values()));

    private final Scanner scanner;

    private final AmazingNumbers amazingNumbers;

    public RunUI() {
        scanner = new Scanner(System.in);
        amazingNumbers = new AmazingNumbers();
    }

    public void run() {
        printWelcome();
        printInstructions();

        System.out.println();

        while (true) {
            System.out.println("Enter a request: ");
            String inString = scanner.nextLine();

            if (inString.isEmpty()) {
                printInstructions();
                continue;
            }

            String[] strings = inString.split("\\s+", 3);

            if (isNotNumeric(strings[0])) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            } else if (strings[0].equals("0")) {
                System.out.println("Goodbye!");
                break;
            } else if (strings.length >= 2 && isNotNumeric(strings[1])) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            } else if (strings.length >= 2 && strings[1].equals("0")) {
                System.out.println("Goodbye!");
                break;
            }

            if (strings.length == 1) {
                long num = Long.parseLong(strings[0]);
                amazingNumbers.runAmazingNumbers(num);
            } else if (strings.length == 2) {
                long num = Long.parseLong(strings[0]);
                int x = Integer.parseInt(strings[1]);
                amazingNumbers.runAmazingNumbers(num, x);
            } else {
                long num = Long.parseLong(strings[0]);
                int x = Integer.parseInt(strings[1]);
                final List<String> properties = Arrays.stream(strings[2].split("\\s+"))
                        .map(String::toLowerCase).collect(Collectors.toList());

                final List<String> invalidProperties = findInvalidProperties(properties);
                if (!invalidProperties.isEmpty()) {
                    String invalidPropertiesString = invalidProperties.stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.joining(", "));
                    System.out.println("One or more properties are wrong: [" + invalidPropertiesString + "]");
                    System.out.println(AVAILABLE_PROPERTIES);
                    continue;
                }
                final List<String> mutuallyExclusiveProperties = findMutuallyExclusiveProperties(properties);
                if (!mutuallyExclusiveProperties.isEmpty()) {
                    String mutuallyExclusivePropertiesString = mutuallyExclusiveProperties.stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.joining(", "));
                    System.out.println("The request contains mutually exclusive properties: ["
                            + mutuallyExclusivePropertiesString + "]");
                    System.out.println("There are no java.numbers with these properties.");
                    continue;
                }
                amazingNumbers.runAmazingNumbers(num, x, properties);
            }
        }
        scanner.close();
    }

    private static void printWelcome() {
        System.out.println("Welcome to Amazing Numbers!\n");
    }

    private static void printInstructions() {
        System.out.println("""
                Supported requests:
                - enter a natural number to know its properties;
                - enter two natural java.numbers to obtain the properties of the list:
                  * the first parameter represents a starting number;
                  * the second parameter shows how many consecutive java.numbers are to be printed;
                - two natural java.numbers and a property to search for;
                - two natural java.numbers and properties to search for;
                - a property preceded by minus must not be present in java.numbers;
                - separate the parameters with one space;
                - enter 0 to exit.
                """);
    }

    private static boolean isNotNumeric(String str) {
        return !str.matches("(?!-)\\d+");
    }

    private static List<String> findInvalidProperties(final List<String> properties) {
        return properties.stream().filter(string -> {
            String finalString = string.startsWith("-") ?
                    string.replace("-", "").toUpperCase() : string.toUpperCase();
            return Arrays.stream(Property.values()).noneMatch(property -> property.name().equals(finalString));
        }).collect(Collectors.toList());
    }

    private static List<String> findMutuallyExclusiveProperties(final List<String> properties) {
        final List<String> contradictionList = new ArrayList<>();

        if (properties.contains("even") && properties.contains("odd")) {
            contradictionList.add("EVEN");
            contradictionList.add("ODD");
        } else if (properties.contains("duck") && properties.contains("spy")) {
            contradictionList.add("DUCK");
            contradictionList.add("SPY");
        } else if (properties.contains("square") && properties.contains("sunny")) {
            contradictionList.add("SQUARE");
            contradictionList.add("SUNNY");
        } else if (properties.contains("happy") && properties.contains("sad")) {
            contradictionList.add("HAPPY");
            contradictionList.add("SAD");
        } else if (properties.contains("-even") && properties.contains("-odd")) {
            contradictionList.add("-EVEN");
            contradictionList.add("-ODD");
        } else if (properties.contains("-happy") && properties.contains("-sad")) {
            contradictionList.add("-HAPPY");
            contradictionList.add("-SAD");
        } else {
            for (String property : properties) {
                if (!property.startsWith("-") && properties.contains("-" + property)) {
                    contradictionList.add(property);
                    contradictionList.add("-" + property);
                }
            }
        }

        return contradictionList;
    }
}
