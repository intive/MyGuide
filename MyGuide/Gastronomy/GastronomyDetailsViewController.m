//
//  GastronomyDetailsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsViewController.h"
#import "GastronomyDetailsMenuTableViewController.h"
#import "GastronomyDetailsInfoViewController.h"

@interface GastronomyDetailsViewController ()

@property (strong, nonatomic) UIViewController *detailsMapController;

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
    [self prepareMapController];
}


- (void) viewWillAppear: (BOOL)animated
{
    [_segmentedControl setSelectedSegmentIndex: 0];
}

- (void) prepareMapController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}

-(IBAction) switchControllers: (UISegmentedControl *) segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        GastronomyDetailsMenuTableViewController *gc = [GastronomyDetailsMenuTableViewController new];
        [self addChildViewController: gc];
    }
    else if(selectedIndex == 1) {
    }
    else {
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}

@end