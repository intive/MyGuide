//
//  LocationManager.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "LocationManager.h"
#import "AFTracksData.h"
#import "AFParsedData.h"
#import "AFAnimal.h"

@interface LocationManager ()

@property (nonatomic) CLLocationManager *locationManager;
@property (nonatomic) AFTrack           *monitoredTrack;
@property (nonatomic) NSMutableArray    *animalIndexesArray;
@property (nonatomic) NSMutableSet      *backgroundLocationsArray;
@property (nonatomic) NSMutableString   *debugString;

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
    
    _debugString = [NSMutableString new];
    [self prepareFileToSaveLocationsForTesting];
    
    _animalIndexesArray       = [NSMutableArray new];
    _backgroundLocationsArray = [NSMutableSet new];
    _monitoredTrack           = [[AFTrack alloc] init];
}

#pragma mark - CLLocationMangerDelegate methods

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [self.debugString appendFormat:@"%@ didUpdateLocations with #%d locations\n", [(CLLocation*)locations.firstObject timestamp], locations.count];
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    if(![userDefaults boolForKey:@"isInBackground"]){
        [self.debugString appendFormat:@"is in foreground\n"];
        [self.debugString appendFormat:@"background locations count before(fr): %d\n", self.backgroundLocationsArray.count];

        [self.backgroundLocationsArray addObjectsFromArray:locations];
        
        [self.debugString appendFormat:@"background locations count after(fr): %d\n\n", self.backgroundLocationsArray.count];

        for(CLLocation *location in self.backgroundLocationsArray){
            NSMutableArray *tempAnimalIDArray = [NSMutableArray new];
            for(NSString *animalID in self.animalIndexesArray){
                AFAnimal *animal = [self findAnimalByID:animalID.integerValue];
                if([animal isWithinDistance:20 fromLocation:location]){
                    [self.monitoredTrack incrementProgress];
                    [tempAnimalIDArray addObject:animalID];
                }
            }
            [self.animalIndexesArray removeObjectsInArray:tempAnimalIDArray];
        }
    }
    else{
        [self.debugString appendFormat:@"is in background\n"];
        [self.debugString appendFormat:@"background locations count before(bg): %d\n", self.backgroundLocationsArray.count];
        
        [self.backgroundLocationsArray addObjectsFromArray:locations];
        
        [self.debugString appendFormat:@"background locations count after(bg): %d\n\n", self.backgroundLocationsArray.count];
    }
    [userDefaults synchronize];
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    [self displayLocationStatus: status withMessage: NSLocalizedString(@"gpsDisabledMapMessage", nil)];
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
    [self clearMonitoredTrack];
    self.monitoredTrack = currentTrack;
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];

    if(![[userDefaults valueForKeyPath:@"current track"] isEqualToString:EXPLORATION_TRACK_NAME]){
        self.animalIndexesArray = [self.monitoredTrack.notVisitedAnimalsArray mutableCopy];
    }
    [userDefaults synchronize];
}
- (void)clearMonitoredTrack
{
    [self saveCurrentTrackContext];
    self.animalIndexesArray = [NSMutableArray new];
    self.monitoredTrack = [[AFTrack alloc] init];
}
- (void)saveCurrentTrackContext
{
    if(![self.monitoredTrack.image isEqualToString:@""]){
        self.monitoredTrack.activeStatus = @"start";
        self.monitoredTrack.notVisitedAnimalsArray = [self.animalIndexesArray copy];
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
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracking_Debug.txt"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:path])
	{
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
}
- (void)saveLocationsForTesting
{
    NSString *path;
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracking_Debug.txt"];
    [self.debugString writeToFile:path atomically:YES encoding:NSUTF8StringEncoding error:nil];
}




@end