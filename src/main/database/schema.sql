CREATE TABLE  USERS(
   USERNAME TEXT PRIMARY KEY     NOT NULL,
   PASSWORD           TEXT    NOT NULL,
   EMAIL            TEXT,
   FIRSTNAME            TEXT,
   LASTNAME            TEXT,
   ALIAS            TEXT,
   GENDER INTEGER,
   SOCIALTYPE INTEGER,
   CREATEDAT timestamp,
   UPDATEDAT timestamp

);

CREATE UNIQUE INDEX USERNAME_ci_idx ON users ((lower(USERNAME)));
CREATE  INDEX EMAIL_ci_idx ON users ((lower(EMAIL)));

CREATE FUNCTION USERS_stamp() RETURNS trigger AS $USERS_stamp$
    BEGIN

        -- Remember who changed the payroll when
        IF (TG_OP = 'INSERT') THEN
        	NEW.CREATEDAT := current_timestamp;
        END IF;
        NEW.UPDATEDAT := current_timestamp;
        RETURN NEW;
    END;
$USERS_stamp$ LANGUAGE plpgsql;

CREATE TRIGGER users_stamp BEFORE INSERT OR UPDATE ON users
    FOR EACH ROW EXECUTE PROCEDURE users_stamp();

