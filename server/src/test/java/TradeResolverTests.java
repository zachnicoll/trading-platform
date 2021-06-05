import database.datasources.AssetTypeDataSource;
import database.datasources.OpenTradeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.ResolvedTradeDataSource;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import models.AssetType;
import models.OpenTrade;
import models.OrganisationalUnit;
import models.TradeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.TradeResolver;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/*
 * TODO: Implement these tests once each DataSource has been completed
 */

public class TradeResolverTests {

    private final OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
    private final ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();
    private final OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
    private final AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
    private final UUID orgUnit1Id = UUID.fromString("f1252904-35da-4cbd-b0ec-d7b83c49df40");
    private final OrganisationalUnit orgUnit1 = new OrganisationalUnit(
            orgUnit1Id,
            "Test Org Unit 1",
            1000.0f,
            new ArrayList<>()
    );
    private final UUID assetType1Id = UUID.fromString("9ac0121a-1d34-4356-9a11-deac37a2e1e5");
    private final AssetType assetType1 = new AssetType(
            assetType1Id,
            "Asset Type 1"
    );
    private final UUID assetType2Id = UUID.fromString("8e179d66-e9d7-40a4-8a65-6b188dfeb393");
    private final AssetType assetType2 = new AssetType(
            assetType2Id,
            "Asset Type 2"
    );
    private final UUID buyTrade1Id = UUID.fromString("1124c784-7e4a-45fe-9745-11e3834388bc");
    private final UUID buyTrade2Id = UUID.fromString("0ac83fb5-a51c-43b7-800c-26d2741ec4d5");
    private final UUID buyTrade3Id = UUID.fromString("69e40edc-8051-4abd-8aa0-236f35c1e1d5");
    private final UUID sellTrade1Id = UUID.fromString("83a00a4e-ade1-423f-90f2-8917b0a95eb7");
    private final UUID sellTrade2Id = UUID.fromString("a2566024-5a38-4a49-be22-8a379a9e3842");
    private final UUID sellTrade3Id = UUID.fromString("bca901ea-47b0-4570-af89-3933e4cf286b");
    private TradeResolver tradeResolver;
    private UUID orgUnit2Id = UUID.fromString("2607dddd-a3d9-43d5-a58c-dd400739bf21");
    private final OrganisationalUnit orgUnit2 = new OrganisationalUnit(
            orgUnit2Id,
            "Test Org Unit 2",
            1000.0f,
            new ArrayList<>()
    );

    /**
     * Test 0: Setup TradeResolver and create data needed for tests
     */
    @BeforeEach
    public void setupTradeResolver() throws SQLException {
        tradeResolver = new TradeResolver();

        // Create fresh objects in the database
        organisationalUnitDataSource.createNew(orgUnit1);
        assetTypeDataSource.createNew(assetType1);
    }

    /**
     * Test 1: Resolving a BUY and SELL trade with matching quantities and prices
     */
    @Test
    public void resolveMatchingTrades() throws SQLException, NotImplementedException {
        Integer quantity = 10;
        Float pricePerAsset = 20.0f;

        /*
            Set values in DB here
         */
        OpenTrade buyOpenTrade = new OpenTrade(
                buyTrade1Id,
                TradeType.BUY,
                orgUnit1Id,
                assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                sellTrade1Id,
                TradeType.SELL,
                orgUnit2Id,
                assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        //        openTradeDataSource.createNew(buyOpenTrade);
        //        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();
        
        /*
            Check values in DB here
         */

        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 2: Resolving a trade where the BUY quantity is greater than the SELL quantity
     */
    @Test
    public void resolveBuyQuantityGreaterThan() throws NotImplementedException {
        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 3: Resolving a trade where the BUY quantity is less than the SELL quantity
     */
    @Test
    public void resolveBuyQuantityLessThan() throws NotImplementedException {
        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 4: Case where a SELL OpenTrade has enough quantity to satisfy multiple BUY OpenTrades
     */
    @Test
    public void multipleBuyFromSingleSell() throws NotImplementedException {
        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 5: Case where a BUY OpenTrade has enough quantity to satisfy multiple SELL OpenTrades
     */
    @Test
    public void multipleSellFromSingleBuy() throws NotImplementedException {
        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 6: No matching OpenTrades
     */
    @Test
    public void resolveNoTrades() throws NotImplementedException {
        throw new NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    @AfterEach
    @Test
    public void deleteTestData() throws SQLException {
        // Delete existing test objects to clean up previous test alterations
        organisationalUnitDataSource.deleteById(orgUnit1Id);
        organisationalUnitDataSource.deleteById(orgUnit2Id);

        assetTypeDataSource.deleteById(assetType1Id);
        assetTypeDataSource.deleteById(assetType2Id);
    }
}
