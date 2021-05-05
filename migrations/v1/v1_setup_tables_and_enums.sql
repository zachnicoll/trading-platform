CREATE TYPE "accountType" AS ENUM (
  'USER',
  'ADMIN'
);

CREATE TYPE "tradeType" AS ENUM (
  'BUY',
  'SELL'
);

CREATE TABLE "users" (
  "userId" uuid PRIMARY KEY,
  "username" varchar UNIQUE,
  "userType" "accountType",
  "password" varchar,
  "organisationalUnitId" uuid
);

CREATE TABLE "organisationalUnits" (
  "organisationalUnitId" uuid PRIMARY KEY,
  "organisationalUnitName" varchar UNIQUE,
  "creditBalance" float
);

CREATE TABLE "assetTypes" (
  "assetTypeId" uuid PRIMARY KEY,
  "assetName" varchar UNIQUE
);

CREATE TABLE "organisationalUnitAssets" (
  "organisationalUnitId" uuid,
  "assetTypeId" uuid,
  "quantity" int,
  PRIMARY KEY ("organisationalUnitId", "assetTypeId")
);

CREATE TABLE "openTrades" (
  "tradeId" uuid PRIMARY KEY,
  "assetTypeId" uuid,
  "organisationalUnitId" uuid,
  "tradeType" "tradeType",
  "quantity" int,
  "price" float,
  "dateOpened" timestamp
);

CREATE TABLE "resolvedTrades" (
  "buyTradeId" uuid,
  "buyOrgUnitId" uuid,
  "sellTradeId" uuid,
  "sellOrgUnitId" uuid,
  "quantity" int,
  "price" float,
  "dateResolved" timestamp,
  PRIMARY KEY ("buyTradeId", "sellTradeId")
);

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId");

ALTER TABLE "users" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "openTrades" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId");

ALTER TABLE "openTrades" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "resolvedTrades" ADD FOREIGN KEY ("buyOrgUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "resolvedTrades" ADD FOREIGN KEY ("sellOrgUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");
