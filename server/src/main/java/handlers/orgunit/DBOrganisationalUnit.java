package handlers.orgunit;

import java.util.UUID;

/**
 * Dummy organisationalUnit class to enable partial object
 * data transfer from server to database
 */
public class DBOrganisationalUnit {
    /**
     * UUID String identifying the OrganisationalUnit.
     * Unique.
     */
    private final UUID unitId = UUID.randomUUID();

    /**
     * Name displayed publicly to identify OrganisationUnit.
     * Unique.
     */
    private String unitName;

    /**
     * Current credit balance of the Organisational Unit.
     */
    private Float creditBalance;

    DBOrganisationalUnit(String unitName, Float creditBalance) {
        this.unitName = unitName;
        this.creditBalance = creditBalance;

    }

    /**
     * Get the UnitName of the OU
     *
     * @return the OU's UnitName
     */
    protected String getUnitName() {
        return unitName;
    }

    /**
     * Get the CreditBalance of the OU
     *
     * @return the OU's CreditBalance
     */
    protected Float getCreditBalance() {
        return creditBalance;
    }
}
