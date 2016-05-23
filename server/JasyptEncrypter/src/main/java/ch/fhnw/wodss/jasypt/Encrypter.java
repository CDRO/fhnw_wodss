package ch.fhnw.wodss.jasypt;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class Encrypter {

	public static void main(String[] args){
		 PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
	        config.setPassword("xxx");
	        config.setAlgorithm("PBEWithMD5AndDES");
	        config.setKeyObtentionIterations("1000");
	        config.setPoolSize("1");
	        config.setProviderName("SunJCE");
	        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	        config.setStringOutputType("base64");
	        encryptor.setConfig(config);
	        String encrypt = encryptor.encrypt("xxx");
	        System.out.println(encrypt);
	}
	
}
