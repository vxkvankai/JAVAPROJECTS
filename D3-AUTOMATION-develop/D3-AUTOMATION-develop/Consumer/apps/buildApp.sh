#!/bin/bash
APPDIR=~/d3-ios-app
cd $APPDIR
xcodebuild -scheme 'D3 Automation' -destination "platform=iOS,id=78a61bc9fe1b6f27f192c93af2428639946a88e8" -workspace "D3 Banking.xcworkspace"