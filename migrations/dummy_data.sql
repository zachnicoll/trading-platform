CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

DO $$
declare
	OU1_ID uuid = uuid_generate_v4();
	USER1_ID uuid = uuid_generate_v4();
	USER1_PASSWORD text = '$2a$12$/MOiMr/t34HY.yId62iSs.9glKuSvla4wczxRkZBfhlo6.Czz1hnK'; -- password is "password"
	TRADE1_ID uuid = uuid_generate_v4();

	OU2_ID uuid = uuid_generate_v4();
	USER2_ID uuid = uuid_generate_v4();
	USER2_PASSWORD text = '$2a$12$/MOiMr/t34HY.yId62iSs.9glKuSvla4wczxRkZBfhlo6.Czz1hnK';
	TRADE2_ID uuid = uuid_generate_v4();

	OUASSET_ID uuid = uuid_generate_v4();
	ASSETTYPE_ID uuid = uuid_generate_v4();
BEGIN
	insert into "organisationalUnits" values (
		OU1_ID,
		'Finance',
		1000.0
	);

	insert into "organisationalUnits" values (
		OU2_ID,
		'Human Resources',
		1000.0
	);

	insert into "users" values (
		USER1_ID,
		'sallysue123',
		'USER',
		USER1_PASSWORD,
		OU1_ID
	);

	insert into "users" values (
		USER2_ID,
		'billybob54',
		'ADMIN',
		USER2_PASSWORD,
		OU2_ID
	);

	insert into "assetTypes" values (
		ASSETTYPE_ID,
		'SSL Certificates'
	);

	insert into "organisationalUnitAssets" values (
		OU1_ID,
		ASSETTYPE_ID,
		100
	);

	insert into "organisationalUnitAssets" values (
		OU2_ID,
		ASSETTYPE_ID,
		100
	);

	insert into "openTrades" values (
		TRADE1_ID,
		ASSETTYPE_ID,
		OU1_ID,
		'SELL',
		50,
		1.25,
		CURRENT_TIMESTAMP
	);

	insert into "openTrades" values (
		TRADE2_ID,
		ASSETTYPE_ID,
		OU2_ID,
		'BUY',
		80,
		1.1,
		CURRENT_TIMESTAMP
	);
END $$;