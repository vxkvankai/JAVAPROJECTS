#!/bin/bash
#This will start the ios_webkit_debug_proxy for accessing web views on real iOS devices
PATH=$PATH:/usr/local/bin
HOMEDIR=/usr/local/bin

connected_devices () {
	cd $HOMEDIR
	idevice_id --list
}

deviceId=$(connected_devices)
echo "deviceId=$deviceId"

startProxy () {
	if [ -z "$deviceId" ]
	then
		echo "No connected devices found. Starting ios_webkit_debug_proxy for iOS Simulator."
		cd $HOMEDIR
		ios_webkit_debug_proxy		
	else
		echo "Starting ios_webkit_debug_proxy on port 27753 for connected device with ID = $deviceId."
		cd $HOMEDIR
		ios_webkit_debug_proxy -c $deviceId:27753
	fi
}

startProxy