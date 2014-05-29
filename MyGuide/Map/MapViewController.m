//
//  MapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/2/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "MapViewController.h"
#import "UIImage+Compare.h"
#import "DetailsMapViewController.h"

@interface MapViewController ()

@property (nonatomic) MKCoordinateRegion lastGoodRegion;
@property (nonatomic) UIAlertView       *alertDistance;
@property (nonatomic) MKMapCamera       *lastGoodCamera;
@property (nonatomic) CLLocation        *zooCenterLocation;
@property (nonatomic) NSArray           *nearestAnimals;
@property (nonatomic) BOOL               isSlidingView;
@property (nonatomic) BOOL               slidingViewIsDown;
@property (nonatomic) BOOL               visitedLocationFlag;

@end

#pragma mark -
@implementation MapViewController
{
    Settings          *_settings;
    AFParsedData      *_data;
    AFVisitedPOIsData *_visitedPOIs;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.slidingViewIsDown = NO;
    
    _settings      = [Settings sharedSettingsData];
    _data          = [AFParsedData sharedParsedData];
    _visitedPOIs   = [AFVisitedPOIsData sharedData];
    _alertDistance = [self buildAlertView];
    _zooCenterLocation = [[CLLocation alloc] initWithLatitude:_settings.zooCenter.latitude longitude:_settings.zooCenter.longitude];

    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
    _rightSidebarButton.target = self.revealViewController;
    _rightSidebarButton.action = @selector(rightRevealToggle:);
    [self.view addGestureRecognizer: self.revealViewController.panGestureRecognizer];

    [self configureMapView];
    [self centerMap];
    [self showPaths];
    [self showJunctions];
    [self updateVisitedLocations];

    [self setTitle: NSLocalizedString(@"titleControllerMap", nil)];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self showAnimals];
    [self.mapToolbar.items.firstObject setEnabled:NO];
    [self updateVisitedLocations];
}

#pragma mark - Initial configuration
- (void)configureMapView
{
    _mapView.translatesAutoresizingMaskIntoConstraints = YES;
    _mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.mapView.delegate = self;
    _lastGoodCamera = [self.mapView.camera copy];

    [self configureToolbarItems];
    [self configureTableData];
    [self showUserPosition];
}

- (void)configureToolbarItems
{
    UIBarButtonItem *mapTypebButton = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"mapType"] style:UIBarButtonItemStylePlain target:self action:@selector(changeMapType)];
    mapTypebButton.enabled = YES;
    mapTypebButton.tintColor = [UIColor colorWithRed:1.0f green:0.584f blue:0.0f alpha:1.0f];
    MKUserTrackingBarButtonItem *button = [[MKUserTrackingBarButtonItem alloc] initWithMapView:self.mapView];
    button.customView.backgroundColor = [UIColor clearColor];
    button.customView.tintColor = [UIColor colorWithRed:1.0f green:0.584f blue:0.0f alpha:1.0f];
    UIBarButtonItem *fixedSpaceButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:nil action:nil];
    fixedSpaceButton.width = 218.5f;
    [self.mapToolbar setBackgroundImage:[[UIImage alloc] init] forToolbarPosition:UIToolbarPositionAny barMetrics:UIBarMetricsDefault];
    self.mapToolbar.items = @[button, fixedSpaceButton, mapTypebButton];
}
- (void)changeMapType
{
    if(self.mapView.mapType == MKMapTypeStandard){
        self.mapView.mapType = MKMapTypeHybrid;
    }
    else{
        self.mapView.mapType = MKMapTypeStandard;
    }
}
- (void)configureTableData
{
    _nearestAnimalsTableView.dataSource = self;
    _nearestAnimalsTableView.delegate   = self;
    [self prepareNearestAnimalsListImageViewForTapping];
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
        
        for(MKAnnotationAnimal *annotation in animals){
            MKCircle *circle = [MKCircle circleWithCenterCoordinate:annotation.coordinate radius:20];
            [self.mapView addOverlay:circle];
        }
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
    [self updateNearestAnimalsArrayWithLocation:userLocation.location];
    [_nearestAnimalsTableView reloadData];
    double distance = [self calculateUserDistance:userLocation];
    if(distance <= _settings.maxUserDistance){
        [[self.mapToolbar.items firstObject] setEnabled:YES];
    }
    else if(distance > _settings.maxUserDistance){
        [[self.mapToolbar.items firstObject] setEnabled:NO];
    }
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
    return distance > _settings.maxUserDistance && !_alertDistance.visible && _settings.showDistanceAlert;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex != alertView.cancelButtonIndex)
    {
        [self.navigationController pushViewController: [self getMapViewController] animated: YES];
    }
    _settings.showDistanceAlert = NO;
}

