//
//  AppDelegate.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AppDelegate.h"
#import "SettingsParser.h"
#import "AFParsedData.h"
#import "AFXMLParser.h"
#import "SettingsParser.h"
#import "LocationManager.h"
#import "GastronomyParser.h"
#import "HistoryParser.h"

@implementation AppDelegate {
    LocationManager *_locationManager;
    Settings *_sharedSettings;
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

- (void) parseGastronomy
{
    GastronomyParser *parser = [GastronomyParser new];
    [parser parse];
}

- (void) parseHistory
{
    HistoryParser *parser = [HistoryParser new];
    [parser parse];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary  *)launchOptions
{
    _sharedSettings = [Settings sharedSettingsData];

    [self parseDataXML];
    [self parseGastronomy];
    [self parseHistory];
    [self loadSettings];
    
    _locationManager = [LocationManager sharedLocationManager];
    [_locationManager requestLocationStatus];
    return YES;
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    _sharedSettings.currentLanguageCode = [[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString];
}

@end