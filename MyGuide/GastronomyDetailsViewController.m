//
//  GastronomyDetailsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsViewController.h"

@interface GastronomyDetailsViewController ()

@end

@implementation GastronomyDetailsViewController

- (id) initWithNibName: (NSString *)nibNameOrNil bundle: (NSBundle *)nibBundleOrNil
{
    return [super initWithNibName: nibNameOrNil bundle: nibBundleOrNil];
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self setTitle: [NSString stringWithFormat: NSLocalizedString(@"cellLabelRestaurant", nil), _restaurantID]];
}


- (void) viewWillAppear: (BOOL)animated
{
    [_segmentedControl setSelectedSegmentIndex:0];
}

@end