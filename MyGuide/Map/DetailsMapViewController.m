//
//  DetailsMapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "DetailsMapViewController.h"

@interface DetailsMapViewController ()

    @property (nonatomic) CLLocationCoordinate2D destinationCoordinates;

@end

@implementation DetailsMapViewController

double const ZOOM_LEVEL = 15;

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self setDestinationCoordinates];
    [self drawCoordinatesOnMap: self.destinationCoordinates];
    [self.mapView setShowsUserLocation: YES];
    [self.mapView setMapType: MKMapTypeSatellite];
    
    if (self.showDirections) {
        [self drawDirectionsToLocation];
        self.mapView.delegate = self;
    }
    else {
        [self zoomOnLocation: self.destinationCoordinates];
    }
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
    [request setTransportType: MKDirectionsTransportTypeWalking];
    
    MKDirections *direction = [[MKDirections alloc] initWithRequest: request];
    [direction calculateDirectionsWithCompletionHandler: ^(MKDirectionsResponse *response, NSError *error) {
        if (error) {
            NSLog(@"There was an error getting your directions.");
            NSLog(@"%@", error.userInfo[@"NSLocalizedFailureReason"]);
            NSLog(@"%@", error.userInfo[@"NSLocalizedDescription"]);
            [self zoomOnLocation: self.destinationCoordinates];
            return;
        }
        [self zoomOnLocation: self.mapView.userLocation.coordinate];
        
        MKRoute *route = [response.routes firstObject];
        NSArray *steps = [route steps];
        [self.mapView addOverlay: [route polyline]];
        
        NSLog(@"Total Distance (in Meters) : %.0f", route.distance);
        NSLog(@"Total Steps : %d",[steps count]);
        
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

@end