#!/usr/bin/env bash

export DEBIAN_FRONTEND=noninteractive

apt-get autoclean
apt-get autoremove

cp --force /vagrant/vagrant-scripts/sources.list /etc/apt/sources.list
apt-get --quiet --yes --target-release wheezy-backports update
apt-get --quiet --yes --target-release wheezy-backports upgrade

# NFS Client
apt-get --quiet --yes --target-release wheezy-backports install nfs-common

# PostgreSQL
apt-get --quiet --yes --target-release wheezy-backports install postgresql
apt-get --quiet --yes --target-release wheezy-backports install postgresql-client
echo "listen_addresses = '*'" >> /etc/postgresql/9.1/main/postgresql.conf
echo "host all all 10.0.0.0/16 trust" >> /etc/postgresql/9.1/main/pg_hba.conf
su - postgres -c "psql -f /vagrant/dw-bootstrap.sql"
su - vagrant
service postgresql restart

# Java
apt-get --quiet --yes --target-release wheezy-backports install openjdk-7-jdk

apt-get autoclean
apt-get autoremove
