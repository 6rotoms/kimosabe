package kimosabe.api.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class StringGenerator {
    SecureRandom random;
    char[] alphaNumeric;
    public StringGenerator() {
        random = new SecureRandom();
        alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    }
    public String generateAlphaNumeric(int length) {
        random.setSeed(random.generateSeed(64));
        char[] buff = new char[length];
        for (int i = 0; i < length; i++) {
            buff[i] = alphaNumeric[random.nextInt(alphaNumeric.length)];
        }
        return new String(buff);
    }
}
