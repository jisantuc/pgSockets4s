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
- `LISTEN new_country_channel;`
-
```sql
insert into country (
  code, name, continent, region, surfacearea, population,
  localname, governmentform, code2
) values (
  'NEW', 'New Land', 'Asia', 'South Asia', 1234.5, 1234, 'New Land',
  'constitutional monarchy', 'NE'
);
```
- observe the async notification that gets logged
