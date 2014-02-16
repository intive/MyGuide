#!/bin/sh
# removing files "./" protection
BLS_TMP="${BLS_TMP//[[:space:]]/}"
BLS_TMP="${BLS_TMP:=__tmp}"

security delete-keychain ios-build.keychain
rm -f -r ./$BLS_TMP/*
rm -f ~/Library/MobileDevice/Provisioning\ Profiles/$PROFILE_NAME.mobileprovision