- (DetailsMapViewController *) getMapViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName: @"Main" bundle: [NSBundle mainBundle]];
    DetailsMapViewController *mapViewController = (DetailsMapViewController *)[storyboard instantiateViewControllerWithIdentifier: @"detailsMap"];
    [mapViewController showZOO];
    return mapViewController;
}

#pragma mark - Drawing paths on the map
- (void)drawPath:(NSArray *)nodesArray
{
    CLLocationCoordinate2D coordinatesArray[[nodesArray count]];
    NSUInteger i = 0;
    for(AFNode* node in nodesArray){
        CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(node.latitude, node.longitude);
        coordinatesArray[i++] = coordinate;
    }

    MKPolyline *path = [MKPolyline polylineWithCoordinates:coordinatesArray count:[nodesArray count]];
    [self.mapView addOverlay:path];
}

#pragma mark - Drawing junctions on the map
- (void)drawJunction:(AFNode *)node
{
    CLLocationCoordinate2D coordinatesArray[2];
    coordinatesArray[0] = CLLocationCoordinate2DMake(node.latitude, node.longitude);
    coordinatesArray[1] = CLLocationCoordinate2DMake(node.latitude, node.longitude);
    MKPolyline *junction = [MKPolyline polylineWithCoordinates:coordinatesArray count:2];
    [self.mapView addOverlay:junction];
}

- (MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay
{
    if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolyline *route = overlay;
        MKPolylineRenderer *routeRenderer = [[MKPolylineRenderer alloc] initWithPolyline:route];
        if(route.pointCount == 2 && fabs(route.points[0].x - route.points[1].x) < DBL_EPSILON){
            routeRenderer.strokeColor = [UIColor blackColor];
            routeRenderer.lineWidth = 5;
        }
        else{
            routeRenderer.strokeColor = [UIColor brownColor];
            routeRenderer.lineCap     = kCGLineCapRound;
            routeRenderer.lineJoin    = kCGLineJoinRound;
            routeRenderer.lineWidth   = 3;
            routeRenderer.alpha       = 0.7;
        }
        return routeRenderer;
    }
    else if([overlay isKindOfClass:[MKCircle class]]){
        MKCircleRenderer *circleRenderer = [[MKCircleRenderer alloc] initWithOverlay:overlay];
        circleRenderer.strokeColor = [UIColor redColor];
        circleRenderer.lineWidth = 3;
        circleRenderer.fillColor = [[UIColor redColor] colorWithAlphaComponent:0.4];
        return circleRenderer;
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

#pragma mark - Scrolling nearest animals list
- (void)prepareNearestAnimalsListImageViewForTapping
{
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapDetected)];
    singleTap.numberOfTapsRequired = 1;
    [self.nearestAnimalImageView addGestureRecognizer:singleTap];
}
-(void)tapDetected{
    [UIView beginAnimations:@"move" context:nil];
    [UIView setAnimationDuration:0.5];
    if(self.slidingViewIsDown) {
        _nearestAnimalImageView.frame=CGRectMake(75, self.view.frame.size.height - _nearestAnimalImageView.frame.size.height, _nearestAnimalImageView.frame.size.width, _nearestAnimalImageView.frame.size.height);
        self.slidingViewIsDown = NO;
    }
    else {
        _nearestAnimalImageView.frame=CGRectMake(75, 60, _nearestAnimalImageView.frame.size.width, _nearestAnimalImageView.frame.size.height);
        self.slidingViewIsDown = YES;
    }
    _nearestAnimalsList.frame=CGRectMake(0, _nearestAnimalImageView.frame.origin.y + _nearestAnimalImageView.frame.size.height, _nearestAnimalsList.frame.size.width, _nearestAnimalsList.frame.size.height);
    if([self.nearestAnimalImageView.image isEqualToImage:[UIImage imageNamed:@"arrowDown"]]){
        self.nearestAnimalImageView.image = [UIImage imageNamed:@"arrowUp"];
    }
    else{
        self.nearestAnimalImageView.image = [UIImage imageNamed:@"arrowDown"];
    }
    [UIView commitAnimations];
}

