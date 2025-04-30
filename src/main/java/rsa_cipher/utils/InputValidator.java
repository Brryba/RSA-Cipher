package rsa_cipher.utils;

public class InputValidator {
    public boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;

        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i <= Math.sqrt(n); i++) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    private int countGcd(int a, int b) {
        while (true) {
            if (a == 0) return b;
            if (b == 0) return a;

            if (a > b) a %= b;
            else if (b > a) b %= a;
        }
    }

    public boolean isClosedKeyCorrect(int closedKey, int rEuler) {
        if (closedKey <= 1 || closedKey >= rEuler) return false;

        return countGcd(rEuler, closedKey) == 1;
    }
}
