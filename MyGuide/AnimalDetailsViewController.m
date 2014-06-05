//
//  AnimalDetailsViewController.m
//  MyGuide
//
//  Created by afilipowicz on 23.03.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AnimalDetailsViewController.h"
#import "DetailsMapViewController.h"

@interface AnimalDetailsViewController ()

@property (strong, nonatomic) DetailsMapViewController *detailsMapController;

@end

@implementation AnimalDetailsViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self prepareNextViewController];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.segmentedControl setSelectedSegmentIndex: 0];
}

- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    self.detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        [self prepareWebView];
    }
    else if(selectedIndex == 1) {
        [self prepareMapView];
    }
}

- (void) prepareWebView
{
    
}

- (void) prepareMapView
{
    self.detailsMapController.latitude  = self.animal.coordinates.latitude;
    self.detailsMapController.longitude = self.animal.coordinates.longitude;
    [self.detailsMapController drawPathToAnimal];
    [self.navigationController pushViewController:self.detailsMapController animated:YES];
}

@end