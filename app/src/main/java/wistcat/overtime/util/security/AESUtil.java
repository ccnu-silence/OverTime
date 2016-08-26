package wistcat.overtime.util.security;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import wistcat.overtime.util.Const;


/**
 * AES加密工具
 */
public class AESUtil {
    private final static String HEX = "0123456789ABCDEF";

    private static AESUtil INSTANCE;
    private Cipher enCipher, deCipher;


    private AESUtil() {
        try {
            final SecretKeySpec secKeySpec = new SecretKeySpec(getRawKey(), "AES");

            enCipher = Cipher.getInstance("AES/OFB/PKCS5Padding");
            enCipher.init(Cipher.ENCRYPT_MODE, secKeySpec);

            deCipher = Cipher.getInstance("AES/OFB/PKCS5Padding");
            deCipher.init(Cipher.DECRYPT_MODE, secKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AESUtil getInstance(){
        if(INSTANCE == null){
            synchronized (AESUtil.class) {
                if(INSTANCE == null){
                    INSTANCE = new AESUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 根据字符密密码生成密钥key
     * param seed	字符密码
     * @return	密钥
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public byte[] getRawKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secRandom;
        secRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        secRandom.setSeed(Const.AES_ENCRYPT_KEY.getBytes());
        keyGen.init(128, secRandom);	// 192 and 256 bits may not be available
        SecretKey key = keyGen.generateKey();
        return key.getEncoded();
    }


    /**
     * 加密
    */
    public String encrypt(String clear) {
        String ret = "";
        try {
            byte[] result = enCipher.doFinal(clear.getBytes());
            ret = toHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 解密
     */
    public String decrypt(String encrypted){
        String ret = "";
        try {
            byte[] result = deCipher.doFinal(toByte(encrypted));
            ret = new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 将加密后的乱码字符转化为16进制的数据
     */
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte b : buf) {
            appendHex(result, b);
        }
        return result.toString();
    }

    /**
     * 分离每个字节的前四位和后四位
     */
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    /**
     * 将转化后的16进制数据还原成加密后的字节码
     */
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++){
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }


}
