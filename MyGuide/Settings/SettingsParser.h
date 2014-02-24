//
//  SettingsParser.h
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SettingParserDelegate.h"
#import "Settings.h"
#import "XMLFetcher.h"

extern NSString * const CONFIG_FILE_NAME;
@interface SettingsParser : NSObject
- (void) loadSettings;
@end