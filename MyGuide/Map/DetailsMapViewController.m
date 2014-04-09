//
//  DetailsMapViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "DetailsMapViewController.h"

#define ZOOM_LEVEL 15

@implementation DetailsMapViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self initMapView];
}

- (void) initMapView
{
    CLLocationCoordinate2D selectedAnimalLocation =  CLLocationCoordinate2DMake([_latitude doubleValue], [_longitude doubleValue]);
    MKPointAnnotation *annotationPoint = [MKPointAnnotation new];
    annotationPoint.title       = _nameToDisplay;
    annotationPoint.coordinate  = selectedAnimalLocation;
    [_mapView addAnnotation: annotationPoint];    
    MKCoordinateSpan span = MKCoordinateSpanMake(180 / pow(2, ZOOM_LEVEL) * _mapView.frame.size.height / 256, 0);
    [_mapView setRegion:MKCoordinateRegionMake(selectedAnimalLocation, span) animated: YES];
}

@end