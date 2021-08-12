package numbers;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String AVAILABLE_PROPERTIES = "Available properties: " + Arrays.asList(Property.values());

    enum Property {
        EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD


    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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

            String[] strings = inString.split(" ");

            if (!isNumeric(strings[0])) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            } else if (isNumeric(strings[0]) && Long.parseLong(strings[0]) < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            } else if (strings[0].equals("0")) {
                System.out.println("Goodbye!");
                break;
            } else if (strings.length >= 2 && !isNumeric(strings[1])) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            } else if (strings.length >= 2 && isNumeric(strings[1]) && Integer.parseInt(strings[1]) < 0) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            } else if (strings.length >= 2 && strings[1].equals("0")) {
                System.out.println("Goodbye!");
                break;
            }

            if (strings.length == 1) {
                long num = Long.parseLong(strings[0]);
                printProprieties(num);
            } else if (strings.length == 2){
                long num = Long.parseLong(strings[0]);
                int x = Integer.parseInt(strings[1]);
                runAmazingNumbers(num, x);
            } else {
                long num = Long.parseLong(strings[0]);
                int x = Integer.parseInt(strings[1]);
                ArrayList<String> properties = new ArrayList<>();
                for (int i = 2; i < strings.length; i++) {
                    properties.add(strings[i].toLowerCase());
                }

                // See below how I restructured the code:
                // if someAssertionFailed {
                //     handleIt()
                //     continue or return
                // }
                // if someOtherAssertionFailed {
                //     handleIt()
                //     continue or return
                // }
                //
                // executeAction()
                //
                // Now the flow is like this - check something, if it's wrong execute the conditional path and end
                // the iteration (or return from the method). Check another thing and again - if it's wrong handle it
                // and exit from this scope. Finally, when you get to the end, you execute the action that required
                // all these prerequisites. To me it's more legible because the main flow of the method goes all the
                // way down to the end and all edge cases and errors are handled in short conditional branches.
                // See if it's maybe possible to refactor other long if/else chains.

                // I think it's better not to differentiate between 1 error or multiple errors and try to
                // handle this with '1 or more errors' approach. I'd also suggest not calling the same method
                // multiple times, it's better to call it once and save the output to a variable.
                final ArrayList<String> invalidProperties = findInvalidProperties(properties);
                if (!invalidProperties.isEmpty()) {
                    String invalidPropertiesString = invalidProperties.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.joining(", "));
                    System.out.println("One or more properties are wrong: [" + invalidPropertiesString + "]");
                    System.out.println(AVAILABLE_PROPERTIES);
                    continue;
                }

                // Now we don't need hasMutuallyExclusiveProperties - we find all mutually exclusive properties and
                // check if the list is empty or not - same effect but only 1 method is required.
                final ArrayList<String> mutuallyExclusiveProperties = findMutuallyExclusiveProperties(properties);
                if (!mutuallyExclusiveProperties.isEmpty()) {
                    String mutuallyExclusivePropertiesString = mutuallyExclusiveProperties.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.joining(", "));
                    System.out.println(
                        "The request contains mutually exclusive properties: [" + mutuallyExclusivePropertiesString + "]");
                    System.out.println("There are no numbers with these properties.");
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
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                // Nitpick: missing newlines
                "- two natural numbers and a property to search for;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }

    public static void runAmazingNumbers(long num, int x) {
        for (int i = 0; i < x; i++) {
            printProprietiesShort(num);
            num++;
        }
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

    public static void runAmazingNumbers(long num, int x, ArrayList<String> properties) {
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
        return (int) Math.sqrt(n+1) == Math.sqrt(n+1);
    }

    public static boolean isSquare(long n) {
        return (int) Math.sqrt(n) == Math.sqrt(n);
    }

    public static boolean isJumping(long n) {
        while(n != 0) {
            long digit1 = n % 10;
            n = n/10;
            if(n != 0) {
                long digit2 = n % 10;
                if(Math.abs(digit1 - digit2) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isHappy(long n) {
        long m = 0;
        int digit = 0;
        HashSet<Long> cycle = new HashSet<>();
        while(n != 1 && cycle.add(n)){
            m = 0;
            while(n > 0){
                digit = (int)(n % 10);
                m += digit * digit;
                n /= 10;
            }
            n = m;
        }
        return n == 1;
    }

    public static boolean isSad(long n) {
        long m = 0;
        int digit = 0;
        HashSet<Long> cycle = new HashSet<>();
        while(n != 1 && cycle.add(n)){
            m = 0;
            while(n > 0){
                digit = (int)(n % 10);
                m += digit * digit;
                n /= 10;
            }
            n = m;
        }
        return n != 1;
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean stringInPropertyList(String string) {
        if (string.startsWith("-")) {
            string = string.substring(1);
        }
        string = string.toUpperCase();
        for(Property property : Property.values()) {
            if (string.equals(property.name())) {
                return true;
            }
        }
        return false;
    }

    // The meaning of this method is not fully clear, I renamed it to make it clearer what the return value is.
    public static ArrayList<String> findInvalidProperties(ArrayList<String> properties) {
        ArrayList<String> wrongProperties = new ArrayList<>();
        for (String property : properties) {
            if(!stringInPropertyList(property)) {
                wrongProperties.add(property);
            }
        }
        return wrongProperties;
    }

    // The meaning of this method is not clear, I renamed it so that it's clearer what the return value represents.
    public static boolean hasMutuallyExclusiveProperties(ArrayList<String> properties) {
        // I'd add a simple comment: Check include/exclude of same property (e.g. odd -odd).
        for (String property :  properties) {
            if (!property.startsWith("-") && properties.contains("-" + property)) {
                return true;
            }
        }

        // Variables evenOrOdd, etc. are used in the return statement so let's keep them as close to the place where
        // they're used as possible (i.e. I've moved the for loop above).
        // I'd also add a simple comment: Check mutually exclusive properties (e.g. no number is both even and odd).
        boolean evenOrOdd = properties.contains("even") && properties.contains("odd") ||
                properties.contains("-even") && properties.contains("-odd");
        boolean duckOrSpy = properties.contains("duck") && properties.contains("spy");
        boolean sunnyOrSquare = properties.contains("sunny") && properties.contains("square");
        boolean happyOrSad = properties.contains("happy") && properties.contains("sad") ||
                properties.contains("-happy") && properties.contains("-sad");

        return evenOrOdd || duckOrSpy || sunnyOrSquare || happyOrSad;
    }

    // This method duplicates hasMutuallyExclusiveProperties. I think it would be better to remove
    // hasMutuallyExclusiveProperties and use getMutuallyExclusive only (see how I modified the usage).
    public static ArrayList<String> findMutuallyExclusiveProperties(ArrayList<String> properties) {

        ArrayList<String> contradictionList = new ArrayList<>();

        // Should it be an if-else chain? What if there are multiple contradictions (e.g. even odd duck spy)?
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
            for (String property :  properties) {
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
        StringBuilder stringBuilder = new StringBuilder();
        String formattedNum = String.format("%,16d", num);
        stringBuilder.append(formattedNum);
        stringBuilder.append(" is");

        // The approach with isNotFirstProperty is not good - that's too many if statements. I'd use a list instead to
        // hold all matched properties and then join all elements which will automatically solve the problem with the
        // comma. There's also no need to save the results of each method to a variable.
        final List<String> foundProperties = new ArrayList<>();
        if (isEven(num)) {
            foundProperties.add("even");
        }
        if (isBuzz(num)) {
            foundProperties.add("buzz");
        }
        // ...
        stringBuilder.append(String.join(", ", foundProperties));


        boolean isEven = isEven(num);
        boolean isBuzz = isBuzz(num);
        boolean isDuck = isDuck(num);
        boolean isPalindromic = isPalindromic(num);
        boolean isGapful = isGapful(num);
        boolean isSpy = isSpy(num);
        boolean isSunny = isSunny(num);
        boolean isSquare = isSquare(num);
        boolean isJumping = isJumping(num);
        boolean isHappy = isHappy(num);
        boolean isSad = isSad(num);
        boolean isNotFirstProperty = false;

        if (isBuzz) {
            stringBuilder.append(" buzz");
            isNotFirstProperty = true;
        }
        if (isDuck) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" duck");
            isNotFirstProperty = true;
        }
        if (isPalindromic) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" palindromic");
            isNotFirstProperty = true;
        }
        if (isGapful) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" gapful");
            isNotFirstProperty = true;
        }
        if (isSpy) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" spy");
            isNotFirstProperty = true;
        }
        if (isSunny) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" sunny");
            isNotFirstProperty = true;
        }
        if (isSquare) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" square");
            isNotFirstProperty = true;
        }
        if (isJumping) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" jumping");
            isNotFirstProperty = true;
        } if (isHappy) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" happy");
            isNotFirstProperty = true;
        } if (isSad) {
            if (isNotFirstProperty) {
                stringBuilder.append(",");
            }
            stringBuilder.append(" sad");
            isNotFirstProperty = true;
        }
        if (isNotFirstProperty) {
            stringBuilder.append(",");
        }
        stringBuilder.append(isEven ? " even" : " odd");

        System.out.println(stringBuilder);
    }
}
