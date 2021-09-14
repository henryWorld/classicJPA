#!/bin/bash

# I really don't like adding manual sleeps in here, but it's not working without it, and
# it's something that microsoft's own examples do:
# https://github.com/microsoft/sql-server-samples/blob/master/samples/containers/dtc/dtc1/db-init.sh
sleep 10s

# The -l 60 sets a login timeout of 60 seconds. This is important since the sql server doesn't
# start until we call sqlservr
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P local@SaPassword -l 60 -Q "CREATE DATABASE clinical_microservice"
