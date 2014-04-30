//
//  TicketsViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "TicketsViewController.h"
#import "InformationData.h"
#import "Ticket.h"

@interface TicketsViewController ()

@property NSInteger currentExpandedIndex;

@property (nonatomic) NSArray         *ticketsHeaders;
@property (nonatomic) NSMutableArray  *ticketsGroups;
@property (nonatomic) InformationData *informationData;

@end

@implementation TicketsViewController

#pragma mark - Initialization

- (void) viewDidLoad
{
    [super viewDidLoad];
    self.informationData = [InformationData sharedParsedData];
    [self prepareTicketsData];
}

- (void) prepareTicketsData
{
    self.currentExpandedIndex = -1;
    self.ticketsHeaders = @[
                             NSLocalizedString(@"ticketIndividual", nil),
                             NSLocalizedString(@"ticketGroup",      nil)
                             ];
    self.ticketsGroups = [NSMutableArray new];
    [self.ticketsGroups addObject: self.informationData.ticketsIndividual];
    [self.ticketsGroups addObject: self.informationData.ticketsGroup];
}

#pragma mark - Displaying tickets

- (NSInteger) tableView: (UITableView *)tableView
  numberOfRowsInSection: (NSInteger)section
{
    return [self numberOfHeaders] + [self numberOfExpandedChildren];
}

- (UITableViewCell *) tableView: (UITableView *)tableView
          cellForRowAtIndexPath: (NSIndexPath *)indexPath
{
    static NSString *ParentCellIdentifier = @"ParentCell";
    static NSString *ChildCellIdentifier  = @"ChildCell";
    
    UITableViewCell *cell;
    
    if ([self isChild: indexPath]) {
        cell = [tableView dequeueReusableCellWithIdentifier: ChildCellIdentifier];
    }
    else {
        cell = [tableView dequeueReusableCellWithIdentifier: ParentCellIdentifier];
    }
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:ParentCellIdentifier];
        cell.textLabel.textColor = [UIColor colorWithRed:255/255.f green:95/255.f blue:0/255.f alpha:1];
    }
    
    if ([self isChild: indexPath]) {
        Ticket *ticket = (Ticket *)[[self.ticketsGroups objectAtIndex: self.currentExpandedIndex] objectAtIndex:indexPath.row - self.currentExpandedIndex - 1];
        cell.detailTextLabel.text = [NSString stringWithFormat: @"%@: %@ zł", [ticket getName], [ticket.price stringValue]];
    }
    else {
        NSInteger topIndex = indexPath.row - ([self isExpanded] && [self isBelowHeader: indexPath] ? [self numberOfExpandedChildren] : 0);
        cell.textLabel.text = [self.ticketsHeaders objectAtIndex:topIndex];
    }
    
    return cell;
}

- (BOOL) tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
    return ![self isChild: indexPath];
}

#pragma mark - Expanding tickets

- (void)      tableView: (UITableView *)tableView
didSelectRowAtIndexPath: (NSIndexPath *)indexPath
{
    if ([self isChild: indexPath]) { NSLog(@"Ticket tapped!"); return; }
    
    [self.tableView beginUpdates];
    
    if (self.currentExpandedIndex == indexPath.row) {
        [self collapseTicketsGroupsAtIndex: self.currentExpandedIndex];
        self.currentExpandedIndex = -1;
    }
    else {
        
        BOOL shouldCollapse = [self isExpanded];
        
        if (shouldCollapse) {
            [self collapseTicketsGroupsAtIndex: self.currentExpandedIndex];
        }
        
        [self updateCurrentExpandedIndex: indexPath];
        [self expandTicketsAtIndex: self.currentExpandedIndex];
    }
    
    [self.tableView endUpdates];
}

- (void) updateCurrentExpandedIndex: (NSIndexPath *)indexPath
{
    self.currentExpandedIndex = indexPath.row - ([self isExpanded] && [self isBelowHeader: indexPath] ? [self numberOfExpandedChildren] : 0);
}

- (void) expandTicketsAtIndex: (NSInteger)index
{
    NSMutableArray *indexPaths   = [NSMutableArray new];
    NSArray *currentTicketsGroup = [self.ticketsGroups objectAtIndex:index];
    long insertPos = index + 1;
    for (int i = 0; i < [currentTicketsGroup count]; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:insertPos++ inSection:0]];
    }
    [self.tableView insertRowsAtIndexPaths: indexPaths
                          withRowAnimation: UITableViewRowAnimationFade];
    [self.tableView scrollToRowAtIndexPath: [NSIndexPath indexPathForRow: index
                                                               inSection: 0]
                          atScrollPosition: UITableViewScrollPositionTop
                                  animated: YES];
}

- (void) collapseTicketsGroupsAtIndex: (NSInteger)index
{
    NSMutableArray *indexPaths = [NSMutableArray new];
    for (long i = index + 1; i <= index + [self childrenCount: index]; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow: i
                                                 inSection: 0]];
    }
    [self.tableView deleteRowsAtIndexPaths: indexPaths
                          withRowAnimation: UITableViewRowAnimationFade];
}

#pragma mark - Helper methods

- (NSInteger) numberOfHeaders
{
    return [self.ticketsHeaders count];
}

- (NSInteger) childrenCount: (NSInteger)index
{
    return [[self.ticketsGroups objectAtIndex:index] count];
}

- (BOOL) isExpanded
{
    return self.currentExpandedIndex > -1;
}

- (BOOL) isChild: (NSIndexPath *)indexPath
{
    return [self isExpanded] && [self isBelowHeader: indexPath] && [self isInExpandedChildren: indexPath];
}

- (BOOL) isBelowHeader: (NSIndexPath *)indexPath
{
    return indexPath.row > self.currentExpandedIndex;
}

- (BOOL) isInExpandedChildren: (NSIndexPath *)indexPath
{
    return indexPath.row <= self.currentExpandedIndex + [self numberOfExpandedChildren];
}

- (NSInteger) numberOfExpandedChildren
{
    return [self isExpanded] ? [self childrenCount: self.currentExpandedIndex] : 0;
}

@end