package org.shozo.util;

import java.security.Key;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;

public class SimpleKeyGenerator implements KeyGenerator {
	
	Logger logger = Logger.getLogger(SimpleKeyGenerator.class.getName());
	
	@Override
	public Key generateKey() {
		
        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        logger.info("key "+key);
        return key;
	}

}
