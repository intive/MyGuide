//
//  HistoryTableViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "HistoryTableViewController.h"
#import "SWRevealViewController.h"
#import "HistoryData.h"
#import "HistoryEvent.h"

@interface HistoryTableViewController ()

@property (nonatomic) NSArray *historyEvents;

@end

@implementation HistoryTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
    [self initHistory];
    self.tableView.allowsSelection = NO;
    [self setTitle: NSLocalizedString(@"titleControllerHistory", nil)];
}

- (void) initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
}

- (void) initHistory
{
    HistoryData *sharedParsedData = [HistoryData sharedParsedData];
    _historyEvents = sharedParsedData.historyEvents;
}

#pragma mark - Table view data source

- (NSInteger) tableView: (UITableView *)tableView numberOfRowsInSection: (NSInteger)section
{
    return _historyEvents.count;
}

- (UITableViewCell *) tableView: (UITableView *)tableView cellForRowAtIndexPath: (NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"HistoryCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    HistoryEvent *historyEvent = _historyEvents[indexPath.row];
    cell.textLabel.text = [historyEvent getName];
    
    return cell;
}

@end