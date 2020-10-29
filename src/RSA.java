import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RSA {
    // Declare the list for storing numbers
    public static ArrayList<Integer> simpleOddNumbers = new ArrayList<>();

    /*
    doGenerationOfSimpleOddRandomNumbers() - create list of simple numbers and choose two from it
     */
    protected static String doGenerationOfSimpleOddRandomNumbers() {
        // Full empty list simpleOddNumbers with simple and odd numbers
        doSieveOfEratosthenes(2, 30);

        // Delete the one even number
        simpleOddNumbers.remove(0);

        // Select two random simple numbers
        Random random = new Random();
        int p = simpleOddNumbers.get(random.nextInt(simpleOddNumbers.size()));
        int q = simpleOddNumbers.get(random.nextInt(simpleOddNumbers.size()));

        /*
        If last element in list is less than multiplication of these two random simple numbers,
        than extent the list
        */
        if (simpleOddNumbers.get(simpleOddNumbers.size() - 1) < (p - 1) * (q - 1)) {
            doSieveOfEratosthenes(51, (p - 1) * (q - 1));
        }

        return p + " " + q;
    }

    /*
    doSieveOfEratosthenes() - accomplish sieve of Eratosthenes algorithm
    */
    private static void doSieveOfEratosthenes(int min, int max) {
        // Full list with numbers from min to max
        for (int i = min; i <= max; i++) {
            simpleOddNumbers.add(i);
        }

        // Delete all numbers that are not simple but composite
        for (int i = 0; i <= simpleOddNumbers.size() + 2; i++) {
            for (int j = i + 1; j < simpleOddNumbers.size(); j++) {
                if (simpleOddNumbers.get(j) % simpleOddNumbers.get(i) == 0) {
                    simpleOddNumbers.remove(j);
                }
            }
        }
    }

    /*
    findOpenExponent() - pick up all numbers that can be open exponent for resulting Euler function
    */
    protected static int findOpenExponent(int eulerFunction) {
        // Empty list for storing open exponents
        ArrayList<Integer> exponents = new ArrayList<>();

        simpleOddNumbers.add(0, 2);

        /*
        Check conditions of open exponent:
        1. Simple
        2. Less than value of Euler function
        3. Mutually simple to value of Euler function
        */
        for (int i = 0; i < simpleOddNumbers.size() && simpleOddNumbers.get(i) < eulerFunction; i++) {
            // Divisors for the simple number and supposed exponent
            Collection<Integer> divisorsForSimpleNumber = findDivisors(eulerFunction);
            Collection<Integer> divisorsForSupposedExponent = findDivisors(simpleOddNumbers.get(i));

            // If both numbers do not have common divisors, store the supposed exponent
            divisorsForSupposedExponent.removeAll(divisorsForSimpleNumber);
            if (!divisorsForSupposedExponent.isEmpty()) {
                exponents.add(simpleOddNumbers.get(i));
            }
        }
        simpleOddNumbers.remove(0);

        // Select random number from the list
        Random random = new Random();
        return exponents.get(random.nextInt(exponents.size()));
    }

    /*
    findDivisors() - count all divisors for the number
    */
    private static ArrayList<Integer> findDivisors(int number) {
        // Empty list for storing divisors
        ArrayList<Integer> divisors = new ArrayList<>();

        // Check reminders of division
        for (double i = 2.0; i <= number; i++) {
            if (number % i == 0) {
                divisors.add((int) i);
            }
        }

        return divisors;
    }

    /*
    calculateExponentialReverseNumberOnModule() - pick up numbers
    that is exponential reverse numbers on module of Euler function value
    */
    protected static int calculateExponentialReverseNumberOnModule(int exponent, int eulerFunction, int module) {
        // Empty list for storing numbers
        ArrayList<Integer> exponentialReverseNumbersOnModule = new ArrayList<>();

        // Pick up numbers 'd' that satisfy the condition : (d * exponent) mod eulerFunction = 1
        for (int d = 1; d < module; d++) {
            if ((d * exponent) % eulerFunction == 1) {
                exponentialReverseNumbersOnModule.add(d);
            }
        }

        // Select random number from the list
        Random random = new Random();
        return exponentialReverseNumbersOnModule.get(random.nextInt(exponentialReverseNumbersOnModule.size()))/*exponentialReverseNumbersOnModule.get(0)*/;
    }
}