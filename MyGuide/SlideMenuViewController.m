//
//  SlideMenuViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "SlideMenuViewController.h"
#import "SWRevealViewController.h"

#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_IPHONE_5 (IS_IPHONE && ( fabsf([ [ UIScreen mainScreen ] bounds ].size.height - 568.0f ) < FLT_EPSILON ))

static CGFloat sEmptyCellHeight;

@interface SlideMenuViewController ()

@property (nonatomic, strong) NSArray *menuItems;

@end

@implementation SlideMenuViewController

+ (void)initialize
{
    sEmptyCellHeight = (IS_IPHONE_5 ? 120.0f : 40.0f);
}

- (void)setupControls
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.0f, 44.0f)];
    headerView.backgroundColor = [UIColor colorWithRed:1.0f green:0.584f blue:0.0f alpha:1.0f];
    self.tableView.tableHeaderView = headerView;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupControls];
    self.menuItems = @[@"map", @"animals", @"events", @"info", @"history", @"gastronomy", @"empty", @"preferences"];
}

#pragma mark - Table view data source
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.menuItems count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *cellIdentifier = [self.menuItems objectAtIndex:indexPath.row];
    return [tableView dequeueReusableCellWithIdentifier: cellIdentifier forIndexPath:indexPath];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *cellIdentifier = [self.menuItems objectAtIndex:indexPath.row];
    if ([cellIdentifier isEqualToString:@"empty"] == NO) {
        return 52.0f;
    }
    else {
        return sEmptyCellHeight;
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
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