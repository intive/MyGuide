//
//  AppDelegate.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AppDelegate.h"
#import "SettingsParser.h"
#import "HistoryParser.h"
#import "GastronomyParser.h"
#import "AFXMLParser.h"
#import "InformationParser.h"
#import "AFEventsParser.h"
#import "LocationManager.h"
#import "AFTracksParser.h"
#import "AFTracksData.h"

@implementation AppDelegate

- (void)loadXMLs
{
    [[SettingsParser new] loadSettings];
    [[HistoryParser     new] parse];
    [[GastronomyParser  new] parse];
    [[AFXMLParser       new] parse];
    [[InformationParser new] parse];
    [[AFEventsParser    new] parse];
    [[AFTracksParser    new] parse];
}

- (void)checkAndCreateTracksPlist
{
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracks.plist"];

	if (![[NSFileManager defaultManager] fileExistsAtPath:path])
	{
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary  *)launchOptions
{
    [[LocationManager sharedLocationManager] requestLocationStatus];
    [self checkAndCreateTracksPlist];
    [self loadXMLs];
    
    return YES;
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    Settings *sharedSettings = [Settings sharedSettingsData];
    sharedSettings.currentLanguageCode = [[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString];
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    AFTracksData *sharedData = [AFTracksData sharedParsedData];
    
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracks.plist"];

    [NSKeyedArchiver archiveRootObject:sharedData.tracks toFile:path];
}
- (void)applicationWillTerminate:(UIApplication *)application
{
    [self applicationDidEnterBackground:application];
}
@end
