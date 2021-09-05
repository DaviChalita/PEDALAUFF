package com.uff.pedalauff.comuns;

import java.util.Random;

public class Comuns {


    Random r = new Random();

    public String geraQrCodeAleatorio() {
        //https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
        String alphaNumericString = "0123456789abcdefghijklmnopqrstuvxyz";
        int n = 4;

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = r.nextInt(alphaNumericString.length());

            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
