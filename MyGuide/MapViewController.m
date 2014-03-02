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
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _settings = [Settings sharedSettingsData];
    _data     = [AFParsedData sharedParsedData];
    
    [self showUserPosition];
    [self showAnimals];
    [self centerMap];
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
    [self.mapView setRegion: _settings.mapBounds animated:YES];
}

@end