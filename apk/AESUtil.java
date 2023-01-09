import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String CipherMode = "AES/CBC/PKCS7Padding";

    private static IvParameterSpec createIV(String iv) throws UnsupportedEncodingException {
        if(iv == null) {
            iv = "";
        }

        StringBuffer sb = new StringBuffer(16);
        sb.append(iv);
        while(sb.length() < 16) {
            sb.append(" ");
        }

        return new IvParameterSpec(sb.substring(0, 16).getBytes("UTF-8"));
    }

    private static SecretKeySpec createKey(String password) throws UnsupportedEncodingException {
        if(password == null) {
            password = "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(password);
        while(sb.length() < 0x20) {
            sb.append(" ");
        }

        return new SecretKeySpec(sb.substring(0, 0x20).getBytes("UTF-8"), "AES");
    }

    public static byte[] decryptBase642Byte(String content, String password, String iv) {
        try {
            return AESUtils.decryptByte2Byte(Base64.decode(content), password, iv);
        }
        catch(Exception e) {
            e.printStackTrace();
            return AESUtils.decryptByte2Byte(null, password, iv);
        }
    }

    public static String decryptBase642String(String content, String password, String iv) {
        return AESUtils.decryptByte2String(Base64.decode(content), password, iv);
    }

    public static byte[] decryptByte2Byte(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = AESUtils.createKey(password);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(2, key, AESUtils.createIV(iv));
            return cipher.doFinal(content);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptByte2String(byte[] content, String password, String iv) {
        return new String(AESUtils.decryptByte2Byte(content, password, iv));
    }

    public static byte[] decryptString2Byte(String content, String password, String iv) throws UnsupportedEncodingException {
        return AESUtils.decryptByte2Byte(content.getBytes("UTF-8"), password, iv);
    }

    public static String encryptByte2Base64(byte[] content, String password, String iv) {
        return new String(Base64.encode(AESUtils.encryptByte2Byte(content, password, iv)));
    }

    public static byte[] encryptByte2Byte(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = AESUtils.createKey(password);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(1, key, AESUtils.createIV(iv));
            return cipher.doFinal(content);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptByte2String(byte[] content, String password, String iv) {
        return new String(AESUtils.encryptByte2Byte(content, password, iv));
    }

    public static String encryptString2Base64(String content, String password, String iv) throws UnsupportedEncodingException {
        return new String(Base64.encode(AESUtils.encryptByte2Byte(content.getBytes("UTF-8"), password, iv)));
    }

    public static byte[] encryptString2Byte(String content, String password, String iv) throws UnsupportedEncodingException {
        return AESUtils.encryptByte2Byte(content.getBytes("UTF-8"), password, iv);
    }

    public static String encryptString2String(String content, String password, String iv) throws UnsupportedEncodingException {
        return new String(AESUtils.encryptByte2Byte(content.getBytes("UTF-8"), password, iv));
    }

    public static void main(String[] args) {
        String res = AESUtils.decryptBase642String("u0uYYmh4yRpPIT/zSP7EL/MOCliVoVLt3gHcrXDymLc=",
                "n1book", "123456");
        System.out.println("Result flag is: " + res);
    }
}


