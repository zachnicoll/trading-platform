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
  "username" varchar UNIQUE NOT NULL,
  "userType" "accountType" NOT NULL,
  "password" varchar NOT NULL,
  "organisationalUnitId" uuid NOT NULL
);

CREATE TABLE "organisationalUnits" (
  "organisationalUnitId" uuid PRIMARY KEY,
  "organisationalUnitName" varchar UNIQUE NOT NULL,
  "creditBalance" float NOT NULL
);

CREATE TABLE "assetTypes" (
  "assetTypeId" uuid PRIMARY KEY,
  "assetName" varchar UNIQUE NOT NULL
);

CREATE TABLE "organisationalUnitAssets" (
  "organisationalUnitId" uuid,
  "assetTypeId" uuid,
  "quantity" int NOT NULL,
  PRIMARY KEY ("organisationalUnitId", "assetTypeId")
);

CREATE TABLE "openTrades" (
  "tradeId" uuid PRIMARY KEY,
  "assetTypeId" uuid NOT NULL,
  "organisationalUnitId" uuid NOT NULL,
  "tradeType" "tradeType" NOT NULL,
  "quantity" int NOT NULL,
  "price" float NOT NULL,
  "dateOpened" timestamp NOT NULL
);

CREATE TABLE "resolvedTrades" (
  "buyTradeId" uuid,
  "sellTradeId" uuid,
  "buyOrgUnitId" uuid NOT NULL,
  "sellOrgUnitId" uuid NOT NULL,
  "assetTypeId" uuid NOT NULL,
  "quantity" int NOT NULL,
  "price" float NOT NULL,
  "dateResolved" timestamp NOT NULL,
  CHECK ("quantity" > 0 AND "price" > 0),
  PRIMARY KEY ("buyTradeId", "sellTradeId")
);

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId") ON DELETE CASCADE;

ALTER TABLE "users" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId") ON DELETE CASCADE;

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId") ON DELETE CASCADE;

ALTER TABLE "openTrades" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId") ON DELETE CASCADE;

ALTER TABLE "openTrades" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId") ON DELETE CASCADE;

ALTER TABLE "resolvedTrades" ADD FOREIGN KEY ("buyOrgUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId") ON DELETE CASCADE;

ALTER TABLE "resolvedTrades" ADD FOREIGN KEY ("sellOrgUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId") ON DELETE CASCADE;

ALTER TABLE "resolvedTrades" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId") ON DELETE CASCADE;
