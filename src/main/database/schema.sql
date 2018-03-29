CREATE TABLE  USERS(
   USERNAME TEXT PRIMARY KEY     NOT NULL,
   PASSWORD           TEXT    NOT NULL,
   EMAIL            TEXT,
   FIRSTNAME            TEXT,
   LASTNAME            TEXT,
   ALIAS            TEXT,
   GENDER INTEGER,
   SOCIALTYPE INTEGER,
   CREATEDAT bigint,
   UPDATEDAT bigint

);

CREATE TABLE  ACCOUNTS(
   ID TEXT PRIMARY KEY     NOT NULL,
   NAME           TEXT    NOT NULL,
   DESCRIPTION            TEXT,
   IS_SHARABLE            BOOLEAN,
   IS_DELETABLE            BOOLEAN,
   STATUS            SMALLINT,
   DEFAULT_USER TEXT,
   CREATEDAT bigint,
   UPDATEDAT bigint

);

CREATE TABLE  USER_ACCOUNT(
   ID_USER TEXT   NOT NULL,
   ID_ACCOUNT  TEXT      NOT NULL,
	PRIMARY KEY (ID_USER, ID_ACCOUNT)
);

 CREATE TABLE  CATEGORIES(
   ID TEXT PRIMARY KEY     NOT NULL,
   TYPE           SMALLINT    NOT NULL,
   USER_VALUE TEXT,
   IS_EDITABLE           BOOLEAN,
   ACCOUNT_ID            TEXT,
   CREATEDAT bigint,
   UPDATEDAT bigint

);



CREATE UNIQUE INDEX USERNAME_ci_idx ON users ((lower(USERNAME)));
CREATE  INDEX EMAIL_ci_idx ON users ((lower(EMAIL)));

CREATE OR REPLACE FUNCTION stamp() RETURNS trigger AS $stamp$
    BEGIN

        -- Remember who changed the payroll when
        IF (TG_OP = 'INSERT') THEN
        	NEW.CREATEDAT := trunc(extract(epoch from now() at time zone 'utc'));
        END IF;
        NEW.UPDATEDAT := trunc(extract(epoch from now() at time zone 'utc'));
        RETURN NEW;
    END;
$stamp$ LANGUAGE plpgsql;


CREATE TRIGGER users_stamp BEFORE INSERT OR UPDATE ON users
    FOR EACH ROW EXECUTE PROCEDURE stamp();


CREATE TRIGGER ACCOUNTS_stamp BEFORE INSERT OR UPDATE ON ACCOUNTS
    FOR EACH ROW EXECUTE PROCEDURE stamp();

CREATE TRIGGER categories_stamp BEFORE INSERT OR UPDATE ON CATEGORIES
    FOR EACH ROW EXECUTE PROCEDURE stamp();