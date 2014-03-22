//
//  SlideMenuViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "SlideMenuViewController.h"

@implementation SlideMenuViewController {
    NSArray *menuItems;
}

- (id)initWithStyle:(UITableViewStyle)style
{
    return [super initWithStyle:style];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    menuItems = @[@"map", @"animals", @"events", @"info", @"history", @"gastronomy", @"preferences"];
}

#pragma mark - Table view data source

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return [menuItems count];
}

- (UITableViewCell *) tableView: (UITableView *) tableView cellForRowAtIndexPath: (NSIndexPath *) indexPath
{
    NSString *cellIdentifier = [menuItems objectAtIndex: indexPath.row];
    return [tableView dequeueReusableCellWithIdentifier: cellIdentifier forIndexPath: indexPath];
}

@end