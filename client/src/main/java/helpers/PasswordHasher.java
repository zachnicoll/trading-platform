package helpers;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Simple wrapper class for hashing Strings, in the context of a password String.
 */
public class PasswordHasher {
    public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
