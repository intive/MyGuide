//
//  AccessViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 6/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface AccessViewController : UIViewController <MKMapViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel      *labelAddress;
@property (weak, nonatomic) IBOutlet UITextView   *textViewPark;
@property (weak, nonatomic) IBOutlet MKMapView    *mapView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollViewBuses;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollViewTrams;

@end
