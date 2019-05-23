package de.uhd.ifi.se.decision.management.eclipse.persistence;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoHelper {

	// Random String (for more security: shouldn't be hardcoded, maybe replace with
	// input from user in future)
	private static final String CRYPTO_KEY = "zjApYy0Mw9XdAEwU";

	public static String encrypt(String cleartext) {
		try {
			SecretKeySpec secKeySpec = new SecretKeySpec(CRYPTO_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secKeySpec);
			byte[] encrypted = cipher.doFinal(cleartext.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String decrypt(String encryptedString) {
		try {
			SecretKeySpec secKeySpec = new SecretKeySpec(CRYPTO_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secKeySpec);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedString));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}
}
