//
//  LocationManager.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "LocationManager.h"
#import "AFTracksData.h"
#import "AFVisitedPOIsData.h"
#import "AFParsedData.h"
#import "AFAnimal.h"

@interface LocationManager ()

@property (nonatomic) CLLocationManager *locationManager;
@property (nonatomic) AFTrack           *monitoredTrack;
@property (nonatomic) NSMutableArray    *animalsArray;
@property (nonatomic) NSMutableString   *locationsForTesting;
@property (nonatomic) NSMutableString   *regionsForTesting;

@end

@implementation LocationManager

+ (id)sharedLocationManager
{
    static LocationManager *sharedLocationManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedLocationManager = [[self alloc] init];
    });
    return sharedLocationManager;
}

#pragma mark - Location services

- (void)requestLocationStatus
{
    _locationManager = [[CLLocationManager alloc] init];
    [_locationManager setDistanceFilter:kCLDistanceFilterNone];
    [_locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
    [_locationManager setPausesLocationUpdatesAutomatically:NO];
    [_locationManager setActivityType:CLActivityTypeFitness];
    [_locationManager setDelegate: self];
    [_locationManager startUpdatingLocation];
    
    [self.regionsForTesting appendFormat:@"isRegionMonitoringAvailableForCLCircularRegion: %hhd\n", [CLLocationManager isMonitoringAvailableForClass:[CLCircularRegion class]]];

    
    _monitoredTrack = [[AFTrack alloc] init];
    
    _locationsForTesting = [NSMutableString new];
    _regionsForTesting   = [NSMutableString new];
    [self prepareFileToSaveLocationsForTesting];
}

#pragma mark - CLLocationMangerDelegate methods

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    // TODO check proximity to every animal from exploration track
    NSMutableString *temp = self.locationsForTesting;

    for(CLLocation *loc in locations){
        [temp appendFormat:@"%@ %f %f \n", loc.timestamp, loc.coordinate.latitude, loc.coordinate.longitude];
    }
    self.locationsForTesting = temp;
}

- (void)locationManager:(CLLocationManager *)manager didDetermineState:(CLRegionState)state forRegion:(CLRegion *)region
{
    [self.regionsForTesting appendFormat:@"determined state: %d for region: %@\n", state, region.identifier];
    for(AFAnimal *animal in self.monitoredTrack.animalsArray){
        if([animal.name isEqualToString:region.identifier]){
            [self.regionsForTesting appendFormat:@"self.animalsArray.count was: %d\n", self.animalsArray.count];
            [self.animalsArray removeObject:animal];
            [self.regionsForTesting appendFormat:@"self.animalsArray.count is: %d\n", self.animalsArray.count];
        }
    }
    [self.regionsForTesting appendFormat:@"incrementing progress (%d) of track: %@\n", self.monitoredTrack.progressBase, [self.monitoredTrack getName]];
    [self.monitoredTrack incrementProgress];
    [self.regionsForTesting appendFormat:@"incremented progress (%d) of track: %@\n", self.monitoredTrack.progressBase, [self.monitoredTrack getName]];
    [self.regionsForTesting appendFormat:@"stopping monitoring for region: %@\n", region.identifier];
    [self.locationManager stopMonitoringForRegion:region];
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    [self displayLocationStatus: status withMessage: NSLocalizedString(@"gpsDisabledMapMessage", nil)];
}

- (void)locationManager:(CLLocationManager *)manager monitoringDidFailForRegion:(CLRegion *)region withError:(NSError *)error
{
    [self.regionsForTesting appendFormat:@"\t\t! monitoring did fail for region: %@ with error: %@\n", region.identifier, error];
}
- (void)locationManager:(CLLocationManager *)manager didStartMonitoringForRegion:(CLRegion *)region
{
    [self.regionsForTesting appendFormat:@"started monitoring for region: %@\n", region.identifier];
}

#pragma mark - Helper methods

- (void)displayLocationStatus:(CLAuthorizationStatus)status withMessage:(NSString *)message
{
    if(status == kCLAuthorizationStatusDenied) {
        [self showLocationServicesAlert:message];
    }
}

- (void)showLocationServicesAlert:(NSString *)message
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle: NSLocalizedString(@"gpsDeniedTitle", nil)
                                                        message: message
                                                       delegate: nil
                                              cancelButtonTitle: NSLocalizedString(@"OK", nil)
                                              otherButtonTitles: nil];
    [alertView show];
}

