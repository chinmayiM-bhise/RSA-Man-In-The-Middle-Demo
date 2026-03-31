import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RSAUtils {

    public record RSAKeyPair(BigInteger publicKeyE, BigInteger privateKeyD, BigInteger modulusN) {}

    /**
     * Generates an RSA key pair using specified prime numbers p and q, and public exponent e.
     * This method is primarily for demonstration purposes where specific weak primes are used.
     * @param p A prime number.
     * @param q Another prime number, distinct from p.
     * @param e The public exponent, typically a small prime like 3, 17, or 65537.
     * @return An RSAKeyPair containing the public exponent, private exponent, and modulus.
     * @throws IllegalArgumentException if p or q are not prime, or if e is not coprime to (p-1)*(q-1).
     */
    public static RSAKeyPair generateKeyPair(int p, int q, int e) {
        BigInteger bigP = BigInteger.valueOf(p);
        BigInteger bigQ = BigInteger.valueOf(q);

        BigInteger n = bigP.multiply(bigQ);
        BigInteger phiN = (bigP.subtract(BigInteger.ONE)).multiply(bigQ.subtract(BigInteger.ONE));

        if (!bigP.isProbablePrime(10) || !bigQ.isProbablePrime(10)) {
            throw new IllegalArgumentException("p and q must be prime numbers.");
        }
        if (bigP.equals(bigQ)) {
            throw new IllegalArgumentException("p and q must be distinct prime numbers.");
        }
        BigInteger bigE = BigInteger.valueOf(e);
        if (bigE.compareTo(BigInteger.ONE) <= 0 || bigE.compareTo(phiN) >= 0 || !bigE.gcd(phiN).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("e must be coprime to (p-1)*(q-1) and 1 < e < phiN.");
        }

        BigInteger d = bigE.modInverse(phiN);

        return new RSAKeyPair(bigE, d, n);
    }

    /**
     * Generates a "weak" RSA key pair by finding two small random prime numbers
     * within the specified range [minPrime, maxPrime].
     *
     * @param minPrime The minimum value for the prime numbers (inclusive).
     * @param maxPrime The maximum value for the prime numbers (inclusive).
     * @param e The public exponent, typically a small prime like 3, 17, or 65537.
     * @return An RSAKeyPair containing the public exponent, private exponent, and modulus.
     * @throws IllegalArgumentException if no two distinct primes can be found in the given range,
     *                                  or if e is not coprime to (p-1)*(q-1).
     */
    public static RSAKeyPair generateWeakKeyPair(int minPrime, int maxPrime, int e) {
        List<Integer> primes = new ArrayList<>();
        for (int i = minPrime; i <= maxPrime; i++) {
            if (isProbablePrime(i, 10)) { // 10 iterations for a reasonable probability
                primes.add(i);
            }
        }

        if (primes.size() < 2) {
            throw new IllegalArgumentException("Not enough distinct prime numbers found in the specified range.");
        }

        Random rand = new Random();
        int p, q;
        do {
            p = primes.get(rand.nextInt(primes.size()));
            q = primes.get(rand.nextInt(primes.size()));
        } while (p == q); // Ensure p and q are distinct

        return generateKeyPair(p, q, e);
    }

    /**
     * Checks if a given integer is a probable prime using Miller-Rabin test.
     * @param number The integer to check.
     * @param iterations The number of iterations for the Miller-Rabin test. Higher means more certainty.
     * @return true if the number is probably prime, false otherwise.
     */
    private static boolean isProbablePrime(int number, int iterations) {
        if (number <= 1) return false;
        if (number == 2 || number == 3) return true;
        if (number % 2 == 0) return false;
        return BigInteger.valueOf(number).isProbablePrime(iterations);
    }

    public static String encryptMessage(String message, BigInteger e, BigInteger n) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int charValue = (int) message.charAt(i);
            BigInteger ch = BigInteger.valueOf(charValue);
            BigInteger encryptedCh = ch.modPow(e, n);
            encryptedMessage.append(encryptedCh).append(" ");
        }
        return encryptedMessage.toString().trim();
    }

    public static String decryptMessage(String encryptedMessage, BigInteger d, BigInteger n) {
        StringBuilder decryptedMessage = new StringBuilder();
        String[] encryptedValues = encryptedMessage.split(" ");
        for (String cipher : encryptedValues) {
            BigInteger encryptedCh = new BigInteger(cipher);
            BigInteger decryptedCh = encryptedCh.modPow(d, n);
            decryptedMessage.append((char) decryptedCh.intValue());
        }
        return decryptedMessage.toString();
    }
}
