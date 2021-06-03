import models.partial.PartialOrganisationalUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialOrganisationalUnitTests {

    /*
     * Test 0: Declaring PartialOrganisationalUnit object
     */
    String unitName = "test unit";
    Float creditBalance1 = 10000f;
    Float creditBalance2 = 5000f;
    PartialOrganisationalUnit partialOrganisationalUnit1;
    PartialOrganisationalUnit partialOrganisationalUnit2;

    /* Test 1: Constructing a PartialOrganisationalUnit object
     */
    @BeforeEach
    @Test
    public void setUpPartialOrganisationalUnit() {
        // Partial #1
        partialOrganisationalUnit1 = new PartialOrganisationalUnit(unitName, creditBalance1);

        //Partial #2
        partialOrganisationalUnit2 = new PartialOrganisationalUnit(creditBalance2);
    }

    /* Test 2: Get Partial#1 Name
     */
    @Test
    public void getPartialOrganisationalUnitName() {
        assertEquals("test unit", partialOrganisationalUnit1.unitName);
    }

    /* Test 3: Get Partial#1 Balance
     */
    @Test
    public void getPartialOrganisationalUnitBalance() {
        assertEquals(10000f, partialOrganisationalUnit1.creditBalance);
    }

    /* Test 4: Get Partial#2 Balance
     */
    @Test
    public void getAlternatePartialOrganisationalUnitBalance() {
        assertEquals(5000f, partialOrganisationalUnit2.creditBalance);
    }

}
