//
//  AppDelegate.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AppDelegate.h"
#import "AFParsedData.h"
#import "AFXMLParser.h"
#import "SettingsParser.h"


@implementation AppDelegate

- (void) parseDataXML: (id) object
{
    AFXMLParser *parser = [[AFXMLParser alloc] init];
    [parser parse];
}

- (void) loadSettings: (id) object {
    SettingsParser *parser = [[SettingsParser alloc] init];
    [parser loadSettings];
}

- (BOOL)                   application: (UIApplication *) application
         didFinishLaunchingWithOptions: (NSDictionary  *) launchOptions
{
    [self performSelectorInBackground: @selector(parseDataXML:) withObject: nil];
    [self performSelectorInBackground: @selector(loadSettings:) withObject: nil];

    return YES;
}


@end