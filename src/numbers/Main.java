package numbers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {
    private static final String AVAILABLE_PROPERTIES = String.format("Available properties: %s",
            Arrays.asList(Property.values()));

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        printWelcome();
        printInstructions();

        System.out.println();

        while (true) {
            System.out.println("Enter a request: ");
            String inString = scanner.nextLine();

            if (inString.equals("")) {
                printInstructions();
                continue;
            }

            String[] strings = inString.split("\\s+", 3);

            if (!isNumeric(strings[0])) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            } else if (strings[0].equals("0")) {
                System.out.println("Goodbye!");
                break;
            } else if (strings.length >= 2 && !isNumeric(strings[1])) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            } else if (strings.length >= 2 && strings[1].equals("0")) {
                System.out.println("Goodbye!");
                break;
            }

            if (strings.length == 1) {
                long num = Long.parseLong(strings[0]);
                printProprieties(num);
            } else if (strings.length == 2) {
                long num = Long.parseLong(strings[0]);
                int x = Integer.parseInt(strings[1]);
                runAmazingNumbers(num, x);
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
                runAmazingNumbers(num, x, properties);
            }
        }
    }


    public static void printWelcome() {
        System.out.println("Welcome to Amazing Numbers!\n");
    }

    public static void printInstructions() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural java.numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive java.numbers are to be printed;\n" +
                "- two natural java.numbers and a property to search for;\n" +
                "- two natural java.numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in java.numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }

    public static void runAmazingNumbers(long num, int x) {
        LongStream.range(num, num + x).forEach(Main::printProprietiesShort);
    }

    public static boolean runNumber(long num, String property) {
        switch (property) {
            case "even":
                return isEven(num);
            case "odd":
                return isOdd(num);
            case "buzz":
                return isBuzz(num);
            case "duck":
                return isDuck(num);
            case "palindromic":
                return isPalindromic(num);
            case "gapful":
                return isGapful(num);
            case "spy":
                return isSpy(num);
            case "sunny":
                return isSunny(num);
            case "square":
                return isSquare(num);
            case "jumping":
                return isJumping(num);
            case "happy":
                return isHappy(num);
            case "sad":
                return isSad(num);
            default:
                return false;
        }
    }

    public static void runAmazingNumbers(long num, int x, List<String> properties) {
        boolean hasProperty;
        long checkRange = 0;
        while (checkRange < x) {
            hasProperty = true;
            for (String s : properties) {
                if (s.startsWith("-")) {
                    s = s.substring(1);
                    if (runNumber(num, s)) {
                        hasProperty = false;
                    }
                } else {
                    if (!runNumber(num, s)) {
                        hasProperty = false;
                    }
                }
            }
            if (hasProperty) {
                checkRange++;
                printProprietiesShort(num);
            }
            num++;
        }
    }

    public static boolean isEven(long n) {
        return n % 2 == 0;
    }

    public static boolean isOdd(long n) {
        return n % 2 != 0;
    }

    public static boolean isBuzz(long n) {
        return n % 7 == 0 || n % 10 == 7;
    }

    public static boolean isDuck(long n) {
        String stringNum = String.valueOf(n);
        return stringNum.contains("0");
    }

    public static boolean isPalindromic(long n) {
        long original = n;
        long reverse = 0;
        while (n > 0) {
            long digit = n % 10;
            reverse = (reverse * 10) + digit;
            n /= 10;
        }
        return original == reverse;
    }

    public static boolean isGapful(long n) {
        if (n / 100 > 0) {
            String[] strings = String.valueOf(n).split("");
            String concatFirstLast = strings[0] + strings[strings.length - 1];
            long second = Long.parseLong(concatFirstLast);
            return n % second == 0;
        } else {
            return false;
        }
    }

    public static boolean isSpy(long n) {
        long sum = 0;
        long product = 1;
        while (n > 0) {
            long digit = n % 10;
            sum += digit;
            product *= digit;
            n /= 10;
        }
        return sum == product;
    }

    public static boolean isSunny(long n) {
        return (int) Math.sqrt(n + 1) == Math.sqrt(n + 1);
    }

    public static boolean isSquare(long n) {
        return (int) Math.sqrt(n) == Math.sqrt(n);
    }

    public static boolean isJumping(long n) {
        while (n != 0) {
            long digit1 = n % 10;
            n = n / 10;
            if (n != 0) {
                long digit2 = n % 10;
                if (Math.abs(digit1 - digit2) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static long processHappySadNumber(long n) {
        long m;
        int digit;
        HashSet<Long> cycle = new HashSet<>();
        while (n != 1 && cycle.add(n)) {
            m = 0;
            while (n > 0) {
                digit = (int) (n % 10);
                m += digit * digit;
                n /= 10;
            }
            n = m;
        }
        return n;
    }

    public static boolean isHappy(long n) {
        long m = processHappySadNumber(n);
        return m == 1;
    }

    public static boolean isSad(long n) {
        long m = processHappySadNumber(n);
        return m != 1;
    }

    public static boolean isNumeric(String str) {
        return str.matches("(?!-)\\d+");
    }

    public static List<String> findInvalidProperties(final List<String> properties) {
        return properties.stream().filter(string -> {
            String finalString = string.startsWith("-") ?
                    string.replace("-", "").toUpperCase() : string.toUpperCase();
            return Arrays.stream(Property.values()).noneMatch(property -> property.name().equals(finalString));
        }).collect(Collectors.toList());
    }

    public static List<String> findMutuallyExclusiveProperties(final List<String> properties) {
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

    public static void printProprieties(long num) {
        System.out.printf("Properties of %,d %n", num);
        System.out.printf("%12s: %b %n", "buzz", isBuzz(num));
        System.out.printf("%12s: %b %n", "duck", isDuck(num));
        System.out.printf("%12s: %b %n", "palindromic", isPalindromic(num));
        System.out.printf("%12s: %b %n", "gapful", isGapful(num));
        System.out.printf("%12s: %b %n", "spy", isSpy(num));
        System.out.printf("%12s: %b %n", "square", isSquare(num));
        System.out.printf("%12s: %b %n", "sunny", isSunny(num));
        System.out.printf("%12s: %b %n", "jumping", isJumping(num));
        System.out.printf("%12s: %b %n", "happy", isHappy(num));
        System.out.printf("%12s: %b %n", "sad", isSad(num));
        System.out.printf("%12s: %b %n", "even", isEven(num));
        System.out.printf("%12s: %b %n", "odd", isOdd(num));
    }

    public static void printProprietiesShort(long num) {
        final List<String> foundProperties = new ArrayList<>();
        final StringBuilder stringBuilder = new StringBuilder();
        String formattedNum = String.format("%,16d", num);
        stringBuilder.append(formattedNum);
        stringBuilder.append(" is ");

        processProperties(num, foundProperties);

        foundProperties.add(isEven(num) ? "even" : "odd");
        stringBuilder.append(String.join(", ", foundProperties));

        System.out.println(stringBuilder);
    }

    private static void processProperties(final long num, final List<String> foundProperties) {
        if (isBuzz(num)) {
            foundProperties.add("buzz");
        }
        if (isDuck(num)) {
            foundProperties.add("duck");
        }
        if (isPalindromic(num)) {
            foundProperties.add("palindromic");
        }
        if (isGapful(num)) {
            foundProperties.add("gapful");
        }
        if (isSpy(num)) {
            foundProperties.add("spy");
        }
        if (isSunny(num)) {
            foundProperties.add("sunny");
        }
        if (isSquare(num)) {
            foundProperties.add("square");
        }
        if (isJumping(num)) {
            foundProperties.add("jumping");
        }
        if (isHappy(num)) {
            foundProperties.add("happy");
        }
        if (isSad(num)) {
            foundProperties.add("sad");
        }
    }
}
