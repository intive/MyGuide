//
//  DetailsMapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "DetailsMapViewController.h"
#import "Settings.h"

@interface DetailsMapViewController ()

@property (nonatomic) CLLocationCoordinate2D destinationCoordinates;
@property (nonatomic) Settings *sharedSettings;
@property (nonatomic) CLLocationManager *locationManager;

@end

@implementation DetailsMapViewController

double const ZOOM_LEVEL = 15;

- (instancetype)initWithCoder:(NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        self.sharedSettings = [Settings sharedSettingsData];
    }
    return self;
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self setDestinationCoordinates];
    [self drawCoordinatesOnMap: self.destinationCoordinates];
    [self setupMapView];
    
    [self prepareLocationManager];
    if (self.showDirections) {
        [self drawDirectionsToLocation];
    }
    else {
        [self zoomOnLocation: self.destinationCoordinates];
    }
}

- (void) setupMapView
{
    [self.mapView setShowsUserLocation: YES];
    [self.mapView setMapType: MKMapTypeSatellite];
    self.mapView.delegate = self;
}

- (void) zoomOnLocation: (CLLocationCoordinate2D) coordinates
{
    MKCoordinateSpan span = MKCoordinateSpanMake(180 / pow(2, ZOOM_LEVEL) * self.mapView.frame.size.height / 256, 0);
    [self.mapView setRegion: MKCoordinateRegionMake(coordinates, span) animated: YES];
}

- (void) drawCoordinatesOnMap: (CLLocationCoordinate2D) coordinates
{
    MKPointAnnotation *annotationPoint = [MKPointAnnotation new];
    annotationPoint.title       = self.nameToDisplay;
    annotationPoint.coordinate  = coordinates;
    [self.mapView addAnnotation: annotationPoint];
}

- (void) setDestinationCoordinates
{
    self.destinationCoordinates = CLLocationCoordinate2DMake(self.latitude.doubleValue, self.longitude.doubleValue);
}

- (void) showZOO
{
    self.showDirections = YES;
    self.nameToDisplay  = @"ZOO";
    self.latitude  = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.latitude];
    self.longitude = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.longitude];
}

- (void) prepareLocationManager
{
    self.locationManager = [CLLocationManager new];
    [self.locationManager setDelegate:self];
    [self.locationManager setDistanceFilter: kCLHeadingFilterNone];
    [self.locationManager startUpdatingLocation];
}

- (void) locationManager: (CLLocationManager *)manager
      didUpdateLocations: (NSArray *)locations
{
    [self drawDirectionsToLocation];
}

# pragma mark - Rendering directions

- (void) drawDirectionsToLocation
{
    MKMapItem *sourceMapItem = [MKMapItem mapItemForCurrentLocation];
    [sourceMapItem setName: NSLocalizedString(@"yourLocation", nil)];
    
    MKPlacemark *destination = [[MKPlacemark alloc] initWithCoordinate: self.destinationCoordinates addressDictionary: nil];
    MKMapItem *destinationMapItem = [[MKMapItem alloc] initWithPlacemark: destination];
    [destinationMapItem setName: self.nameToDisplay];
    
    MKDirectionsRequest *request = [MKDirectionsRequest new];
    [request setSource: sourceMapItem];
    [request setDestination: destinationMapItem];
    
    MKDirections *direction = [[MKDirections alloc] initWithRequest: request];
    [direction calculateDirectionsWithCompletionHandler: ^(MKDirectionsResponse *response, NSError *error) {
        if (error) {
            NSLog(@"There was an error getting your directions.");
            NSLog(@"%@", error.userInfo[@"NSLocalizedFailureReason"]);
            NSLog(@"%@", error.userInfo[@"NSLocalizedDescription"]);
            [self zoomOnLocation: self.destinationCoordinates];
            return;
        }
        
        [self setMapRegion];
        MKRoute *route = [response.routes firstObject];
        NSArray *steps = [route steps];
        [self.mapView removeOverlay: self.mapView.overlays.lastObject];
        [self.mapView addOverlay: [route polyline]];
        
        NSLog(@"Total Distance (in Meters) : %.0f", route.distance);
        NSLog(@"Total Steps : %lu",(unsigned long)[steps count]);
        
        [steps enumerateObjectsUsingBlock: ^(id obj, NSUInteger idx, BOOL *stop) {
            NSLog(@"Route Instruction : %@",[obj instructions]);
            NSLog(@"Route Distance : %.0f",   [obj distance]);
        }];
    }];
}

- (MKOverlayRenderer *) mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay
{
    MKPolylineRenderer *renderer = [[MKPolylineRenderer alloc] initWithPolyline: overlay];
    renderer.strokeColor = [UIColor orangeColor];
    renderer.lineWidth = 4.0;
    return  renderer;
}

- (void) setMapRegion
{
    const double mapPadding = 1.2;
    const double minimumVerticalSpan = .1;
    
    CLLocationCoordinate2D userCoordinate        = self.mapView.userLocation.coordinate;
    CLLocationCoordinate2D destinationCoordinate = self.destinationCoordinates;
    
    double minLatitude  = MIN(destinationCoordinate.latitude,  userCoordinate.latitude);
    double minLongitude = MIN(destinationCoordinate.longitude, userCoordinate.longitude);
    double maxLatitude  = MAX(destinationCoordinate.latitude,  userCoordinate.latitude);
    double maxLongitude = MAX(destinationCoordinate.longitude, userCoordinate.longitude);
    
    MKCoordinateRegion region;
    region.center.latitude      = (minLatitude + maxLatitude) / 2;
    region.center.longitude     = (minLongitude + maxLongitude) / 2;
    region.span.latitudeDelta   = (maxLatitude - minLatitude) * mapPadding;
    region.span.latitudeDelta   = (region.span.latitudeDelta < minimumVerticalSpan)
    ? minimumVerticalSpan
    : region.span.latitudeDelta;
    region.span.longitudeDelta  = (maxLongitude - minLongitude) * mapPadding;
    
    MKCoordinateRegion scaledRegion = [self.mapView regionThatFits: region];
    [self.mapView setRegion:scaledRegion animated:YES];
}

@end