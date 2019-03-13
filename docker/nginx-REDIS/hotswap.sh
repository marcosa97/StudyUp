#! /bin/bash

# a script to change to local or public images

# vars
PARAM=$1 
FILE="/etc/nginx/nginx.conf" 

# First check if PARAM already is the configuration 
if grep -q "$PARAM:6379" $FILE 
then
  echo "Parameter passed is the same as what is already being used. Try a different one."
  exit
fi

# If PARAM doesn't match what is already being used, replace it so it is.

NEWPARAM="    server $PARAM:6379"
PORT="6379"
sed -n 11p $FILE | sed -i '11 s/.*://' $FILE  # delete everything before semicolom
sed -i "11 s/$PORT/$NEWPARAM/" $FILE  # put server PARAM before semicolom

exec /usr/sbin/nginx -s reload
