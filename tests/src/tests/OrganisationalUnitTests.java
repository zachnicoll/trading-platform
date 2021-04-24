package tests;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.ApiException;
import exceptions.InvalidTransactionException;

import models.OrganisationalUnit;
import models.Asset;
import models.AssetType;

import org.junit.jupiter.api.*;

import javax.naming.AuthenticationException;
import java.util.UUID;

public class OrganisationalUnitTests {

    /*
     * Test 0: Declaring OrganisationalUnit objects
     */
    OrganisationalUnit organisationalUnit;
    String unitID = UUID.randomUUID().toString();

}