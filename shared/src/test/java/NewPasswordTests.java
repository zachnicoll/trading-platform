import models.Asset;
import models.NewPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class NewPasswordTests {


    /*
     * Test 0: Declaring NewPassword objects
     */
    String password = "aPassword";
    String confirmPassword = "aPassword";
    String wrongPassword = "anotherPassword";
    NewPassword newPassword;
    NewPassword aWrongPassword;

    /* Test 1: Constructing an NewPassword object
     */
    @BeforeEach
    @Test
    public void setUpNewPassword() {
        newPassword = new NewPassword(password, confirmPassword);
        aWrongPassword = new NewPassword(password, wrongPassword);
    }

    /* Test 2: Get NewPassword Password
     */
    @Test
    public void getNewPasswordPassword() {
        assertEquals(newPassword.password, "aPassword");
    }

    /* Test 3: Get NewPassword ConfirmPassword
     */
    @Test
    public void getNewPasswordConfirmPassword() {
        assertEquals(newPassword.confirmPassword, "aPassword");
    }

    /* Test 4: NewPassword CheckMatch
     */
    @Test
    public void getNewPasswordCheckMatch() {
        assertEquals(newPassword.checkMatchingPasswords(), true);
    }

    /* Test 5: NewPassword CheckMatch Fail
     */
    @Test
    public void getNewPasswordCheckMatchFail() {
        assertEquals(aWrongPassword.checkMatchingPasswords(), false);
    }


}