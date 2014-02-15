#!/bin/sh
mkdir -p ./$BLS_TMP

openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/profile/$PROFILE_NAME.mobileprovision.enc -d -a -out ./$BLS_TMP/$PROFILE_NAME.mobileprovision
openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/certs/dist.cer.enc -d -a -out ./$BLS_TMP/dist.cer
openssl aes-256-cbc -k "$BLS_ENCRYPTION_SECRET" -in scripts/certs/dist.p12.enc -d -a -out ./$BLS_TMP/dist.p12

security create-keychain -p travis ios-build.keychain
security import ./scripts/certs/apple.cer -k ~/Library/Keychains/ios-build.keychain -T /usr/bin/codesign
security import ./$BLS_TMP/dist.cer -k  ~/Library/Keychains/ios-build.keychain -T /usr/bin/codesign
security import ./$BLS_TMP/dist.p12 -k  ~/Library/Keychains/ios-build.keychain -P $BLS_KEY_PASSWORD -T /usr/bin/codesign
mkdir -p ~/Library/MobileDevice/Provisioning\ Profiles
mv ./$BLS_TMP/$PROFILE_NAME.mobileprovision ~/Library/MobileDevice/Provisioning\ Profiles/