//
//  GastronomyDetailsMenuTableViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsMenuTableViewController.h"
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
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:simpleTableIdentifier];
    }
    
    Dish *dish = _dishes[indexPath.row];
    cell.textLabel.text       = [dish getName];
    cell.detailTextLabel.text = [NSString stringWithFormat: @"%.2fzł", [dish.price doubleValue]];
    
    return cell;
}

@end