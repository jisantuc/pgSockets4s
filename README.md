# pg-sockets-hs

## Setup

### Tools you'll need

- `psql`
- [`rambler`](https://github.com/elwinar/rambler)

### Initial setup

- Copy `.env.example` to `.env` and `rambler.json.example` to `rambler.json`
- `docker-compose up -d postgres`
- After `docker-compose ps postgres` reports an `Up (healthy)` state, `./scripts/load-development-data` (password in `.env` file)
- Then apply migrations: `rambler apply`

### Prove you're all set

- `docker-compose exec postgres psql -U pgsockets -d pgsockets`
- `LISTEN new_city_channel;`
-
```sql
insert into city (
  id, name, countrycode, district, population
) values (
  4080, 'New City Great Place', 'ABC', 'Good Place District', 12345
);
```
- observe the async notification that gets logged
