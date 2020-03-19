# pgsockets4s

An API project that uses `skunk` and `http4s` to serve Postgres notifications over websockets.

### Development

The following tools must be present for a development environment:

- Docker
- Java 8 or newer -- if this is a problem, try using [`jabba`](https://github.com/shyiko/jabba#jabba--) to manage different java versions
- Sbt 1.x -- the newer the better probably, installation instructions can be found [here](https://www.scala-sbt.org/download.html)
- [Bloop](https://scalacenter.github.io/bloop/) -- this is a scala build server that should run in the background for building the project; installation instructions can be found [here](https://scalacenter.github.io/bloop/setup)
- [rambler](https://github.com/elwinar/rambler) -- a cross-platform migration runner

### STRTA

This project uses [`scripts-to-rule-them-all`](https://github.blog/2015-06-30-scripts-to-rule-them-all/) for managing the developer experience. Below is a quick explanation for the specific usage of each for this particular project.

| Script    | Use                                                                                                                     |
| --------- | ----------------------------------------------------------------------------------------------------------------------- |
| `setup`   | Used to provision the project from scratch                                                                              |
| `update`  | Update project, assemble, run migrations; assume that you can test any PR after running this script on that PR's branch |
| `console` | Open an `ammonite` shell                                                                                                |
| `server`  | Start the server                                                                                                        |
| `dbshell` | Open a `psql` shell connected to the database                                                                           |
| `migrate` | Run migrations against the database                                                                                     |
| `tests`   | Run tests                                                                                                               |
| `cibuild` | Assemble jars and create containers                                                                                     |
