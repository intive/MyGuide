//
//  SlideMenuViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "SlideMenuViewController.h"
#import "SWRevealViewController.h"

@interface SlideMenuViewController ()

@property (nonatomic, strong) NSArray *menuItems;

@end

@implementation SlideMenuViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    return [super initWithStyle:style];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.menuItems = @[@"map", @"animals", @"events", @"info", @"history", @"gastronomy", @"preferences"];
}

#pragma mark - Table view data source

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return [self.menuItems count];
}

- (UITableViewCell *) tableView: (UITableView *) tableView cellForRowAtIndexPath: (NSIndexPath *) indexPath
{
    NSString *cellIdentifier = [self.menuItems objectAtIndex: indexPath.row];
    return [tableView dequeueReusableCellWithIdentifier: cellIdentifier forIndexPath: indexPath];
}

- (void) prepareForSegue: (UIStoryboardSegue *) segue sender: (id) sender
{
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    UINavigationController *destViewController = (UINavigationController*) segue.destinationViewController;
    destViewController.title = [[self.menuItems objectAtIndex: indexPath.row] capitalizedString];
    SWRevealViewControllerSegue *swSegue = (SWRevealViewControllerSegue*) segue;
    swSegue.performBlock = ^(SWRevealViewControllerSegue* rvc_segue, UIViewController* svc, UIViewController* dvc) {
        UINavigationController* navController = (UINavigationController*) self.revealViewController.frontViewController;
        [navController setViewControllers: @[dvc] animated: NO ];
        [self.revealViewController setFrontViewPosition: FrontViewPositionLeft animated: YES];
    };
}

@end