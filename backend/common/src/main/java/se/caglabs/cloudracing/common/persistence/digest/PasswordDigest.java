package se.caglabs.cloudracing.common.persistence.digest;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordDigest {

    public static String digest(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
