import models.AccountType;
import models.partial.PartialReadableUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialReadableUserTests {

    /*
     * Test 0: Declaring PartialReadableUser object
     */
    UUID userId = UUID.randomUUID();
    String username = "test user";
    AccountType accountType = AccountType.ADMIN;
    String organisationalUnitName = "test unit";

    PartialReadableUser partialReadableUser;

    /* Test 1: Constructing a PartialReadableUser object
     */
    @BeforeEach
    @Test
    public void setUpPartialReadableUser() {
        partialReadableUser = new PartialReadableUser(userId, username, accountType, organisationalUnitName);
    }

    /* Test 2: Get PartialReadableUser's UserId
     */
    @Test
    public void getPartialReadableUserUserId() {
        assertEquals(userId,partialReadableUser.getUserId());
    }

    /* Test 3: Get PartialReadableUser's Username
     */
    @Test
    public void getPartialReadableUserUsername() {
        assertEquals("test user",partialReadableUser.getUsername());
    }

    /* Test 4: Get PartialReadableUser's AccountType
     */
    @Test
    public void getPartialReadableUserAccountType() {
        assertEquals(AccountType.ADMIN,partialReadableUser.getAccountType());
    }

    /* Test 5: Get PartialReadableUser's OrganisationalUnitName
     */
    @Test
    public void getPartialReadableUserOrganisationalUnitName() {
        assertEquals("test unit",partialReadableUser.getOrganisationalUnitName());
    }

}
