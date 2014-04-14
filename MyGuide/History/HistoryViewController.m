//
//  HistoryViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "HistoryViewController.h"
#import "SWRevealViewController.h"
#import "HistoryData.h"
#import "HistoryEvent.h"
#import "UIBubbleTableView.h"
#import "UIBubbleTableViewDataSource.h"
#import "NSBubbleData.h"

@interface HistoryViewController ()

@property (nonatomic) NSArray *historyEvents;
@property (nonatomic) NSMutableArray *bubbleData;
@property (weak, nonatomic) IBOutlet UIBubbleTableView *tableView;

@end

@implementation HistoryViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
    [self initHistory];
    [self initTableData];
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

- (void) initTableData
{
    [self fillBubleData];
    _tableView.allowsSelection = NO;
    _tableView.bubbleDataSource = self;
    _tableView.showAvatars = YES;
    [_tableView reloadData];
}

- (void) fillBubleData
{
    NSBubbleData *bubble;
    BOOL left = NO;
    _bubbleData = [NSMutableArray new];
    for (HistoryEvent *event in _historyEvents) {
        NSBubbleType type = (left ^= YES) ? BubbleRight : BubbleLeft;
        NSString *name  = [event getName];
        NSString *date  = event.date;
        NSString *image = event.image;
        bubble = [[NSBubbleData alloc] initWithText: name date:date type: type];
        bubble.avatar = [UIImage imageNamed: image];
        [_bubbleData addObject: bubble];
    }
}

#pragma mark - Table view data source

- (NSInteger)rowsForBubbleTable:(UIBubbleTableView *)tableView
{
    return [_bubbleData count];
}

- (NSBubbleData *)bubbleTableView:(UIBubbleTableView *)tableView dataForRow:(NSInteger)row
{
    return [_bubbleData objectAtIndex:row];
}

@end