# define EXPLORATION_TRACK_NAME [[[[AFTracksData sharedParsedData] tracks] objectAtIndex:0] getName]
- (void)loadTrackRegionsToMonitor:(AFTrack *)currentTrack
{
    [self.regionsForTesting appendFormat:@"monitored track: %@\n", [currentTrack getName]];
    [self clearMonitoredTracks];
    self.monitoredTrack = currentTrack;
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [self.regionsForTesting appendFormat:@"user defaults track: %@\t(keyPath)\n", [userDefaults valueForKeyPath:@"current track"]];
    [self.regionsForTesting appendFormat:@"user defaults track: %@\t(key)\n", [userDefaults valueForKey:@"current track"]];

    if(![[userDefaults valueForKeyPath:@"current track"] isEqualToString:EXPLORATION_TRACK_NAME]){
        self.animalsArray = [self.monitoredTrack.visitedAnimalsArray copy];
        [self.regionsForTesting appendFormat:@"self.animalsArray.count: %lu\n", (unsigned long)self.animalsArray.count];
        for(NSInteger i=0; i<self.animalsArray.count; i++){
            [self.regionsForTesting appendFormat:@"checking animal id: %d\n", [[self.animalsArray objectAtIndex:i] integerValue]];
            AFAnimal *animal = [self findAnimalByID:[[self.animalsArray objectAtIndex:i] integerValue]];
            [self.regionsForTesting appendFormat:@"found animal: %@\n", [animal name]];
            CLCircularRegion *region = [[CLCircularRegion alloc] initWithCenter:animal.getLocationCoordinate radius:20.0 identifier:animal.name];
            [self.regionsForTesting appendFormat:@"created region: %@ %f %f\n", region.identifier, region.center.latitude, region.center.longitude];
            [self.locationManager startMonitoringForRegion:region];
            [self.regionsForTesting appendFormat:@"self.locationManager.monitoredRegions.count after start: %d\n", self.locationManager.monitoredRegions.count];
        }
    }
    [userDefaults synchronize];
}
- (void)clearMonitoredTracks
{
    [self.regionsForTesting appendFormat:@"clearing monitored tracks of track: %@\n", [self.monitoredTrack getName]];
    [self saveCurrentTrackContext];
    for(CLCircularRegion *region in self.locationManager.monitoredRegions){
        [self.regionsForTesting appendFormat:@"stopping monitoring for region: %@\n", region.identifier];
        [self.locationManager stopMonitoringForRegion:region];
        [self.regionsForTesting appendFormat:@"self.locationManager.monitoredRegions.count after stop: %d\n", self.locationManager.monitoredRegions.count];
    }
    self.monitoredTrack = [[AFTrack alloc] init];
}
- (void)saveCurrentTrackContext
{
    [self.regionsForTesting appendFormat:@"saving context of track: %@\n", [self.monitoredTrack getName]];
    if(![self.monitoredTrack.image isEqualToString:@""]){
        [self.regionsForTesting appendFormat:@"saved track was proper, it's activeStatus was: %@\n", [self.monitoredTrack activeStatus]];
        self.monitoredTrack.activeStatus = @"start";
        [self.regionsForTesting appendFormat:@"saved track was proper, it's visitedAnimalsArray.count was: %d\n", self.monitoredTrack.visitedAnimalsArray.count];
        self.monitoredTrack.visitedAnimalsArray = [self.animalsArray copy];
        [self.regionsForTesting appendFormat:@"saved track was proper, it's visitedAnimalsArray.count is: %d\n", self.monitoredTrack.visitedAnimalsArray.count];
    }
}

- (AFAnimal *)findAnimalByID:(NSInteger)animalID
{
    AFAnimal *animalToReturn = nil;
    for(AFAnimal *animal in [[AFParsedData sharedParsedData] localizeAnimalsArray]){
        if(animal.animalID == animalID){
            animalToReturn = animal;
        }
    }
    return animalToReturn;
}
- (void)prepareFileToSaveLocationsForTesting
{
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Locations.txt"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:path])
	{
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
    path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Regions.txt"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:path])
	{
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
}
- (void)saveLocationsForTesting
{
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Locations.txt"];
    [self.locationsForTesting writeToFile:path atomically:YES encoding:NSUTF8StringEncoding error:nil];
    
    path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Regions.txt"];
    [self.regionsForTesting writeToFile:path atomically:YES encoding:NSUTF8StringEncoding error:nil];
}

@end