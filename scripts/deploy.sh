#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i
    target/

echo 'Restart server...'

ssh

pgrep java | xargs kill -9
nohup java -jar > log.txt &

EOF

echo 'Bye'