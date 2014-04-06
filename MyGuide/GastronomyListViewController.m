//
//  GastronomyListViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyListViewController.h"
#import "SWRevealViewController.h"

#define NUMBER_OF_DUMMY_CELLS 10

@interface GastronomyListViewController ()

@end

@implementation GastronomyListViewController

- (id) initWithNibName: (NSString *) nibNameOrNil bundle: (NSBundle *) nibBundleOrNil
{
    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
    
    [self setTitle: NSLocalizedString(@"titleControllerGastronomy", nil)];
}

- (void) initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return NUMBER_OF_DUMMY_CELLS;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"RestaurantCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = [NSString stringWithFormat: NSLocalizedString(@"cellLabelRestaurant", nil), indexPath.row];
    return cell;
}

@end