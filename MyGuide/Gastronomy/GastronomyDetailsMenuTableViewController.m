//
//  GastronomyDetailsMenuTableViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsMenuTableViewController.h"
#import "DishTableViewCell.h"

#define NUMBER_OF_DUMMY_CELLS 10
#define TAG_IMAGE_VIEW  1
#define TAG_LABEL_NAME  2
#define TAG_LABEL_PRICE 3

@interface GastronomyDetailsMenuTableViewController ()

@end

@implementation GastronomyDetailsMenuTableViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
}

- (NSInteger)tableView: (UITableView *)tableView numberOfRowsInSection: (NSInteger)section
{
    return NUMBER_OF_DUMMY_CELLS;
}

- (UITableViewCell *) tableView: (UITableView *)tableView cellForRowAtIndexPath: (NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"DishCell";
    
    DishTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[DishTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.labelName.text  = @"Name";
    cell.labelPrice.text = @"Price";
    cell.imageLogo.image = [UIImage imageNamed: @"placeholder_adult"];
    
    return cell;
}

@end