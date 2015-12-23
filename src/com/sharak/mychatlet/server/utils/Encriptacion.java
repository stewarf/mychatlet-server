package com.sharak.mychatlet.server.utils;

import java.util.Random;

public class Encriptacion {
    
    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {

        }
        return null;
    }

    public static String md5(String txt) {
        return getHash(txt, "MD5");
    }

    public static String sha1(String txt) {
        return getHash(txt, "SHA1");
    }

    public static String generarUserRandomNumeric(){
        Random rnd = new Random();
        Double numA, numB, numC, numD, numZ;
        String randomNumeric = "";

        numA = rnd.nextDouble();
        numB = rnd.nextDouble();
        numC = rnd.nextDouble();
        numD = rnd.nextDouble();

        numZ = (numA + numB + numC + numD) * rnd.nextDouble();

        randomNumeric = String.valueOf(numZ*1000);
        return randomNumeric;
    }
    
}
