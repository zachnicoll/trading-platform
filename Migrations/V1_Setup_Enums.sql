/*
Creates enumerated types for accounts, trades, and trade status.
*/

CREATE TYPE accountType AS ENUM ('USER', 'ADMIN');
CREATE TYPE tradeType AS ENUM('BUY', 'SELL');
CREATE TYPE statusType AS ENUM('RESOLVED','UNRESOLVED');