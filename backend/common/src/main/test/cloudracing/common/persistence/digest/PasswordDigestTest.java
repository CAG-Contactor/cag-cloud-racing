package cloudracing.common.persistence.digest;

import org.junit.Test;
import se.caglabs.cloudracing.common.persistence.digest.PasswordDigest;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PasswordDigestTest {

    private static final String PWD = "password";

    @Test
    public void shouldDigestPassword() {
        assertThat(PasswordDigest.digest(PWD), not(PWD));
    }

}
