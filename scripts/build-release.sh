#!/bin/sh

if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
  echo "This is a pull request. No release build will be done."
  exit 0
fi

xctool -workspace MyGuide.xcworkspace -scheme MyGuide -sdk iphoneos -configuration Release OBJROOT=$PWD/build SYMROOT=$PWD/build ONLY_ACTIVE_ARCH=NO
