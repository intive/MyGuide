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
    
//    MKUserTrackingBarButtonItem                           <<<     FIND HOW SHOULD THIS WORK
    
    [self showUserPosition];
    [self showAnimals];
    [self centerMap];
    [self showPaths];
    [self showJunctions];
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

- (void)showPaths{
    if(_settings.showPathsOnMap)
    {
        for(AFWay* way in _data.waysArray) [self drawPath:way.nodesArray];
    }
}

- (void)showJunctions{
    if(_settings.showJunctionsOnMap)
    {
        for(AFJunction* junction in _data.junctionsArray) [self drawJunction:junction.coordinates];
    }
}

- (void) centerMap {
    [self.mapView setRegion: _settings.mapBounds animated: YES];
}

#pragma mark Showing AlertView depending on user distance

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
    _showAlert = NO;
}

#pragma mark Drawing paths on the map

- (void)drawPath:(NSArray *)nodesArray
{
    CLLocationCoordinate2D coordinatesArray[[nodesArray count]];
    int i=0;
    for(AFNode* node in nodesArray)
    {
        CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([node.latitude doubleValue], [node.longitude doubleValue]);
        coordinatesArray[i] = coordinate;
        i+=1;
    }
    
    MKPolyline *path = [MKPolyline polylineWithCoordinates:coordinatesArray count:[nodesArray count]];
    [self.mapView addOverlay:path];
}

#pragma mark Drawing junctions on the map

- (void)drawJunction:(AFNode *)node
{
    CLLocationCoordinate2D coordinatesArray[2];
    coordinatesArray[0] = CLLocationCoordinate2DMake([node.latitude doubleValue], [node.longitude doubleValue]);
    coordinatesArray[1] = CLLocationCoordinate2DMake([node.latitude doubleValue], [node.longitude doubleValue]);
    MKPolyline *junction = [MKPolyline polylineWithCoordinates:coordinatesArray count:2];
    [self.mapView addOverlay:junction];
}

- (MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay
{
    if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolyline *route = overlay;
        MKPolylineRenderer *routeRenderer = [[MKPolylineRenderer alloc] initWithPolyline:route];
        if(route.pointCount == 2 && (route.points[0].x == route.points[1].x)){
            routeRenderer.strokeColor = [UIColor blackColor];
            routeRenderer.lineWidth = 5;
        }
        else{
            routeRenderer.strokeColor = [UIColor brownColor];
            routeRenderer.lineCap = kCGLineCapRound;
            routeRenderer.lineJoin = kCGLineJoinRound;
//          routeRenderer.lineDashPattern = @[@20, @10];
            routeRenderer.lineWidth = 3;
            routeRenderer.alpha = 0.5;
        }
        return routeRenderer;
    }
    else return nil;
}

@end