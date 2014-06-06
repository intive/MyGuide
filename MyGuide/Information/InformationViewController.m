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
#import "AccessViewController.h"

@interface InformationViewController ()

@property (strong, nonatomic) AccessViewController *accessViewController;

@end

@implementation InformationViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    self.accessViewController = (AccessViewController *)[super getControllerById: @"accessViewController"];
}

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
    
}

- (IBAction) switchControllers: (UISegmentedControl *)segmentControl
{
    if([segmentControl selectedSegmentIndex] == 2) {
        [super switchController: self.accessViewController withAnimation: YES];
    }
    else {
        [super switchControllers:segmentControl];
    }
}

@end
