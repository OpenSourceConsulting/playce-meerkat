#!/bin/sh
CNT=$#
if [ $CNT = 2 ] ; then
	ORIG_HOST_NAME=`hostname`
    IP_ADDRESS=$1
    NEW_HOST_NAME=$2
    hostname $NEW_HOST_NAME
    sed -i '/'${IP_ADDRESS}'/d' /etc/hosts
    echo $IP_ADDRESS $NEW_HOST_NAME >> /etc/hosts
    sed -i 's/localhost.localdomain/'${NEW_HOST_NAME}'/g' /etc/sysconfig/network
    sed -i 's/'${ORIG_HOST_NAME}'/'${NEW_HOST_NAME}'/g' /etc/sysconfig/network
else
   echo "IP Address & Host Name are required."
fi