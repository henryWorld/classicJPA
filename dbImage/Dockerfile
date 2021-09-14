FROM mcr.microsoft.com/mssql/server:2019-latest

WORKDIR /

ENV ACCEPT_EULA=Y
ENV SA_PASSWORD=local@SaPassword

COPY initServer.sh .

EXPOSE 1433

USER root
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./initServer.sh
USER mssql

CMD ./initServer.sh & /opt/mssql/bin/sqlservr
