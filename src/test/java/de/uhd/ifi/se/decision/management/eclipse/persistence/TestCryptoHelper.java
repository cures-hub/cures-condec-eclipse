package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCryptoHelper {

	@Test
	public void test() {
		String input1 = "Teststring1";
		String encrypt1 = CryptoHelper.encrypt(input1);
		String decrypt1 = CryptoHelper.decrypt(encrypt1);
		String input2 = "Teststring2";
		String encrypt2 = CryptoHelper.encrypt(input2);
		String decrypt2 = CryptoHelper.decrypt(encrypt2);
		// Make sure, that the inputs are different
		assertNotEquals(input1, input2);
		// Make sure, that two given Strings are not encrypted to the same value
		assertNotEquals(encrypt1, encrypt2);
		assertNotEquals(input1, encrypt1);
		// Make sure, that the decryption returns the same value, which was passed to
		// the encryption
		assertEquals(input1, decrypt1);
		assertEquals(input2, decrypt2);
		System.out.println("String 1: " + input1);
		System.out.println("String 1 (encrypted): " + encrypt1);
		System.out.println("String 1 (decrypted): " + decrypt1);
		System.out.println("String 2: " + input2);
		System.out.println("String 2 (encrypted): " + encrypt2);
		System.out.println("String 2 (decrypted): " + decrypt2);
	}
 
}
