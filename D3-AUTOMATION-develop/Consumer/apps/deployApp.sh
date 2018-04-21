#!/bin/bash
#This will deploy the D3 Banking app to an iOS Simulator
APPDIR=~/d3-ios-app
cd $APPDIR
xcodebuild test -scheme 'D3 Automation' -destination "platform=iOS Simulator,OS=8.4,name=iPhone 6" -workspace "D3 Banking.xcworkspace"