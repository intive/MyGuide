//
//  MKMapView+ZoomLevel.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/9/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <MapKit/MapKit.h>

#import <MapKit/MapKit.h>

@interface MKMapView (ZoomLevel)

- (void) setCenterCoordinate: (CLLocationCoordinate2D)centerCoordinate
                   zoomLevel: (NSUInteger)zoomLevel
                    animated: (BOOL)animated;

@end