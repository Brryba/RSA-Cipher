package rsa_cipher.rsa_cipher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RSACipher {
    public int countR(int p, int q) {
        return p * q;
    }

    public int countREuler(int p, int q) {
        return (p - 1) * (q - 1);
    }

    //Расширенный алгоритм Евклида
    public int countOpenKey(int rEuler, int closedKey) {
        int d0 = rEuler, d1 = closedKey, x0 = 1, x1 = 0, y0 = 0, y1 = 1;
        while (d1 > 1) {
            int q = d0 / d1;
            int d2 = d0 % d1;
            int x2 = x0 - q * x1;
            int y2 = y0 - q * y1;
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }

        if (y1 < 0) {
            return y1 + rEuler;
        }
        return y1;
    }

    private short fastModularExponentiation(short num, int exponent, int mod) {
        List<Short> numberTerms = new ArrayList<>();
        short term = 1;
        for (short i = 0; i < 16; i++) {
            if ((term & exponent) > 0) {
                numberTerms.add(i);
            }
            term <<= 1;
        }

        List<Short> modularMultipliers = new ArrayList<>();
        modularMultipliers.add((short) (num % mod));
        for (int i = 0; i < numberTerms.getLast(); i++) {
            modularMultipliers.add((short) (modularMultipliers.get(i) * modularMultipliers.get(i) % mod));
        }


        short result = (short) (modularMultipliers.get(numberTerms.getFirst()) % mod);
        for (int i = 1; i < numberTerms.size(); i++) {
            result = (short) ((result * modularMultipliers.get(numberTerms.get(i))) % mod);
        }

        return result;
    }



    public List<Short> encodeSymbols(List<Short> inputTextArray, int openKey, int r) {
        return inputTextArray.stream()
                .map(sym -> fastModularExponentiation(sym, openKey, r))
                .collect(Collectors.toList());
    }

    public List<Short> decodeSymbols(List<Short> inputTextArray, int closedKey, int r) {
        return inputTextArray.stream()
                .map(sym -> fastModularExponentiation(sym, closedKey, r))
                .collect(Collectors.toList());
    }
}
