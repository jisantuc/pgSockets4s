-- rambler up

CREATE OR REPLACE FUNCTION new_country_notify()
	RETURNS trigger AS
$$
BEGIN
    PERFORM pg_notify('new_country_channel', row_to_json(NEW)::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER new_country_trigger
	AFTER INSERT OR UPDATE OF code
	ON country
	FOR EACH ROW
EXECUTE PROCEDURE new_country_notify();

-- rambler down

DROP TRIGGER IF EXISTS new_country_trigger on country;

DROP FUNCTION IF EXISTS new_country_notify();
