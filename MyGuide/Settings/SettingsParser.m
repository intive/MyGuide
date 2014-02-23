//
//  SettingsParser.m
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "SettingsParser.h"

NSString * const CONFIG_FILE_NAME = @"config";

@implementation SettingsParser {
    SettingParserDelegateCallback _callback;
}

- (id) init {
    self = [super init];
    if(self) {
        _callback = [self buildCallbackBlock];
    }
    return self;
}

- (void) loadSettings {
    NSData                *configData = [XMLFetcher fetchDataFromXML: CONFIG_FILE_NAME];
    NSXMLParser           *parser     = [[NSXMLParser alloc] initWithData: configData];
    SettingParserDelegate *delegate   = [[SettingParserDelegate alloc] initWithCallback: _callback];
    [parser setDelegate: delegate];
    [parser parse];
}

- (SettingParserDelegateCallback) buildCallbackBlock {
    Settings *settings = [Settings sharedSettingsData];
    return ^(NSString *name, NSMutableString *value) {
        if ([name isEqualToString: @"configuration"]) return;
        MWLogInfo(@"Injecting %@ with value \"%@\" to settings.", name, value);
        [settings injectDataWithName: name andValue: value];
    };
}
@end