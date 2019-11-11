package com.i.hbapi.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteUtils {

    public static byte[] objToByte(Object obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            byte[] result = bos.toByteArray();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
                if (null != oos) {
                    oos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static Object byteToObj(byte[] bytes) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != ois) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public static String stringToHex(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String hexString = Integer.toHexString((int) c);
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static String hexToString(String hex) {
        if (TextUtils.isEmpty(hex)) {
            return null;
        }
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (0xff & Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16));
        }
        return new String(bytes);
    }


    public static byte[] stringToByte(String s) {
        return s.getBytes();
    }

    public static String byteToString(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] hexTobyte(String hex) {
        return hex.getBytes();
    }

    public static String byteTohex(byte[] bytes) {
        return new String(bytes);
    }


}
