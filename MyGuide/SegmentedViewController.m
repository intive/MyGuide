//
//  SegmentedViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/19/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "SegmentedViewController.h"
#import "SWRevealViewController.h"

@interface SegmentedViewController ()

@property (strong, nonatomic) UIStoryboard *mainStoryboard;
@property (strong, nonatomic) UIViewController *currentViewController;

@end

@implementation SegmentedViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
    self.mainStoryboard = [UIStoryboard storyboardWithName: @"Main" bundle: [NSBundle mainBundle]];
    [self prepareControllers];
}

- (void) initMenuBar
{
    self.sidebarButton.target = self.revealViewController;
    self.sidebarButton.action = @selector(revealToggle:);
}

- (void) prepareControllers
{
    self.detailsMapController = (DetailsMapViewController *)[self getControllerById: @"detailsMap"];
    self.currentViewController = self.childViewControllers.lastObject;
    [self invalidateViewControllers];
}

- (void) invalidateViewControllers
{
    @throw [NSException
            exceptionWithName:  @"NotImplementedException"
            reason:             @"This method should be implemented in subclass."
            userInfo:           nil];
}

- (void) prepareFirstViewController: (NSString *) storyboardId
{
    self.firstViewController = (UIViewController *)[self getControllerById: storyboardId];
}

- (void) prepareSecondViewController: (NSString *) storyboardId
{
    self.secondViewController = (UIViewController *)[self getControllerById: storyboardId];
}

- (UIViewController *) getControllerById: (NSString *) controllerID
{
    return [self.mainStoryboard instantiateViewControllerWithIdentifier: controllerID];
}

- (void) viewWillAppear: (BOOL)animated
{
    [self invalidateViewControllers];
    [self.segmentedControl setSelectedSegmentIndex: 0];
    [self switchController: self.firstViewController withAnimation: NO];
}

- (IBAction) switchControllers: (UISegmentedControl *)segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        BOOL result = [self switchController: self.firstViewController withAnimation: YES];
        if(!result) [segmentControl setSelectedSegmentIndex: 1];
    }
    else if(selectedIndex == 1) {
        BOOL result = [self switchController: self.secondViewController withAnimation: YES];
        if(!result) [segmentControl setSelectedSegmentIndex: 0];
    }
    else {
        [self.navigationController pushViewController: self.detailsMapController animated: YES];
    }
}

- (BOOL) switchController: (UIViewController *) newController withAnimation: (BOOL) animation {
    if (self.currentViewController == newController) return YES;
    
    self.segmentedControl.userInteractionEnabled = NO;
    
    [self addChildViewController: newController];
    newController.view.frame = self.containerView.bounds;
    
    [self.currentViewController willMoveToParentViewController: nil];
    
    [self transitionFromViewController: self.currentViewController
                      toViewController: newController
                              duration: .6
                               options: animation ?
UIViewAnimationOptionTransitionFlipFromLeft :
     UIViewAnimationOptionTransitionNone
                            animations: nil
                            completion: ^(BOOL finished) {
                                [self.currentViewController removeFromParentViewController];
                                [newController didMoveToParentViewController: self];
                                self.currentViewController = newController;
                                self.segmentedControl.userInteractionEnabled = YES;
                            }];
    return YES;
}

@end
