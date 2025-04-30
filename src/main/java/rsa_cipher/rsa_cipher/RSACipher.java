package rsa_cipher.rsa_cipher;

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
}
