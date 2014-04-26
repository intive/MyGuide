//
//  InformationViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "InformationViewController.h"
#import "TicketsViewController.h"
#import "ContactViewController.h"

@implementation InformationViewController

- (void) invalidateViewControllers
{
    [self prepareFirstViewController:  @"ticketsViewController"];
    [self prepareSecondViewController: @"contactViewController"];
}

- (void) viewWillAppear: (BOOL)animated
{
    [super viewWillAppear: animated];
    [self prepareMapController];
}

- (void) prepareMapController
{
    [self.detailsMapController showZOO];
}

@end
