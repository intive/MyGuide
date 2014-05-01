//
//  AFEventsViewController.m
//  MyGuide
//
//  Created by afilipowicz on 30.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFEventsViewController.h"
#import "SWRevealViewController.h"
#import "AFEventsData.h"
#import "AFEvent.h"

@interface AFEventsViewController ()

@property (nonatomic) NSArray *events;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end

@implementation AFEventsViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadMenuBar];
    [self loadEvents];
    [self loadTableData];
    [self setTitle: NSLocalizedString(@"titleControllerEvents", nil)];
}

- (void)loadMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
    [self.view addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    [self.navigationItem setLeftBarButtonItem:_sidebarButton];
    [self.navigationItem setRightBarButtonItem:nil];
}

- (void)loadEvents
{
    AFEventsData *sharedParsedData = [AFEventsData sharedParsedData];
    self.events = sharedParsedData.events;
}

- (void)loadTableData
{
    _tableView.dataSource = self;
    _tableView.delegate = self;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return self.events.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = nil;
    if(indexPath.section % 2 == 0){
        static NSString *cellId = @"Cell left";
        cell = [tableView dequeueReusableCellWithIdentifier:cellId];
        if (cell == nil) {
           cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
        }
        UIImageView *eventImage = (UIImageView *)[cell viewWithTag:101];
        eventImage.image = [UIImage imageNamed:[[self.events objectAtIndex:indexPath.section] eventImage]];
       
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:102];
        nameLabel.text = [[self.events objectAtIndex:indexPath.section] getName];
        
        UILabel *timeLabel = (UILabel *)[cell viewWithTag:105];
        timeLabel.text = [self setTimeForCellAtSection:indexPath.section];
    }
    else{
        static NSString *cellId = @"Cell right";
        cell = [tableView dequeueReusableCellWithIdentifier:cellId];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
        }
        UIImageView *eventImage = (UIImageView *)[cell viewWithTag:103];
        eventImage.image = [UIImage imageNamed:[[self.events objectAtIndex:indexPath.section] eventImage]];
        
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:104];
        nameLabel.text = [[self.events objectAtIndex:indexPath.section] getName];
        
        UILabel *timeLabel = (UILabel *)[cell viewWithTag:106];
        timeLabel.text = [self setTimeForCellAtSection:indexPath.section];
    }
    return cell;
}
- (NSString *)setTimeForCellAtSection:(NSInteger)section
{
    NSString        *str;
    NSDateFormatter *formatterMonth = [NSDateFormatter new];
    NSString        *dateString;
    
    [formatterMonth setDateFormat:@"MM"];
    
    if([[self.events objectAtIndex:section] timeWeekends] != nil){
        if([self isWeekend:[NSDate date]]){
            str = [[[self.events objectAtIndex:section] timeWeekends] stringByReplacingOccurrencesOfString:@";" withString:@"\n"];
        }
        else{
            str = [[[self.events objectAtIndex:section] time] stringByReplacingOccurrencesOfString:@";" withString:@"\n"];
        }
    }
    else if([[self.events objectAtIndex:section] startDate] != nil){
        dateString = [formatterMonth stringFromDate:[NSDate date]];
        if(dateString.integerValue >= 6 && dateString.integerValue <= 9){
            str = [[[self.events objectAtIndex:section] time] stringByReplacingOccurrencesOfString:@";" withString:@"\n"];
        }
        else{
            str = NSLocalizedString(@"alertEventWrongMonth", nil);
        }
    }
    else{
        str = [[[self.events objectAtIndex:section] time] stringByReplacingOccurrencesOfString:@";" withString:@"\n"];
    }
    return str;
}
- (BOOL)isWeekend:(NSDate *)date
{
    NSInteger day = [[[NSCalendar currentCalendar] components:NSWeekdayCalendarUnit fromDate:date] weekday];
    
    const int kSunday = 1;
    const int kSaturday = 7;
    
    BOOL result = (day == kSunday || day == kSaturday);
    
    return result;
}

@end
