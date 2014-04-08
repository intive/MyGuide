//
//  GastronomyListViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyListViewController.h"
#import "SWRevealViewController.h"
#import "GastronomyDetailsViewController.h"
#import "GastronomyData.h"
#import "Restaurant.h"

#define NUMBER_OF_DUMMY_CELLS 10

@interface GastronomyListViewController ()

@property (strong, nonatomic) GastronomyDetailsViewController *gastronomyDetailsViewController;
@property (nonatomic) NSArray *restaurants;

@end

@implementation GastronomyListViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
    [self prepareDetailsViewController];
    [self initRestaurants];
    
    [self setTitle: NSLocalizedString(@"titleControllerGastronomy", nil)];
}

- (void) initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
}

- (void) prepareDetailsViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _gastronomyDetailsViewController = (GastronomyDetailsViewController *)[storyboard instantiateViewControllerWithIdentifier:@"gastronomyDetails"];
}

- (void) initRestaurants
{
    GastronomyData *sharedParsedData = [GastronomyData sharedParsedData];
    _restaurants = [sharedParsedData.restaurants copy];
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return _restaurants.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"RestaurantCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = [(Restaurant *)(_restaurants[indexPath.row]) getName];
    return cell;
}

- (void) tableView: (UITableView *) tableView didSelectRowAtIndexPath: (NSIndexPath *) indexPath
{
    [_gastronomyDetailsViewController setRestaurant: _restaurants[indexPath.row]];
    [self.navigationController pushViewController: _gastronomyDetailsViewController animated:YES];
}

@end