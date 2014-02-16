#!/bin/sh

if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
  echo "This is a pull request. No deployment will be done."
  exit 0
fi
if [[ "$TRAVIS_BRANCH" != "ios" ]]; then
  echo "Testing on a branch other than master. No deployment will be done."
  exit 0
fi

PROVISIONING_PROFILE="$HOME/Library/MobileDevice/Provisioning Profiles/$PROFILE_NAME.mobileprovision"
OUTPUTDIR="$PWD/build/Release-iphoneos"


echo "***************************"
echo "*        Signing          *"
echo "***************************"

xcrun -log -sdk iphoneos PackageApplication "$OUTPUTDIR/$APP_NAME.app" -o "$OUTPUTDIR/$APP_NAME.ipa" -sign "$DEVELOPER_NAME" -embed "$PROVISIONING_PROFILE"

zip -r -9 "$OUTPUTDIR/$APP_NAME.app.dSYM.zip" "$OUTPUTDIR/$APP_NAME.app.dSYM"

RELEASE_DATE=`date '+%d-%m-%Y %H:%M:%S'`
RELEASE_NOTES="Build: $TRAVIS_BUILD_NUMBER\nUploaded: $RELEASE_DATE"

if [ ! -z "$BLS_TF_TEAM_TOKEN" ] && [ ! -z "$BLS_TF_API_TOKEN" ]; then
  echo ""
  echo "***************************"
  echo "* Uploading to Testflight *"
  echo "***************************"
  curl http://testflightapp.com/api/builds.json \
    -F file="@$OUTPUTDIR/$APP_NAME.ipa" \
    -F dsym="@$OUTPUTDIR/$APP_NAME.app.dSYM.zip" \
    -F api_token="$BLS_TF_API_TOKEN" \
    -F team_token="$BLS_TF_TEAM_TOKEN" \
    -F distribution_lists='Internal' \
    -F notes="$RELEASE_NOTES"
fi