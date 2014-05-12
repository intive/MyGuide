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

@property BOOL fitToPath;

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
    self.fitToPath = YES;
    [self drawCoordinatesOnMap];
    [self setupMapView];
    
    if (self.showDirections) {
        [self drawDirectionsToLocation];
        [self setMapRegion];
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

- (void)      mapView: (MKMapView *)      mapView
didUpdateUserLocation: (MKUserLocation *) userLocation
{
    [self drawDirectionsToLocation];
    if (self.fitToPath) [self setMapRegion];
}

- (void) zoomOnLocation: (CLLocationCoordinate2D) coordinates
{
    MKCoordinateSpan span = MKCoordinateSpanMake(180 / pow(2, ZOOM_LEVEL) * self.mapView.frame.size.height / 256, 0);
    [self.mapView setRegion: MKCoordinateRegionMake(coordinates, span) animated: YES];
}

- (void) drawCoordinatesOnMap
{
    self.destinationCoordinates         = CLLocationCoordinate2DMake(self.latitude.doubleValue, self.longitude.doubleValue);
    MKPointAnnotation *annotationPoint  = [MKPointAnnotation new];
    annotationPoint.title               = self.nameToDisplay;
    annotationPoint.coordinate          = self.destinationCoordinates;
    [self.mapView addAnnotation: annotationPoint];
}

- (void) showZOO
{
    self.showDirections = YES;
    self.nameToDisplay  = @"ZOO";
    self.latitude  = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.latitude];
    self.longitude = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.longitude];
}

# pragma mark - Rendering directions

- (void) drawDirectionsToLocation
{
    MKMapItem *sourceMapItem = [MKMapItem mapItemForCurrentLocation];
    [sourceMapItem setName: NSLocalizedString(@"yourLocation", nil)];
    
    MKPlacemark *destination      = [[MKPlacemark alloc] initWithCoordinate: self.destinationCoordinates addressDictionary: nil];
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
    renderer.strokeColor         = [UIColor orangeColor];
    renderer.lineWidth           = 4.0;
    return  renderer;
}

- (void) setMapRegion
{
    MKPointAnnotation *annotationUser         = [MKPointAnnotation new];
    annotationUser.coordinate                 = self.destinationCoordinates;
    MKPointAnnotation *annotationDestination  = [MKPointAnnotation new];
    annotationDestination.coordinate          = self.mapView.userLocation.coordinate;
    
    if (self.mapView.userLocation.coordinate.latitude  == 0 &&
        self.mapView.userLocation.coordinate.longitude == 0)
    {
        [self zoomOnLocation: self.destinationCoordinates];
    }
    else
    {
        NSArray *annotations = @[annotationUser, annotationDestination];
        [self.mapView showAnnotations: annotations animated: YES];
        [self.mapView removeAnnotations: annotations];
        self.fitToPath = NO;
    }
}

@end