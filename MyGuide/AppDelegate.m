//
//  AppDelegate.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AppDelegate.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [self performSelectorInBackground:@selector(loadSettings:) withObject:nil];
    return YES;
}

- (void) loadSettings: (id) object {
    SettingsParser *parser = [[SettingsParser alloc] init];
    [parser loadSettings];
}

@end
