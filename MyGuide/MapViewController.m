//
//  MapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/2/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "MapViewController.h"

@implementation MapViewController {
    Settings     *_settings;
    AFParsedData *_data;
    UIAlertView  *_alertDistance;
    BOOL         _showAlert;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _settings = [Settings sharedSettingsData];
    _data     = [AFParsedData sharedParsedData];
    _alertDistance = [self buildAlertView];
    _showAlert = YES;
    
    self.mapView.delegate = self;
    
    [self showUserPosition];
    [self showAnimals];
    [self centerMap];
}

- (UIAlertView *) buildAlertView {
    return [[UIAlertView alloc] initWithTitle: @"Too far from the zoo!"
                                      message: @"You are too far from the zoo. Do you want to show driving directions?"
                                     delegate: self
                            cancelButtonTitle: @"NO"
                            otherButtonTitles: @"YES", nil];
}

- (IBAction) centerOnCurrentLocation: (id) sender {
    CLLocation *userLocation = self.mapView.userLocation.location;
    if(userLocation) {
        [self.mapView setCenterCoordinate: userLocation.coordinate];
    }
}

- (void) showUserPosition {
    if(_settings.showUserPosition) {
        [self.mapView setShowsUserLocation: YES];
    }
}

- (void) showAnimals {
    if(_settings.showAnimalsOnMap) {
        NSArray *animals = [MKAnnotationAnimal buildAnimalMKAnnotations: _data.animalsArray];
        [self.mapView addAnnotations: animals];
    }
}

- (void) centerMap {
    [self.mapView setRegion: _settings.mapBounds animated: YES];
}

#pragma Showing AlertView depending on user distance

- (void) mapView:(MKMapView *) mapView didUpdateUserLocation: (MKUserLocation *) userLocation {
    double distance = [self calculateUserDistance: userLocation];
    if([self shouldShowAlertDistance: distance]) {
        [_alertDistance show];
    }
}

- (BOOL) shouldShowAlertDistance: (double) distance {
    return distance > _settings.maxUserDistance && !_alertDistance.visible && _showAlert;
}

- (double) calculateUserDistance: (MKUserLocation *) userLocation {
    CLLocationCoordinate2D mapCenter = _settings.mapCenter;
    CLLocation *zooLocation  = [[CLLocation alloc] initWithLatitude: mapCenter.latitude longitude: mapCenter.longitude];
    return [userLocation.location distanceFromLocation: zooLocation];
}

- (void) alertView: (UIAlertView *) alertView clickedButtonAtIndex: (NSInteger) buttonIndex {
    NSString *title = [alertView buttonTitleAtIndex: buttonIndex];
    
    if([title isEqualToString: @"YES"])
    {
        [self.tabBarController setSelectedIndex: 2];
    }
    _showAlert = NO;}


@end