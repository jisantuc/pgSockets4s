-- rambler up

CREATE OR REPLACE FUNCTION new_city_notify()
	RETURNS trigger AS
$$
BEGIN
    PERFORM pg_notify(lower(NEW.countrycode), row_to_json(NEW)::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS new_city_trigger on city;

CREATE TRIGGER new_city_trigger
	AFTER INSERT OR UPDATE OF id
	ON city
	FOR EACH ROW
EXECUTE PROCEDURE new_city_notify();

-- rambler down

DROP TRIGGER IF EXISTS new_city_trigger on city;

DROP FUNCTION IF EXISTS new_city_notify();
