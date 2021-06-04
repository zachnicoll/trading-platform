import static org.junit.jupiter.api.Assertions.*;

import models.AccountType;
import models.User;
import exceptions.*;
import org.junit.jupiter.api.*;
import javax.naming.AuthenticationException;
import java.util.UUID;

public class UserTests {

    /*
     * Test 0: Declaring User objects
     */
    User user;
    UUID userId = UUID.randomUUID();
    UUID organisationalUnitId = UUID.randomUUID();
    String username = "test_user";
    AccountType accountType = AccountType.USER;


    /* Test 1: Constructing a User object
     */
    @BeforeEach
    @Test
    public void setUpUser() {
        user = new User(
                userId,
                username,
                accountType,
                organisationalUnitId
        );
    }


    /* Test 2: Get User's AccountType
     */
    @Test
    public void getAccountType() {
        assertEquals(user.getAccountType(), accountType);
    }

    /* Test 3: Get User's Id
     */
    @Test
    public void getUserId() {
        assertEquals(user.getUserId(), userId);
    }

    /* Test 4: Get User's Organisational Unit Id
     */
    @Test
    public void getOrgUnitId() {
        assertEquals(user.getOrganisationalUnitId(), organisationalUnitId);
    }

    /* Test 5: Get User's username
     */
    @Test
    public void getUsername() {
        assertEquals(user.getUsername(), username);
    }


    /* Test 7: Update User information
     */
    @Test
    public void updateUser() throws ApiException {
        UUID newOrgUnitId = UUID.randomUUID();
        AccountType newAccountType = AccountType.ADMIN;
        user.updateUser(newAccountType, newOrgUnitId);
        assertEquals(newAccountType, user.getAccountType());
        assertEquals(newOrgUnitId, user.getOrganisationalUnitId());
    }
}
