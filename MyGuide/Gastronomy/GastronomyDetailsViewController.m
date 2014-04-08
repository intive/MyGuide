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

@property (strong, nonatomic) UIStoryboard *mainStoryboard;

@property (strong, nonatomic) UIViewController *currentViewController;
@property (strong, nonatomic) UIViewController *detailsMapController;
@property (strong, nonatomic) GastronomyDetailsInfoViewController       *gastronomyInfo;
@property (strong, nonatomic) GastronomyDetailsMenuTableViewController  *gastronomyMenu;

@property (nonatomic) BOOL canSwichView;

@end

@implementation GastronomyDetailsViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    _mainStoryboard = [UIStoryboard storyboardWithName: @"Main" bundle: [NSBundle mainBundle]];
}

# pragma mark - Initializing controllers

- (void) prepareControllers
{
    [self prepareMapController];
    [self prepareMenuController];
    [self prepareInfoController];
    _currentViewController = self.childViewControllers.lastObject;
    _canSwichView = YES;
}

- (void) viewWillAppear: (BOOL)animated
{
    [self setTitle: [_restaurant getName]];
    [self prepareControllers];
    [_segmentedControl setSelectedSegmentIndex: 0];
    [self switchController: _gastronomyInfo withAnimation: NO];
}

- (void) prepareMapController
{
    _detailsMapController = [self getControllerById: @"detailsMap"];
}

- (void) prepareMenuController
{
    _gastronomyMenu = (GastronomyDetailsMenuTableViewController *)[self getControllerById: @"gastronomyDetailsMenu"];
    [_gastronomyMenu setDishes: _restaurant.dishes];
}

- (void) prepareInfoController
{
    _gastronomyInfo = (GastronomyDetailsInfoViewController *)[self getControllerById: @"gastronomyDetailsInfo"];
}

- (UIViewController *) getControllerById: (NSString *) controllerID
{
    return [_mainStoryboard instantiateViewControllerWithIdentifier: controllerID];
}

# pragma mark - Switching controllers

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        BOOL result = [self switchController: _gastronomyInfo withAnimation: YES];
        if(!result) [segmentControl setSelectedSegmentIndex: 1];
    }
    else if(selectedIndex == 1) {
        BOOL result = [self switchController: _gastronomyMenu withAnimation: YES];
        if(!result) [segmentControl setSelectedSegmentIndex: 0];
    }
    else {
        [self.navigationController pushViewController: _detailsMapController animated: YES];
    }
}

- (BOOL) switchController: (UIViewController *) newController withAnimation: (BOOL) animation {
    if(!_canSwichView) return  NO;
    _canSwichView = NO;
    
    [self addChildViewController: newController];
    newController.view.frame = _containerView.bounds;
    
    [_currentViewController willMoveToParentViewController: nil];
    
    [self transitionFromViewController: _currentViewController
                      toViewController: newController
                              duration: .6
                               options: animation ?
                                        UIViewAnimationOptionTransitionFlipFromLeft :
                                        UIViewAnimationOptionTransitionNone
                            animations: nil
                            completion: ^(BOOL finished) {
                                [_currentViewController removeFromParentViewController];
                                [newController didMoveToParentViewController: self];
                                _currentViewController = newController;
                                _canSwichView = YES;
                            }];
    return YES;
}

@end