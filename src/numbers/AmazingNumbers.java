package numbers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.LongStream;

public class AmazingNumbers {

    public void runAmazingNumbers(long num) {
        printProprieties(num);
    }

    public void runAmazingNumbers(long num, int x) {
        LongStream.range(num, num + x).forEach(this::printProprietiesShort);
    }

    public void runAmazingNumbers(long num, int x, List<String> properties) {
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

    private boolean runNumber(long num, String property) {
        return switch (property) {
            case "even" -> isEven(num);
            case "odd" -> isOdd(num);
            case "buzz" -> isBuzz(num);
            case "duck" -> isDuck(num);
            case "palindromic" -> isPalindromic(num);
            case "gapful" -> isGapful(num);
            case "spy" -> isSpy(num);
            case "sunny" -> isSunny(num);
            case "square" -> isSquare(num);
            case "jumping" -> isJumping(num);
            case "happy" -> isHappy(num);
            case "sad" -> isSad(num);
            default -> false;
        };
    }

    public boolean isEven(long n) {
        return n % 2 == 0;
    }

    public boolean isOdd(long n) {
        return n % 2 != 0;
    }

    public boolean isBuzz(long n) {
        return n % 7 == 0 || n % 10 == 7;
    }

    public boolean isDuck(long n) {
        String stringNum = String.valueOf(n);
        return stringNum.contains("0");
    }

    public boolean isPalindromic(long n) {
        long original = n;
        long reverse = 0;
        while (n > 0) {
            long digit = n % 10;
            reverse = (reverse * 10) + digit;
            n /= 10;
        }
        return original == reverse;
    }

    public boolean isGapful(long n) {
        if (n / 100 > 0) {
            String[] strings = String.valueOf(n).split("");
            String concatFirstLast = strings[0] + strings[strings.length - 1];
            long second = Long.parseLong(concatFirstLast);
            return n % second == 0;
        }

        return false;
    }

    public boolean isSpy(long n) {
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

    public boolean isSunny(long n) {
        return (int) Math.sqrt(n + 1) == Math.sqrt(n + 1);
    }

    public boolean isSquare(long n) {
        return (int) Math.sqrt(n) == Math.sqrt(n);
    }

    public boolean isJumping(long n) {
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

    private long processHappySadNumber(long n) {
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

    public boolean isHappy(long n) {
        long m = processHappySadNumber(n);
        return m == 1;
    }

    public boolean isSad(long n) {
        long m = processHappySadNumber(n);
        return m != 1;
    }


    private void printProprietiesShort(long num) {
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

    private void printProprieties(long num) {
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

    private void processProperties(final long num, final List<String> foundProperties) {
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
