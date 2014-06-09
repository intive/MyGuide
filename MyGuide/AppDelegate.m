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

#pragma mark - delegate methods
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary  *)launchOptions
{
    if([self hasBeenLaunched])
    {
        [[LocationManager sharedLocationManager] requestLocationStatus];
        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
        [userDefaults setBool:NO forKey:@"isInBackground"];
        [userDefaults setValue:[[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString] forKey:@"current language code"];
        [userDefaults synchronize];
    }
    [self loadXMLs];
    [self styleApplication];
    [[LocationManager sharedLocationManager] loadExplorationTrack];
    [[LocationManager sharedLocationManager] loadVisitedPOIs];
    
    return YES;
}
- (void)applicationWillEnterForeground:(UIApplication *)application
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setBool:NO forKey:@"isInBackground"];
    [userDefaults setValue:[[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString] forKey:@"current language code"];
    [userDefaults synchronize];
}
- (void)applicationDidEnterBackground:(UIApplication *)application
{
    [self archiveTracks];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setBool:YES forKey:@"isInBackground"];
    [userDefaults synchronize];
}

#pragma mark - helper methods
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
- (void)archiveTracks
{
    AFTracksData *sharedData = [AFTracksData sharedParsedData];
    [self checkAndCreateTracksPlist];
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracks.plist"];
    
    [NSKeyedArchiver archiveRootObject:sharedData.tracks toFile:path];
}

- (void)styleApplication
{
    [[UIApplication sharedApplication] keyWindow].backgroundColor = [UIColor colorWithRed:1.0f green:0.584f blue:0.0f alpha:1.0f];
}

- (BOOL)hasBeenLaunched
{
    BOOL result = YES;
    NSString *hasBeenLaunched = @"HAS_BEEN_LAUNCHED";
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];

    if ([defaults boolForKey:hasBeenLaunched]){
        result = YES;
    }
    else{
        [defaults setBool:YES forKey:hasBeenLaunched];
        [defaults synchronize];
        result = NO;
    }
    return result;
}

@end
