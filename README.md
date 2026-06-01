Keycloak SAML local
================================================================================

Run a Keycloak SAML IdP configuration locally using containers.


How to run
--------------------------------------------------------------------------------

1. Run containers

```bash
docker compose down && docker compose up --build
```


How to confirm a SAML SP site login
--------------------------------------------------------------------------------

1. Run containers

```bash
docker compose down && docker compose up --build
```

2. Access to SAML SP site and goto Protected page

http://localhost:9009/protected

- Username: `testuser`
- Password: `ttttt-11111`


How to export realm & users from Keycloak
--------------------------------------------------------------------------------

1. Use the `exec` subcommand to prepare for executing commands within the Keycloak container.

```bash
docker compose exec keycloak bin/sh
```

2. Export the realm and users to the volume-mounted directory.

When executed, an ERROR is displayed, but the export is completed correctly.

```bash
/opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/import --users different_files --users-per-file 100 --realm master
/opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/import --users different_files --users-per-file 100 --realm saml-test
```
