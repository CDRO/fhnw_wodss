package ch.fhnw.wodss;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import ch.fhnw.wodss.security.Password;

public class ExtractPasswordSalt {

	@Test
	public void extract() throws IOException, URISyntaxException {
		Password pass = new Password("password".toCharArray());
		System.out.println(Hex.encodeHexString(pass.getHash()));
		System.out.println(Hex.encodeHexString(pass.getSalt()));
	}

}
