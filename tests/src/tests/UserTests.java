package tests;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.ApiException;
import models.AccountType;
import models.User;
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

    /* Test 2: Checking authenticated without logging-in
     */
    @Test
    public void checkAuthenticatedNoLogin() {
        Boolean isAuthed = user.IsAuthenticated();
        assertEquals(isAuthed, false);
    }

    /* Test 3: Get User's AccountType
     */
    @Test
    public void getAccountType() {
        assertEquals(user.getAccountType(), accountType);
    }

    /* Test 4: Get User's Id
     */
    @Test
    public void getUserId() {
        assertEquals(user.getUserId(), userId);
    }

    /* Test 5: Get User's Organisational Unit Id
     */
    @Test
    public void getOrgUnitId() {
        assertEquals(user.getOrganisationalUnitId(), organisationalUnitId);
    }

    /* Test 6: Get User's username
     */
    @Test
    public void getUsername() {
        assertEquals(user.getUsername(), username);
    }

    /* Test 7: Change a User's password with mis-matching password and
     * confirmPassword
     */
    @Test
    public void changePasswordIncorrect() throws AuthenticationException {
        assertThrows(AuthenticationException.class, () -> {
           user.changePassword("password1", "password2");
        });
    }

    /* Test 8: Attempt to login with username and password and check if authenticated
     */
    @Test
    public void loginCheckAuthenticated() throws AuthenticationException {
        String authToken = User.Login(username, "password");
        user.setAuthenticationToken(authToken);
        assertEquals(user.IsAuthenticated(), true);
    }

    /* Test 9: Login with incorrect credentials
     */
    @Test
    public void loginIncorrectCredentials() throws AuthenticationException {
        // TODO: Make this fail correctly when API is hooked-up
        assertThrows(AuthenticationException.class, () -> {
            User.Login(username, "wrong_password");
        });
    }

    /* Test 10: Update User information
     */
    @Test
    public void updateUser() throws ApiException {
        UUID newOrgUnitId = UUID.randomUUID();
        AccountType newAccountType = AccountType.ADMIN;
        user.updateUser(newAccountType, newOrgUnitId);
        assertEquals(newAccountType, user.getAccountType());
        assertEquals(newOrgUnitId, user.getOrganisationalUnitId());
    }

    /* Test 11: Login and logout, check for auth status
     */
    @Test
    public void loginLogout() throws AuthenticationException {
        String authToken = User.Login(username, "password");
        user.setAuthenticationToken(authToken);
        user.Logout();
        assertEquals(false, user.IsAuthenticated());
    }
}
