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
    
    _animalIndexesArray       = [NSMutableArray new];
    _backgroundLocationsArray = [NSMutableSet new];
    _monitoredTrack           = [[AFTrack alloc] init];
}

#pragma mark - CLLocationMangerDelegate methods

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    if(![userDefaults boolForKey:@"isInBackground"]){
        [self.backgroundLocationsArray addObjectsFromArray:locations];
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
        [self.backgroundLocationsArray addObjectsFromArray:locations];
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
    [self checkBackgroundAppRefreshAvailability];
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
- (void)checkBackgroundAppRefreshAvailability
{
    if(UIApplication.sharedApplication.backgroundRefreshStatus == UIBackgroundRefreshStatusDenied){
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle: NSLocalizedString(@"backgroundAppRefreshDeniedTitle", nil)
                                                            message: NSLocalizedString(@"backgroundAppRefreshDeniedMessage", nil)
                                                           delegate: nil
                                                  cancelButtonTitle: NSLocalizedString(@"OK", nil)
                                                  otherButtonTitles: nil];
        [alertView show];
    }
}

@end