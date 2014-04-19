//
//  SegmentedViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/19/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DetailsMapViewController.h"

@interface SegmentedViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIBarButtonItem *sidebarButton;
@property (weak, nonatomic) IBOutlet UIView *containerView;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;

@property (strong, nonatomic) DetailsMapViewController *detailsMapController;
@property (strong, nonatomic) UIViewController *firstViewController;
@property (strong, nonatomic) UIViewController *secondViewController;

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl;

- (void) prepareControllers;
- (void) prepareFirstViewController: (NSString *) storyboardId;
- (void) prepareSecondViewController: (NSString *) storyboardId;

@end
