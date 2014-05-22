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
#import "AFVisitedPOIsData.h"
#import "AFAnimal.h"

@interface LocationManager ()

@property (nonatomic) CLLocationManager *locationManager;
@property (nonatomic) AFTrack           *monitoredTrack;
@property (nonatomic) AFTrack           *explorationTrack;
@property (nonatomic) NSMutableArray    *animalIndexesArray;
@property (nonatomic) NSMutableArray    *explorationAnimalIndexesArray;
@property (nonatomic) NSMutableSet      *backgroundLocationsSet;

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
    
    _explorationTrack              = [[AFTrack alloc] init];
    _explorationAnimalIndexesArray = [NSMutableArray new];
    
    _animalIndexesArray     = [NSMutableArray new];
    _backgroundLocationsSet = [NSMutableSet new];
    _monitoredTrack         = [[AFTrack alloc] init];
}

#pragma mark - CLLocationMangerDelegate methods

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    if(![userDefaults boolForKey:@"isInBackground"]){
        [self.backgroundLocationsSet addObjectsFromArray:locations];
        for(CLLocation *location in self.backgroundLocationsSet){
            NSMutableArray *tempAnimalIDArray            = [NSMutableArray new];
            NSMutableArray *tempExplorationAnimalIDArray = [NSMutableArray new];
            for(NSString *animalID in self.animalIndexesArray){
                AFAnimal *animal = [self findAnimalByID:animalID.integerValue];
                if([animal isWithinDistance:20 fromLocation:location]){
                    [self.monitoredTrack incrementProgress];
                    [tempAnimalIDArray addObject:animalID];
                }
            }
            for(NSString *animalID in self.explorationAnimalIndexesArray){
                AFAnimal *animal = [self findAnimalByID:animalID.integerValue];
                if([animal isWithinDistance:20 fromLocation:location]){
                    [self.explorationTrack incrementProgress];
                    [[[AFVisitedPOIsData sharedData] visitedPOIs] addObject:animal];
                    [tempExplorationAnimalIDArray addObject:animalID];
                }
            }
            [self.explorationAnimalIndexesArray removeObjectsInArray:tempExplorationAnimalIDArray];
            [self.animalIndexesArray removeObjectsInArray:tempAnimalIDArray];
        }
        self.backgroundLocationsSet = [NSMutableSet new];
    }
    else{
        [self.backgroundLocationsSet addObjectsFromArray:locations];
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

- (void)loadVisitedPOIs
{
    NSMutableArray *tempIndexesArray = [self.explorationTrack.animalsArray mutableCopy];
    [tempIndexesArray removeObjectsInArray:self.explorationTrack.notVisitedAnimalsArray];
    NSMutableSet *tempAnimalsSet = [NSMutableSet new];
    for(NSString *index in tempIndexesArray){
        [tempAnimalsSet addObject:[self findAnimalByID:index.integerValue]];
    }
    [[AFVisitedPOIsData sharedData] setVisitedPOIs:tempAnimalsSet];
}
# define EXPLORATION_TRACK_NAME [[[[AFTracksData sharedParsedData] tracks] objectAtIndex:0] getName]
- (void)loadExplorationTrack
{
    [self checkBackgroundAppRefreshAvailability];
    self.explorationTrack              = [[[AFTracksData sharedParsedData] tracks] objectAtIndex:0];
    self.explorationAnimalIndexesArray = [self.explorationTrack.notVisitedAnimalsArray mutableCopy];
    self.explorationTrack.activeStatus = @"stop";
}
- (void)loadTrackRegionsToMonitor:(AFTrack *)currentTrack
{
    [self checkBackgroundAppRefreshAvailability];
    [self clearMonitoredTrack];
    self.explorationAnimalIndexesArray = [self.explorationTrack.notVisitedAnimalsArray mutableCopy];
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
    self.animalIndexesArray            = [NSMutableArray new];
    self.explorationAnimalIndexesArray = [NSMutableArray new];
    self.monitoredTrack                = [[AFTrack alloc] init];
}
- (void)saveCurrentTrackContext
{
    if(![self.monitoredTrack.image isEqualToString:@""]){
        self.monitoredTrack.activeStatus = @"start";
        self.monitoredTrack.notVisitedAnimalsArray = [self.animalIndexesArray copy];
        
        self.explorationTrack.notVisitedAnimalsArray = [self.explorationAnimalIndexesArray copy];
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