//
//  DetailsMapViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface DetailsMapViewController : UIViewController <MKMapViewDelegate>

@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIToolbar *mapToolbar;

@property (nonatomic) NSString *nameToDisplay;
@property (nonatomic) double   latitude;
@property (nonatomic) double   longitude;

- (void) showZOO;
- (void) drawPathToAnimal;

@end