#pragma mark - Nearest animals list's table view

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 5;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellId = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
    }
    UIImageView *animalImage = (UIImageView *)[cell viewWithTag:100];
    animalImage.image = [UIImage imageNamed:[[[_nearestAnimals objectAtIndex:indexPath.section] animalInfoDictionary] valueForKey:@"adultImageName"]];

    UILabel *nameLabel = (UILabel *)[cell viewWithTag:101];
    nameLabel.text = [NSString stringWithFormat:@"%@", [[_nearestAnimals objectAtIndex:indexPath.section] name]];

    UITextView *funFact = (UITextView *)[cell viewWithTag:102];
    funFact.text = [NSString stringWithFormat:@"Fun Fact #%ld", (long)indexPath.section];

    UILabel *distanceLabel = (UILabel *)[cell viewWithTag:103];
    distanceLabel.text = [NSString stringWithFormat:@"%ldm", (long)[[_nearestAnimals objectAtIndex:indexPath.section] distanceFromUser]];

    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    AFNode* coordinates = [[_nearestAnimals objectAtIndex:indexPath.section] coordinates];
    double lat = coordinates.latitude;
    double lon = coordinates.longitude;
    CLLocationCoordinate2D selectedAnimalLocation =  CLLocationCoordinate2DMake(lat, lon);
    for(MKAnnotationAnimal *annotation in _mapView.annotations){
        if([self compareCoordinate:selectedAnimalLocation withCoordinate:annotation.coordinate]){
            [self.mapView selectAnnotation:annotation animated:YES];
            break;
        }
    }
}
- (void)updateNearestAnimalsArrayWithLocation:(CLLocation *)location
{
    for(AFAnimal *animal in _data.animalsArray){
        [animal setDistanceFromUser:[animal.coordinates distanceFromLocation:location]];
    }
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"distanceFromUser" ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    _nearestAnimals = [_data.animalsArray sortedArrayUsingDescriptors:sortDescriptors];
}
- (void)updateVisitedLocations
{
    for(AFAnimal *animal in _visitedPOIs.visitedPOIs){
        for(MKAnnotationAnimal *annotation in _mapView.annotations){
            if([annotation.title isEqualToString:animal.name]){
                _visitedLocationFlag = YES;
                [self.mapView selectAnnotation:annotation animated:YES];
                break;
            }
        }
    }
}
- (BOOL)compareCoordinate:(CLLocationCoordinate2D)coordinateOne withCoordinate:(CLLocationCoordinate2D)coordinateTwo
{
    return fabs(coordinateOne.latitude - coordinateTwo.latitude) <= DBL_EPSILON && fabs(coordinateOne.longitude - coordinateTwo.longitude) <= DBL_EPSILON;
}
- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKPinAnnotationView *)view
{
    if(_visitedLocationFlag){
        _visitedLocationFlag = NO;
        view.pinColor = MKPinAnnotationColorGreen;
    }
    else if(view.pinColor == MKPinAnnotationColorRed){
        view.pinColor = MKPinAnnotationColorPurple;
    }
}
- (void)mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKPinAnnotationView *)view
{
    if(view.pinColor == MKPinAnnotationColorPurple) {
        view.pinColor = MKPinAnnotationColorRed;
    }
}

@end
