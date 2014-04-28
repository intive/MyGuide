//
//  TicketsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "TicketsViewController.h"

@interface TicketsViewController ()

@property (nonatomic) NSArray        *topItems;
@property (nonatomic) NSMutableArray *subItems;
@property             NSInteger      currentExpandedIndex;

@end

@implementation TicketsViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    self.topItems = [[NSArray alloc] initWithArray:[self  dummySuperItems]];
    self.subItems = [NSMutableArray new];
    self.currentExpandedIndex = -1;
    
    for (int i = 0; i < [self.topItems count]; i++) {
        [self.subItems addObject:[self  dummySubItems]];
    }
}

#pragma mark - Data generators

- (NSArray *) dummySuperItems {
    NSMutableArray *items = [NSMutableArray array];
    
    for (int i = 0; i < 10; i++) {
        [items addObject:[NSString stringWithFormat:@"Item %d", i + 1]];
    }
    
    return items;
}

- (NSArray *) dummySubItems {
    NSMutableArray *items = [NSMutableArray array];
    int numItems = arc4random() % 6 + 2;
    
    for (int i = 0; i < numItems; i++) {
        [items addObject:[NSString stringWithFormat:@"SubItem %d", i + 1]];
    }
    
    return items;
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.topItems count] + ((self.currentExpandedIndex > -1) ? [[self.subItems objectAtIndex: self.currentExpandedIndex] count] : 0);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *ParentCellIdentifier = @"ParentCell";
    static NSString *ChildCellIdentifier = @"ChildCell";
    
    BOOL isChild =
    self.currentExpandedIndex > -1
    && indexPath.row > self.currentExpandedIndex
    && indexPath.row <= self.currentExpandedIndex + [[self.subItems objectAtIndex: self.currentExpandedIndex] count];
    
    UITableViewCell *cell;
    
    if (isChild) {
        cell = [tableView dequeueReusableCellWithIdentifier:ChildCellIdentifier];
    }
    else {
        cell = [tableView dequeueReusableCellWithIdentifier:ParentCellIdentifier];
    }
    
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:ParentCellIdentifier];
    }
    
    if (isChild) {
        cell.detailTextLabel.text = [[self.subItems objectAtIndex: self.currentExpandedIndex] objectAtIndex:indexPath.row - self.currentExpandedIndex - 1];
    }
    else {
        NSInteger topIndex = (self.currentExpandedIndex > -1 && indexPath.row > self.currentExpandedIndex)
        ? indexPath.row - [[self.subItems objectAtIndex:self.currentExpandedIndex] count]
        : indexPath.row;
        
        cell.textLabel.text = [self.topItems objectAtIndex:topIndex];
        cell.detailTextLabel.text = @"";
    }
    
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    BOOL isChild =
        self.currentExpandedIndex > -1
        && indexPath.row > self.currentExpandedIndex
        && indexPath.row <= self.currentExpandedIndex + [[self.subItems objectAtIndex: self.currentExpandedIndex] count];
    
    if (isChild) {
        NSLog(@"A child was tapped, do what you will with it");
        return;
    }
    
    [self.tableView beginUpdates];
    
    if (self.currentExpandedIndex == indexPath.row) {
        [self collapseSubItemsAtIndex: (int) self.currentExpandedIndex];
        self.currentExpandedIndex = -1;
    }
    else {
        
        BOOL shouldCollapse =  self.currentExpandedIndex > -1;
        
        if (shouldCollapse) {
            [self collapseSubItemsAtIndex: (int) self.currentExpandedIndex];
        }
        
        self.currentExpandedIndex = (shouldCollapse && indexPath.row > self.currentExpandedIndex) ? indexPath.row - [[self.subItems objectAtIndex: self.currentExpandedIndex] count] : indexPath.row;
        
        [self expandItemAtIndex: (int) self.currentExpandedIndex];
    }
    
    [self.tableView endUpdates];
    
}

- (void)expandItemAtIndex:(int)index {
    NSMutableArray *indexPaths = [NSMutableArray new];
    NSArray *currentSubItems = [self.subItems objectAtIndex:index];
    int insertPos = index + 1;
    for (int i = 0; i < [currentSubItems count]; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:insertPos++ inSection:0]];
    }
    [self.tableView insertRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationFade];
    [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:YES];
}

- (void)collapseSubItemsAtIndex:(int)index {
    NSMutableArray *indexPaths = [NSMutableArray new];
    for (int i = index + 1; i <= index + [[self.subItems objectAtIndex:index] count]; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:i inSection:0]];
    }
    [self.tableView deleteRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationFade];
}

@end