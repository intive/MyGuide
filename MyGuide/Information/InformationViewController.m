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

@interface InformationViewController ()

@property (strong, nonatomic) TicketsViewController *firstViewController;
@property (strong, nonatomic) ContactViewController *lastViewController;

@end

@implementation InformationViewController

- (void) invalidateViewControllers
{
    [self prepareFirstViewController:  @"TicketsViewController"];
    [self prepareSecondViewController: @"ContactViewController"];
}

@end
