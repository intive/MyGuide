//
//  GastronomyDetailsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsViewController.h"

@interface GastronomyDetailsViewController ()

@property (weak, nonatomic) DetailsMapViewController *detailsMapController;

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl;

@end

@implementation GastronomyDetailsViewController

- (void) viewWillAppear: (BOOL)animated
{
    [super viewWillAppear: animated];
    
    self.segmentedControl.selectedSegmentIndex = 0;
    [self prepareTableView];
    [self prepareMapController];
    [self prepareImage];
}

- (void) prepareMapController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName: @"Main" bundle: [NSBundle mainBundle]];
    self.detailsMapController = [storyboard instantiateViewControllerWithIdentifier: @"detailsMap"];
    self.detailsMapController.latitude  = self.restaurant.latitude;
    self.detailsMapController.longitude = self.restaurant.longitude;
}

- (void) prepareImage
{
    NSString *restaurantName = [[self.restaurant getNameForLanguage:@"pl"] lowercaseString];
    restaurantName = [restaurantName stringByReplacingOccurrencesOfString:@" " withString:@"_"];
    restaurantName = [NSString stringWithFormat:@"restaurant_%@.png", restaurantName];
    self.imageView.image = [UIImage imageNamed:restaurantName];
}

#pragma mark - Table View

- (void) prepareTableView
{
    self.tableView.allowsSelection = NO;
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    [self.tableView reloadData];
}

- (IBAction) switchControllers: (UISegmentedControl *)segmentControl
{
    if([segmentControl selectedSegmentIndex] == 1) {
        [self.navigationController pushViewController: self.detailsMapController animated: YES];
    }
}

- (NSInteger)tableView: (UITableView *)tableView numberOfRowsInSection: (NSInteger)section
{
    return self.restaurant.dishes.count;
}

- (UITableViewCell *) tableView: (UITableView *)tableView cellForRowAtIndexPath: (NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"DishCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:simpleTableIdentifier];
    }
    
    Dish *dish = self.restaurant.dishes[indexPath.row];
    cell.textLabel.text       = [dish getName];
    cell.detailTextLabel.text = [NSString stringWithFormat: @"%.2fzł", [dish.price doubleValue]];
    
    return cell;
}

@end
