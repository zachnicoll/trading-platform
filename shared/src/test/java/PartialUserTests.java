import models.AccountType;
import models.partial.PartialUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialUserTests {

    /*
     * Test 0: Declaring PartialUser object
     */
    String username = "temp user";
    AccountType accountType = AccountType.USER;
    UUID organisationalUnitId = UUID.randomUUID();
    String password = "password125";

    PartialUser partialUser;

    /* Test 1: Constructing a PartialUser object
     */
    @BeforeEach
    @Test
    public void setUpPartialUser() {
        partialUser = new PartialUser(username, accountType, organisationalUnitId, password);
    }

    /* Test 2: Get PartialUser's Username
     */
    @Test
    public void getPartialUserUsername() {
        assertEquals("temp user",partialUser.username);
    }

    /* Test 3: Get PartialUser's AccountType
     */
    @Test
    public void getPartialUserAccountType() {
        assertEquals(AccountType.USER,partialUser.accountType);
    }

    /* Test 4: Get PartialUser's OrganisationalUnitId
     */
    @Test
    public void getPartialUserOrganisationalUnitId() {
        assertEquals(organisationalUnitId,partialUser.organisationalUnitId);
    }

    /* Test 5: Get PartialUser's Password
     */
    @Test
    public void getPartialUserPassword() {
        assertEquals("password125",partialUser.password);
    }

}
