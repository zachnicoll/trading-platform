/*
Creates enumerated types for accounts, trades, and trade status.
*/

CREATE TYPE accountType AS ENUM ('USER', 'ADMIN');
CREATE TYPE tradeType AS ENUM('BUY', 'SELL');
CREATE TYPE statusType AS ENUM('RESOLVED','UNRESOLVED');


/*
Creates the tables for the trading platform database.
*/

CREATE TABLE "users" (
  "userId" uuid PRIMARY KEY,
  "username" varchar UNIQUE,
  "userType" accountType,
  "password" varchar,
  "organisationalUnitId" uuid UNIQUE
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
  "organisationalUnitId" uuid PRIMARY KEY,
  "assetTypeId" uuid UNIQUE,
  "quantity" int
);

CREATE TABLE "trades" (
  "tradeId" uuid PRIMARY KEY,
  "assetId" uuid UNIQUE,
  "organisationalUnitId" uuid UNIQUE,
  "tradeType" tradeType,
  "quantity" float,
  "price" float,
  "date" timestamp,
  "status" statusType
);

ALTER TABLE "assetTypes" ADD FOREIGN KEY ("assetTypeId") REFERENCES "organisationalUnitAssets" ("assetTypeId");

ALTER TABLE "organisationalUnits" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "users" ("organisationalUnitId");

ALTER TABLE "organisationalUnitAssets" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "organisationalUnits" ("organisationalUnitId");

ALTER TABLE "assetTypes" ADD FOREIGN KEY ("assetTypeId") REFERENCES "trades" ("assetTypeId");

ALTER TABLE "organisationalUnits" ADD FOREIGN KEY ("organisationalUnitId") REFERENCES "trades" ("organisationalUnitId");
