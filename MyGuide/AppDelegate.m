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
    NSLog(@"checking if file exists at path %@", path);
	if (![[NSFileManager defaultManager] fileExistsAtPath:path])
	{
        NSLog(@"%d file created", [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil]);
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
- (void)applicationWillResignActive:(UIApplication *)application
{
    AFTracksData *sharedData = [AFTracksData sharedParsedData];
    NSLog(@"%ld count", (unsigned long)sharedData.tracks.count);
}
- (void)applicationDidEnterBackground:(UIApplication *)application
{
    NSLog(@"saving data");
    AFTracksData *sharedData = [AFTracksData sharedParsedData];
    NSLog(@"%ld count", (unsigned long)sharedData.tracks.count);
//    NSString *path = [[NSBundle mainBundle] pathForResource:@"Tracks2" ofType:@"plist"];
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracks.plist"];

    NSLog(@"archiving to %@", path);
    NSLog(@"%d file archived", [NSKeyedArchiver archiveRootObject:sharedData.tracks toFile:path]);
    NSLog(@"saved data");
}
- (void)applicationWillTerminate:(UIApplication *)application
{
    NSLog(@"will terminate");
    [self applicationDidEnterBackground:application];
}
@end
