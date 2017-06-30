package appmanager.com.appmanager;




import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.cydl.dlj.codec.binary.Base64;


/**
 * Created by huangzhebin on 2017/6/28.
 */

public class EncriptUtils {

    /** 安全的随机数源 */
    private static final SecureRandom RANDOM = new SecureRandom();
    /** 编码 */
    public static final Charset UTF8 = Charset.forName("UTF-8");
    /** Base64 编码 */
    private Base64 B64 = new Base64();
    /** 最大允许时间差异 */
    private static final long TIME_DIS = 1000 * 60 * 2;



    /** 地址 */
    private String wsUrl;
    /** 账号 */
    private String wsAcc;

    /** 加密算法 */
    private Cipher encryptCipher = null;
    /** 解密算法 */
    private Cipher decryptCipher = null;
    public EncriptUtils(String wsUrl, String wsAcc, String wsPwd) {
        this.wsUrl = wsUrl;
        this.wsAcc = wsAcc;
        try {
            SecretKeySpec key = aesKey(wsPwd);
            encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, RANDOM);
            decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, RANDOM);
        } catch (Exception e) {
            encryptCipher = null;
            decryptCipher = null;
            throw new RuntimeException("AES Cipher init failed.");
        }
    }
    /**
     * 数据加密
     *
     * @param str
     *            明文
     * @return 密文
     */
    public String aesEncryp(String str) {
        try {
            byte[] bytes = encryptCipher.doFinal(str.getBytes(UTF8));// 加密
            //return URLEncoder.encode(Base64.encode(bytes, Base64.DEFAULT), "utf-8");// B64 url safe
            //return Base64.encodeToString(bytes, Base64.DEFAULT);//Base64.encodeBase64URLSafeString(bytes);
            //return new String (encryptCipher.doFinal(Base64.decode(bytes, Base64.DEFAULT)));
            return B64.encodeBase64URLSafeString(bytes);
        } catch (Exception e) {
            System.out.println("AES加密失败, 密文：" + str + ", 错误：" + e.getMessage());
        }
        return null;
    }

    /**
     * 数据解密
     *
     * @param str
     *            密文
     * @return 明文
     */
    public String aesDecrypt(String str) {
        try {
            return new String(decryptCipher.doFinal(B64.decode(str)), UTF8);
            //return new String(decryptCipher.doFinal(Base64.decode(str, Base64.DEFAULT)), UTF8);// 解密
            //return new String(decryptCipher.doFinal(Base64.decode(URLDecoder.decode(str), Base64.DEFAULT)), UTF8);// 解密
        } catch (Exception e) {
            System.out.println("AES解密失败, 密文：" + str + ", 错误：" + e.getMessage());
        }
        return null;
    }
    /** AES密钥 */
    private SecretKeySpec aesKey(String key) {
        byte[] bs = key.getBytes();
        if (bs.length != 16) {
            bs = Arrays.copyOf(bs, 16);// 处理数组长度为16(不足16位,不空;超过16位取前16位)
        }
        return new SecretKeySpec(bs, "AES");
    }
}
