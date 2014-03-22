//
//  LocationManager.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "LocationManager.h"

@implementation LocationManager {
    CLLocationManager *_locationManager;
}

+ (id) sharedLocationManager
{
    static LocationManager *sharedLocationManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedLocationManager = [[self alloc] init];
    });
    return sharedLocationManager;
}

#pragma mark - Location services

- (void) requestLocationStatus
{
    _locationManager = [[CLLocationManager alloc] init];
    [_locationManager setDelegate: self];
    [_locationManager startUpdatingLocation];
}

- (void) checkLocationStatus
{
    [self displayLocationStatus: [CLLocationManager authorizationStatus]
                    withMessage: NSLocalizedString(@"gpsDisabledLaunchMessage", nil)];
}

#pragma mark - CLLocationMangerDelegate methods

- (void) locationManager: (CLLocationManager *) manager didFailWithError: (NSError *) error {
    [_locationManager stopUpdatingLocation];
}

- (void) locationManager: (CLLocationManager *) manager didUpdateLocations: (NSArray *) locations {
    [_locationManager stopUpdatingLocation];
}

- (void) locationManager: (CLLocationManager *) manager didChangeAuthorizationStatus:(CLAuthorizationStatus) status {
    [self displayLocationStatus: status
                    withMessage: NSLocalizedString(@"gpsDisabledMapMessage", nil)];
}

#pragma mark - Helper methods

- (void) displayLocationStatus: (CLAuthorizationStatus) status withMessage: (NSString *) message
{
    if(status == kCLAuthorizationStatusDenied) {
        [self showLocationServicesAlert: message];
    }
}

- (void) showLocationServicesAlert: (NSString *) message
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle: NSLocalizedString(@"gpsDeniedTitle", nil)
                                                        message: message
                                                       delegate: nil
                                              cancelButtonTitle: NSLocalizedString(@"OK", nil)
                                              otherButtonTitles: nil];
    [alertView show];
    alertView = nil;
}

@end