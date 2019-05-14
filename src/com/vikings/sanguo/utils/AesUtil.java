package com.vikings.sanguo.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

	private final static String algorithm = "AES/CFB/NoPadding";

	private final static String key_algorithm = "AES";

	public static SecretKeySpec generateKey() throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(key_algorithm);
		// 获取密匙生成器
		kg.init(128); // 初始化
		// DES算法必须是56位
		// DESede算法可以是112位或168位
		// AES算法可以是128、192、256位
		return (SecretKeySpec) kg.generateKey(); // 生成密匙，可用多种方法来保存密匙
	}

	public static SecretKeySpec loadKey(byte[] key) {
		return new SecretKeySpec(key, key_algorithm);
	}

	public static SecretKeySpec loadKey(byte[] key, int offset, int length) {
		return new SecretKeySpec(key, offset, length, key_algorithm);
	}

	public static byte[] encrypt(byte[] data, SecretKeySpec key,
			IvParameterSpec iv) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		return cipher.doFinal(data);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param rawKey
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
			throws Exception {
		return encrypt(data, new SecretKeySpec(key, key_algorithm),
				new IvParameterSpec(iv));
	}

	public static byte[] decrypt(byte[] data, SecretKeySpec key,
			IvParameterSpec iv) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(byte[] data, SecretKeySpec key,
			IvParameterSpec iv, int start) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return cipher.doFinal(data, start, data.length - start);
	}

	/**
	 * 解密
	 * 
	 * @param encrypted
	 * @param rawKey
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key, byte[] iv)
			throws Exception {
		return decrypt(data, new SecretKeySpec(key, key_algorithm),
				new IvParameterSpec(iv));
	}

}
