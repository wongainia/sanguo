package com.vikings.sanguo.utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaUtil {

	public final static int max_encrypt_length = 117;
	public final static int decrypt_length = 117;

	private final static String key_algorithm = "RSA";

	private final static String algorithm = "RSA/ECB/PKCS1Padding";

	public static RSAPrivateKey loadPrivateKey(byte[] buf)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance(key_algorithm);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(buf);
		return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
	}

	public static RSAPublicKey loadPublicKey(byte[] buf)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance(key_algorithm);
		EncodedKeySpec spec = new X509EncodedKeySpec(buf);
		return (RSAPublicKey) keyFactory.generatePublic(spec);
	}

	static public byte[] encrypt(Key publicKey, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int index = 0;
		while (index < data.length) {
			out.write(cipher.doFinal(data, index,
					Math.min(max_encrypt_length, data.length - index)));
			index += max_encrypt_length;
		}
		return out.toByteArray();
	}

	static public byte[] decrypt(Key privateKey, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int index = 0;
		while (index < data.length) {
			out.write(cipher.doFinal(data, index,
					Math.min(decrypt_length, data.length - index)));
			index += decrypt_length;
		}
		return out.toByteArray();
	}

	static public byte[] decrypt(Key privateKey, byte[] raw, int offset,
			int length) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(raw, offset, length);
	}

}
