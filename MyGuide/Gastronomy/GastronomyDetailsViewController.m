//
//  GastronomyDetailsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsViewController.h"
#import "GastronomyDetailsMenuTableViewController.h"

@implementation GastronomyDetailsViewController

- (void) invalidateViewControllers
{
    [self prepareFirstViewController:  @"gastronomyDetailsInfo"];
    [self prepareSecondViewController: @"gastronomyDetailsMenu"];
}

- (void) viewWillAppear: (BOOL)animated
{
    [super viewWillAppear: animated];
    [self prepareMapController];
    [self prepareMenuViewController];
}

- (void) prepareMapController
{
    self.detailsMapController.latitude  = self.restaurant.latitude;
    self.detailsMapController.longitude = self.restaurant.longitude;
}

- (void) prepareMenuViewController
{
    ((GastronomyDetailsMenuTableViewController *)(self.secondViewController)).dishes = self.restaurant.dishes;
}

@end
