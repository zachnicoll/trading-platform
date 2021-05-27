package models.partial;


/**
 * Class similar to the OrganisationalUnit class, but contains no methods and all attributes
 * are publicly accessible. This class is used when creating a new OrganisationalUnit,
 * as only certain attributes need to be sent to the Rest API. For example,
 * the OrganisationalUnitId is generated on the Server and therefore should not be sent
 * with a POST request to the /orgunit/ endpoint.
 */
public class PartialOrganisationalUnit {
    public final String unitName;
    public final Float creditBalance;


    public PartialOrganisationalUnit (String unitName, Float creditBalance) {
        this.unitName = unitName;
        this.creditBalance = creditBalance;
    }
    public PartialOrganisationalUnit (Float creditBalance) {
        this.creditBalance = creditBalance;
        unitName = null;
    }

    public Float getCreditBalance()
    {
        return this.creditBalance;
    }

}

