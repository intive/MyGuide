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
#import "Settings.h"

@interface InformationViewController ()

@property (nonatomic) TicketsViewController *firstViewController;
@property (nonatomic) ContactViewController *lastViewController;
@property (nonatomic) Settings *sharedSettings;

@end

@implementation InformationViewController

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.sharedSettings = [Settings sharedSettingsData];
    }
    return self;
}
- (void) invalidateViewControllers
{
    [self prepareFirstViewController:  @"TicketsViewController"];
    [self prepareSecondViewController: @"ContactViewController"];
}

- (void) viewWillAppear: (BOOL)animated
{
    [super viewWillAppear: animated];
    [self prepareMapController];
}

- (void) prepareMapController
{
    self.detailsMapController.showDirections = YES;
    self.detailsMapController.nameToDisplay  = @"ZOO";
    self.detailsMapController.latitude  = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.latitude];
    self.detailsMapController.longitude = [NSNumber numberWithDouble: self.sharedSettings.zooCenter.longitude];
}

@end
