//
//  MapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/2/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "MapViewController.h"

@interface MapViewController ()

@property (nonatomic) MKCoordinateRegion lastGoodRegion;
@property (nonatomic) UIAlertView       *alertDistance;
@property (nonatomic) MKMapCamera       *lastGoodCamera;
@property (nonatomic) CLLocation        *zooCenterLocation;
@property (nonatomic) BOOL               showAlert;

@end

#pragma mark -
@implementation MapViewController
{
    Settings        *_settings;
    AFParsedData    *_data;
    LocationManager *_locationManager;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _settings = [Settings sharedSettingsData];
    _data     = [AFParsedData sharedParsedData];
    _alertDistance = [self buildAlertView];
    _showAlert = YES;
    _zooCenterLocation = [[CLLocation alloc] initWithLatitude:_settings.zooCenter.latitude longitude:_settings.zooCenter.longitude];

    _sidebarButton.target    = self.revealViewController;
    _sidebarButton.action    = @selector(revealToggle:);
    [self.view addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    
    _locationManager = [LocationManager sharedLocationManager];
    [_locationManager checkLocationStatus];
    
    [self configureMapView];
    [self showAnimals];
    [self centerMap];
    [self showPaths];
    [self showJunctions];
}



#pragma mark - Initial configuration
- (void)configureMapView
{
    _mapView.translatesAutoresizingMaskIntoConstraints = YES;
    _mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.mapView.delegate = self;
    
    [self configureToolbarItems];
    [self showUserPosition];
}

- (void)configureToolbarItems
{
    MKUserTrackingBarButtonItem *button = [[MKUserTrackingBarButtonItem alloc] initWithMapView:self.mapView];
    self.mapToolbar.items = @[button];
    self.mapToolbar.translatesAutoresizingMaskIntoConstraints = YES;
}

- (void)showUserPosition
{
    if(_settings.showUserPosition) {
        [self.mapView setShowsUserLocation:YES];
    }
}

- (UIAlertView *)buildAlertView
{
    return [[UIAlertView alloc] initWithTitle: NSLocalizedString(@"distanceAlertTitle", nil)
                                      message: NSLocalizedString(@"distanceAlertMessage", nil)
                                     delegate: self
                            cancelButtonTitle: NSLocalizedString(@"NO", nil)
                            otherButtonTitles: NSLocalizedString(@"YES", nil), nil];
}

- (void)showAnimals
{
    if(_settings.showAnimalsOnMap) {
        NSArray *animals = [MKAnnotationAnimal buildAnimalMKAnnotations:_data.animalsArray];
        [self.mapView addAnnotations:animals];
    }
}

- (void)showPaths
{
    if(_settings.showPathsOnMap) {
        for(AFWay* way in _data.waysArray) [self drawPath:way.nodesArray];
    }
}

- (void)showJunctions
{
    if(_settings.showJunctionsOnMap) {
        for(AFJunction* junction in _data.junctionsArray) [self drawJunction:junction.coordinates];
    }
}

- (void)centerMap
{
    [self.mapView setRegion:_settings.mapBounds animated:YES];
}

#pragma mark - Showing AlertView depending on user distance
- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    double distance = [self calculateUserDistance:userLocation];
    if([self shouldShowAlertDistance:distance]) {
        [_alertDistance show];
    }
}

- (double)calculateUserDistance:(MKUserLocation *)userLocation
{
    CLLocationCoordinate2D mapCenter = _settings.mapCenter;
    CLLocation *zooLocation  = [[CLLocation alloc] initWithLatitude:mapCenter.latitude longitude:mapCenter.longitude];
    return [userLocation.location distanceFromLocation:zooLocation];
}

- (BOOL)shouldShowAlertDistance:(double)distance
{
    return distance > _settings.maxUserDistance && !_alertDistance.visible && _showAlert;
}


