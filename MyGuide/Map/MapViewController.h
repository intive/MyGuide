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
#import "AFWay.h"
#import "AFJunction.h"
#import "MKAnnotationAnimal.h"
#import "SWRevealViewController.h"
#import "LocationManager.h"

@interface MapViewController : UIViewController <MKMapViewDelegate, UIAlertViewDelegate, UIToolbarDelegate, UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic)   IBOutlet MKMapView *mapView;
@property (strong, nonatomic) IBOutlet UIToolbar *mapToolbar;
@property (weak, nonatomic)   IBOutlet UIBarButtonItem *sidebarButton;
@property (strong, nonatomic) IBOutlet UIImageView *nearestAnimalImageViewOutlet;
@property (strong, nonatomic) IBOutlet UIView *nearestAnimalsListOutlet;
@property (strong, nonatomic) IBOutlet UITableView *nearestAnimalsTableView;

@end