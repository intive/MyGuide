//
//  AppDelegate.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AppDelegate.h"

@implementation AppDelegate {
    LocationManager *_locationManager;
}



- (void)parseDataXML
{
    AFXMLParser *parser = [[AFXMLParser alloc] init];
    [parser parse];
}

- (void)loadSettings
{
    SettingsParser *parser = [[SettingsParser alloc] init];
    [parser loadSettings];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary  *)launchOptions
{
       dispatch_queue_t aQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(aQueue, ^{
        [self parseDataXML];
        [self loadSettings];
    });
    
    _locationManager = [LocationManager sharedLocationManager];
    [_locationManager requestLocationStatus];

    return YES;
}

@end