package ModelRas;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class Coder {
	public static final String KEY_SHA="SHA";
    public static final String KEY_MD5="MD5";
    /**
     * BASE64����
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception{
        return (new BASE64Decoder()).decodeBuffer(key);
    } 
    /**
     * BASE64����
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key)throws Exception{
        return (new BASE64Encoder()).encodeBuffer(key);
    }
    /**
     * MD5����
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data)throws Exception{
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }
     
    /**
     * SHA����
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data)throws Exception{
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }
    
    static public class RasGetPut {

    	public static final String KEY_ALGORTHM = "RSA";//
    	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    	public static final String PUBLIC_KEY = "RSAPublicKey";// ��Կ
    	public static final String PRIVATE_KEY = "RSAPrivateKey";// ˽Կ
    	
    	/**
         * ��ʼ����Կ
         * @return
         * @throws Exception
         */
    	public static Map<String, Object> initKey() throws Exception {
    		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
    		keyPairGenerator.initialize(2048*2);
    		KeyPair keyPair = keyPairGenerator.generateKeyPair();

    		// ��Կ
    		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    		// ˽Կ
    		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    		Map<String, Object> keyMap = new HashMap<String, Object>(2);
    		keyMap.put(PUBLIC_KEY, publicKey);
    		keyMap.put(PRIVATE_KEY, privateKey);

    		return keyMap;
    	}
    	/**
         * ȡ�ù�Կ����ת��ΪString����
         * @param keyMap
         * @return
         * @throws Exception
         */
        public static String getPublicKey(Map<String, Object> keyMap)throws Exception{
            Key key = (Key) keyMap.get(PUBLIC_KEY);  
            return encryptBASE64(key.getEncoded());     
        }
     
        /**
         * ȡ��˽Կ����ת��ΪString����
         * @param keyMap
         * @return
         * @throws Exception
         */
        public static String getPrivateKey(Map<String, Object> keyMap) throws Exception{
            Key key = (Key) keyMap.get(PRIVATE_KEY);  
            return encryptBASE64(key.getEncoded());     
        }
        /**
         * ��˽Կ����
         * @param data   ��������
         * @param key    ��Կ
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data,String key)throws Exception{
            //������Կ
            byte[] keyBytes = decryptBASE64(key);
            //ȡ˽Կ
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
             
            //�����ݼ���
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
             
            return cipher.doFinal(data);
        }
        /**
         * ��˽Կ���� * @param data    ��������
         * @param key    ��Կ
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] data,String key)throws Exception{
            //��˽Կ����
            byte[] keyBytes = decryptBASE64(key);
             
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
  
            //�����ݽ���
           Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
             
            return cipher.doFinal(data);
        }
        /**
         * �ù�Կ����
         * @param data   ��������
         * @param key    ��Կ
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data,String key)throws Exception{
            //�Թ�Կ����
            byte[] keyBytes = decryptBASE64(key);
            //ȡ��Կ
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
             
            //�����ݽ���
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
             
            return cipher.doFinal(data);
        }
        /**
         * �ù�Կ����
         * @param data   ��������
         * @param key    ��Կ
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] data,String key)throws Exception{
            //��˽Կ����
            byte[] keyBytes = decryptBASE64(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
             
            //�����ݽ���
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
             
            return cipher.doFinal(data);
        }
        /**
         * ��˽Կ����Ϣ��������ǩ��
         * @param data   //��������
         * @param privateKey //˽Կ
         * @return
         * @throws Exception
         */
        public static String sign(byte[] data,String privateKey)throws Exception{
            //����˽Կ
            byte[] keyBytes = decryptBASE64(privateKey);
            //����PKCS8EncodedKeySpec����
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            //ָ�������㷨
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            //ȡ˽Կ�׶���
            PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //��˽Կ����Ϣ��������ǩ��
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey2);
            signature.update(data);
             
            return encryptBASE64(signature.sign());
        }
        /**
         * У������ǩ��
         * @param data   ��������
         * @param publicKey  ��Կ
         * @param sign   ����ǩ��
         * @return
         * @throws Exception
         */
        public static boolean verify(byte[] data,String publicKey,String sign)throws Exception{
            //���ܹ�Կ
            byte[] keyBytes = decryptBASE64(publicKey);
            //����X509EncodedKeySpec����
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            //ָ�������㷨
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            //ȡ��Կ�׶���
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
             
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey2);
            signature.update(data);
            //��֤ǩ���Ƿ�����
            return signature.verify(decryptBASE64(sign));
        }
    }

}
