#!/bin/sh

mysqladmin -uroot drop shachi -f > /dev/null 2>&1
mysqladmin -uroot create shachi
echo "Database shachi_test created"
mysql -uroot shachi < db/schema.sql

mysqladmin -uroot drop shachi_test -f > /dev/null 2>&1
mysqladmin -uroot create shachi_test
echo "Database shachi_test created"
mysql -uroot shachi_test < db/schema.sql

echo "Done"
