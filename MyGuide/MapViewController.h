//
//  MapViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 3/2/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "Settings.h"
#import "AFParsedData.h"
#import "AFAnimal.h"
#import "MKAnnotationAnimal.h"

@interface MapViewController : UIViewController <MKMapViewDelegate, UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
- (IBAction) centerOnCurrentLocation: (id) sender;
@end