import data.TradeResolverDataGenerator;
import database.datasources.OpenTradeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.ResolvedTradeDataSource;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.TradeResolver;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TradeResolverTests {

    private OpenTradeDataSource openTradeDataSource;
    private ResolvedTradeDataSource resolvedTradeDataSource;
    private TradeResolverDataGenerator tradeResolverDataGenerator;
    private TradeResolver tradeResolver;

    public TradeResolverTests() throws SQLException {
    }

    /**
     * Test 0: Setup TradeResolver and create data needed for tests
     */
    @BeforeEach
    @Test
    public void setupTradeResolver() throws SQLException {
        // Generates test data in constructor
        tradeResolverDataGenerator = new TradeResolverDataGenerator();

        tradeResolver = new TradeResolver();
        openTradeDataSource = new OpenTradeDataSource();
        resolvedTradeDataSource = new ResolvedTradeDataSource();
    }

    /**
     * Test 1: Resolving a BUY and SELL trade with matching quantities and prices
     */
    @Test
    public void resolveMatchingTrades() throws SQLException {
        Integer quantity = 10;
        Float pricePerAsset = 20.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // A resolved trade should be present in DB
        ResolvedTrade resolvedTrade = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(resolvedTrade.getBuyTradeId(), tradeResolverDataGenerator.buyTrade1Id);
        assertEquals(resolvedTrade.getSellTradeId(), tradeResolverDataGenerator.sellTrade1Id);
    }

    /**
     * Test 2: Resolving a trade where the BUY quantity is greater than the SELL quantity
     */
    @Test
    public void resolveBuyQuantityGreaterThan() throws SQLException {
        Integer buyQuantity = 50;
        Integer sellQuantity = 10;
        Float pricePerAsset = 10.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // A resolved trade should have been created with a quantity of 10
        ResolvedTrade resolvedTrade = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(resolvedTrade.getQuantity(), 10);

        // The buy open trade should still exist with a quantity of 40
        OpenTrade remainingTrade = openTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id);
        assertEquals(remainingTrade.getQuantity(), 40);
    }

    /**
     * Test 3: Resolving a trade where the BUY quantity is less than the SELL quantity
     */
    @Test
    public void resolveBuyQuantityLessThan() throws SQLException {
        Integer buyQuantity = 10;
        Integer sellQuantity = 50;
        Float pricePerAsset = 10.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // A resolved trade should have been created with a quantity of 10
        ResolvedTrade resolvedTrade = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(resolvedTrade.getQuantity(), 10);

        // The sell open trade should still exist with a quantity of 40
        OpenTrade remainingTrade = openTradeDataSource.getById(tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(remainingTrade.getQuantity(), 40);
    }

    /**
     * Test 4: Case where a SELL OpenTrade has enough quantity to satisfy multiple BUY OpenTrades
     */
    @Test
    public void multipleBuyFromSingleSell() throws SQLException {
        Integer buyQuantity = 10;
        Integer sellQuantity = 30;
        Float pricePerAsset = 10.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade1 = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade buyOpenTrade2 = new OpenTrade(
                tradeResolverDataGenerator.buyTrade2Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade buyOpenTrade3 = new OpenTrade(
                tradeResolverDataGenerator.buyTrade3Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade1);
        openTradeDataSource.createNew(buyOpenTrade2);
        openTradeDataSource.createNew(buyOpenTrade3);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // Three resolved trades should have been created with a quantity of 10
        ResolvedTrade resolvedTrade1 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        ResolvedTrade resolvedTrade2 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade2Id, tradeResolverDataGenerator.sellTrade1Id);
        ResolvedTrade resolvedTrade3 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade3Id, tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(resolvedTrade1.getQuantity(), 10);
        assertEquals(resolvedTrade2.getQuantity(), 10);
        assertEquals(resolvedTrade3.getQuantity(), 10);

        // The sell open trade should no longer exist
        assertThrows(SQLException.class, () -> {
            openTradeDataSource.getById(tradeResolverDataGenerator.sellTrade1Id);
        });
    }

    /**
     * Test 5: Case where a BUY OpenTrade has enough quantity to satisfy multiple SELL OpenTrades
     */
    @Test
    public void multipleSellFromSingleBuy() throws SQLException {
        Integer buyQuantity = 30;
        Integer sellQuantity = 10;
        Float pricePerAsset = 10.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                buyQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade1 = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade2 = new OpenTrade(
                tradeResolverDataGenerator.sellTrade2Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );


        OpenTrade sellOpenTrade3 = new OpenTrade(
                tradeResolverDataGenerator.sellTrade3Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                sellQuantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );


        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade1);
        openTradeDataSource.createNew(sellOpenTrade2);
        openTradeDataSource.createNew(sellOpenTrade3);

        tradeResolver.run();

        // Three resolved trades should have been created with a quantity of 10
        ResolvedTrade resolvedTrade1 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        ResolvedTrade resolvedTrade2 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade2Id);
        ResolvedTrade resolvedTrade3 = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade3Id);
        assertEquals(resolvedTrade1.getQuantity(), 10);
        assertEquals(resolvedTrade2.getQuantity(), 10);
        assertEquals(resolvedTrade3.getQuantity(), 10);

        // The buy open trade should no longer exist
        assertThrows(SQLException.class, () -> {
            openTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id);
        });
    }

    /**
     * Test 6: No matching OpenTrades that satisfy price requirements
     */
    @Test
    public void resolveNoTrades() throws SQLException {
        Integer quantity = 10;
        Float buyPricePerAsset = 20.0f;
        Float sellPricePerAsset = 25.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                buyPricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                sellPricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // Open trades should not have been resolved as the sell price was higher than the buy price
        assertThrows(SQLException.class, () -> {
            resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        });
    }

    /**
     * Test 7: Buy price higher than Sell price, Buy trade resolves with lower price
     */
    @Test
    public void buyPriceHigherThanSellPrice() throws SQLException {
        Integer quantity = 10;
        Float buyPricePerAsset = 25.0f;
        Float sellPricePerAsset = 20.0f;

        tradeResolverDataGenerator.createAssetsOrg1();
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                buyPricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                sellPricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // Trades should have been resolved at the lower price (sell price), as the BUY price was higher than the SELL price
        ResolvedTrade resolvedTrade = resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        assertEquals(resolvedTrade.getPrice(), sellPricePerAsset);
    }

    /**
     * Test 8: Org Unit places BUY order for AssetType that it does not own, gets AssetType
     * added to organisationalUnitAssets table
     */
    @Test
    public void buyOrderForAssetTypeNotOwned() throws SQLException {
        Integer quantity = 10;
        Float pricePerAsset = 20.0f;

        // Only create Assets for Org Unit 2
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // Resolved trade should exist and not throw SQLException
        resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);

        // Org Unit 1 should now have the Asset bought in the trades
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit organisationalUnit = organisationalUnitDataSource.getById(tradeResolverDataGenerator.orgUnit1Id);
        Asset asset = organisationalUnit.findExistingAsset(tradeResolverDataGenerator.assetType1Id);
        assertNotNull(asset);
    }

    /**
     * Test 9: Org Unit tries to sell more Assets than they own
     */
    @Test
    public void sellMoreAssetsThanOwned() throws SQLException {
        Integer quantity = 5000;
        Float pricePerAsset = 0.05f;

        // Only create Assets for Org Unit 2
        tradeResolverDataGenerator.createAssetsOrg2();

        OpenTrade buyOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.buyTrade1Id,
                TradeType.BUY,
                tradeResolverDataGenerator.orgUnit1Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        OpenTrade sellOpenTrade = new OpenTrade(
                tradeResolverDataGenerator.sellTrade1Id,
                TradeType.SELL,
                tradeResolverDataGenerator.orgUnit2Id,
                tradeResolverDataGenerator.assetType1Id,
                quantity,
                pricePerAsset,
                Timestamp.from(Instant.now())
        );

        openTradeDataSource.createNew(buyOpenTrade);
        openTradeDataSource.createNew(sellOpenTrade);

        tradeResolver.run();

        // No resolved trade should exists as Sell Org Unit tried to sell more than they owned
        assertThrows(SQLException.class, () -> {
            resolvedTradeDataSource.getById(tradeResolverDataGenerator.buyTrade1Id, tradeResolverDataGenerator.sellTrade1Id);
        });
    }

    @AfterEach
    @Test
    public void deleteTestData() throws SQLException {
        // Delete existing test objects to clean up previous test alterations
        tradeResolverDataGenerator.destroyTestData();
    }
}
