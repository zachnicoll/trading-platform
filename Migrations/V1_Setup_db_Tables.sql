/*
Creates the tables for the trading platform database.
*/

CREATE TABLE "users" (
  "userID" uuid PRIMARY KEY,
  "username" varchar UNIQUE,
  "userType" accountType,
  "password" varchar,
  "ouID" uuid UNIQUE
);

CREATE TABLE "organisationalUnits" (
  "ouID" uuid PRIMARY KEY,
  "organisationalUnit" varchar UNIQUE,
  "credits" float
);

CREATE TABLE "assets" (
  "assetID" uuid PRIMARY KEY,
  "assetName" varchar UNIQUE
);

CREATE TABLE "OUassets" (
  "ouID" uuid PRIMARY KEY,
  "assetID" uuid UNIQUE,
  "quantity" int
);

CREATE TABLE "trades" (
  "tradeID" uuid PRIMARY KEY,
  "assetID" uuid UNIQUE,
  "ouID" uuid UNIQUE,
  "tradeType" tradeType,
  "quantity" float,
  "price" float,
  "date" timestamp,
  "status" statusType
);

ALTER TABLE "assets" ADD FOREIGN KEY ("assetID") REFERENCES "OUassets" ("assetID");

ALTER TABLE "organisationalUnits" ADD FOREIGN KEY ("ouID") REFERENCES "users" ("ouID");

ALTER TABLE "OUassets" ADD FOREIGN KEY ("ouID") REFERENCES "organisationalUnits" ("ouID");

ALTER TABLE "assets" ADD FOREIGN KEY ("assetID") REFERENCES "trades" ("assetID");

ALTER TABLE "organisationalUnits" ADD FOREIGN KEY ("ouID") REFERENCES "trades" ("ouID");
