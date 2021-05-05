/*
Creates enumerated types for accounts, trades, and trade status.
*/

CREATE TYPE accountType AS ENUM ('USER', 'ADMIN');
CREATE TYPE tradeType AS ENUM('BUY', 'SELL');
CREATE TYPE statusType AS ENUM('RESOLVED','UNRESOLVED');


/*
Creates the tables for the trading platform database.
*/

CREATE table if not exists "users" (
  "userId" uuid PRIMARY KEY,
  "username" varchar UNIQUE,
  "userType" accountType,
  "password" varchar,
  "organisationalUnitId" uuid
);

CREATE table if not exists "organisationalUnits" (
  "organisationalUnitId" uuid PRIMARY KEY,
  "organisationalUnitName" varchar UNIQUE,
  "creditBalance" float
);

CREATE table if not exists "assetTypes" (
  "assetTypeId" uuid PRIMARY KEY,
  "assetName" varchar UNIQUE
);

CREATE table if not exists "organisationalUnitAssets" (
  "organisationalUnitId" uuid,
  "assetTypeId" uuid,
  "quantity" int,
  PRIMARY KEY("organisationalUnitId", "assetTypeId")
);

CREATE table if not exists "trades" (
  "tradeId" uuid PRIMARY KEY,
  "assetTypeId" uuid,
  "organisationalUnitId" uuid,
  "tradeType" tradeType,
  "quantity" float,
  "price" float,
  "date" timestamp,
  "status" statusType
);

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId");

ALTER TABLE "users" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "trades" ADD FOREIGN KEY ("assetTypeId") REFERENCES "assetTypes" ("assetTypeId");

ALTER TABLE "trades" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");
