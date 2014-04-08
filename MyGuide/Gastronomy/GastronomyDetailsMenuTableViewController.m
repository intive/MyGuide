//
//  GastronomyDetailsMenuTableViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsMenuTableViewController.h"
#import "DishTableViewCell.h"
#import "Dish.h"

@implementation GastronomyDetailsMenuTableViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    self.tableView.allowsSelection = NO;
}

- (NSInteger)tableView: (UITableView *)tableView numberOfRowsInSection: (NSInteger)section
{
    return _dishes.count;
}

- (UITableViewCell *) tableView: (UITableView *)tableView cellForRowAtIndexPath: (NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"DishCell";
    
    DishTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[DishTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    Dish *dish = _dishes[indexPath.row];
    cell.labelName.text  = [dish getName];
    cell.labelPrice.text = [NSString stringWithFormat: @"%.2f", [dish.price doubleValue]];
    cell.imageLogo.image = [UIImage imageNamed: @"placeholder_adult"];
    
    return cell;
}

@end