#!/bin/sh

if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
  echo "This is a pull request. No encription will be done."
  exit 0
fi

# removing files "./" protection
BLS_TMP="${BLS_TMP//[[:space:]]/}"
BLS_TMP="${BLS_TMP:=__tmp}"

mkdir -p ./$BLS_TMP

openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/profile/$PROFILE_NAME.mobileprovision.enc -d -a -out ./$BLS_TMP/$PROFILE_NAME.mobileprovision
openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/certs/dist.cer.enc -d -a -out ./$BLS_TMP/dist.cer
openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/certs/dist.p12.enc -d -a -out ./$BLS_TMP/dist.p12

security create-keychain -p travis ios-build.keychain
security default-keychain -s ios-build.keychain
security unlock-keychain -p travis ios-build.keychain
security import ./scripts/certs/apple.cer -k ~/Library/Keychains/ios-build.keychain -T /usr/bin/codesign
security import ./$BLS_TMP/dist.cer -k  ~/Library/Keychains/ios-build.keychain -T /usr/bin/codesign
security import ./$BLS_TMP/dist.p12 -k  ~/Library/Keychains/ios-build.keychain -P $BLS_KEY_PASSWORD -T /usr/bin/codesign
mkdir -p ~/Library/MobileDevice/Provisioning\ Profiles
mv ./$BLS_TMP/$PROFILE_NAME.mobileprovision ~/Library/MobileDevice/Provisioning\ Profiles/