- (void) alertView: (UIAlertView *) alertView clickedButtonAtIndex: (NSInteger) buttonIndex
{
    if(buttonIndex != alertView.cancelButtonIndex)
    {
        UIViewController *fakeDrivingLocationController = [[UIViewController alloc] init];
        fakeDrivingLocationController.view.backgroundColor = [UIColor whiteColor];
        [self.navigationController pushViewController:fakeDrivingLocationController animated: YES];

    }
    _showAlert = NO;
}

#pragma mark - Drawing paths on the map
- (void)drawPath:(NSArray *)nodesArray
{
    CLLocationCoordinate2D coordinatesArray[[nodesArray count]];
    NSUInteger i = 0;
    for(AFNode* node in nodesArray){
        CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([node.latitude doubleValue], [node.longitude doubleValue]);
        coordinatesArray[i++] = coordinate;
    }
    
    MKPolyline *path = [MKPolyline polylineWithCoordinates:coordinatesArray count:[nodesArray count]];
    [self.mapView addOverlay:path];
}

#pragma mark - Drawing junctions on the map
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
        if(route.pointCount == 2 && fabs(route.points[0].x - route.points[1].x) < 1e-8){
            routeRenderer.strokeColor = [UIColor blackColor];
            routeRenderer.lineWidth = 5;
        }
        else{
            routeRenderer.strokeColor = [UIColor brownColor];
            routeRenderer.lineCap     = kCGLineCapRound;
            routeRenderer.lineJoin    = kCGLineJoinRound;
            routeRenderer.lineWidth   = 3;
            routeRenderer.alpha       = 0.5;
        }
        return routeRenderer;
    }
    else return nil;
}

#pragma mark - Limiting scroll and zoom levels

#define OLD_iOS_VERSION floor(NSFoundationVersionNumber) <= NSFoundationVersionNumber_iOS_6_1

- (void)mapView:(MKMapView *)mapView regionWillChangeAnimated:(BOOL)animated
{
    CLLocation *mapCenter = [[CLLocation alloc] initWithLatitude:mapView.centerCoordinate.latitude longitude:mapView.centerCoordinate.longitude];
    double distanceFromZooCenter = [mapCenter distanceFromLocation:_zooCenterLocation];
    
    if(distanceFromZooCenter <= _settings.centerRadius) {
        if(OLD_iOS_VERSION) {
            _lastGoodRegion = mapView.region;
        }
        else {
            _lastGoodCamera = [mapView.camera copy];
        }
    }
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated
{
    CLLocation *mapCenter = [[CLLocation alloc] initWithLatitude:mapView.centerCoordinate.latitude longitude:mapView.centerCoordinate.longitude];
    double distanceFromZooCenter = [mapCenter distanceFromLocation:_zooCenterLocation];

    if(OLD_iOS_VERSION) {
        if(distanceFromZooCenter > _settings.centerRadius) {
            [mapView setRegion:_lastGoodRegion animated:YES];
        }
        if (mapView.region.span.latitudeDelta  > _settings.maxSpan.latitudeDelta ||
            mapView.region.span.longitudeDelta > _settings.maxSpan.longitudeDelta)
        {
            [mapView setRegion:_lastGoodRegion animated:YES];
        }
        if (mapView.region.span.latitudeDelta  < _settings.minSpan.latitudeDelta ||
            mapView.region.span.longitudeDelta < _settings.minSpan.longitudeDelta)
        {
            [mapView setRegion:_lastGoodRegion animated:YES];
        }
    }
    else {
        if(distanceFromZooCenter > _settings.centerRadius) {
            [mapView setCamera:_lastGoodCamera animated:YES];
        }
        if (mapView.camera.altitude > _settings.cameraMaxAltitude) {
            MKMapCamera *maxAltitudeCamera = [mapView.camera copy];
            [maxAltitudeCamera setAltitude:2905];
            [mapView setCamera:maxAltitudeCamera animated:YES];
        }
        else if(mapView.camera.altitude < _settings.cameraMinAltitude){
            MKMapCamera *minAltitudeCamera = [mapView.camera copy];
            [minAltitudeCamera setAltitude:362];
            [mapView setCamera:minAltitudeCamera animated:YES];
        }
    }
}

@end