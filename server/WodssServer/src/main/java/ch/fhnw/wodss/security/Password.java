/*
 * The MIT License
 *
 * Copyright (c) 2015 Squeng AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.fhnw.wodss.security;

import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Copyright &copy; 2015 Squeng AG
 *
 * @author Paul
 */
public final class Password {
	private static final SecureRandom random = new SecureRandom();

	private final byte[] salt;
	private final int iterationCount;
	private final int keyLength;
	private final byte[] hash;
	
	public Password(char[] password) {
		salt = new byte[32];
		random.nextBytes(salt);
		iterationCount = 12000;
		keyLength = 512;
		hash = pbkdf2(password, salt, iterationCount, keyLength);
	}
	
	public Password(char[] password, byte[] salt) {
		this.salt = salt;
		iterationCount = 12000;
		keyLength = 512;
		hash = pbkdf2(password, salt, iterationCount, keyLength);
	}

	public Password(byte[] salt, int iterationCount, int keyLength, byte[] hash) {
		this.salt = salt;
		this.iterationCount = iterationCount;
		this.keyLength = keyLength;
		this.hash = hash;
	}

	// http://www.mhprofessional.com/product.php?isbn=0071835881&cat=112
	private byte[] pbkdf2(final char[] password, final byte[] s, final int ic, final int kl) {
		try {
			return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
					.generateSecret(new PBEKeySpec(password, s, ic, kl)).getEncoded();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			assert false;
			throw new RuntimeException(e);
		}
	}

	public byte[] getSalt() {
		return salt;
	}

	public int getIterationCount() {
		return iterationCount;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public byte[] getHash() {
		return hash;
	}

	public boolean matches(char[] password) {
		return Arrays.equals(hash, pbkdf2(password, salt, iterationCount, keyLength));
	}

	@Override
	public String toString() {
		return "Password[" + Arrays.toString(salt) + ", " + iterationCount + ", " + keyLength + ", "
				+ Arrays.toString(hash) + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		} else if (!(that instanceof Password)) {
			return false;
		} else {
			Password p = (Password) that;
			return Arrays.equals(this.salt, p.salt) && this.iterationCount == p.iterationCount
					&& this.keyLength == p.keyLength && Arrays.equals(this.hash, p.hash);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(Arrays.hashCode(salt), iterationCount, keyLength, Arrays.hashCode(hash));
	}
}