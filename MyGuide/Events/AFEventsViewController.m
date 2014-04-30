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
    _events = sharedParsedData.events;
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
        eventImage.image = [UIImage imageNamed:[[_events objectAtIndex:indexPath.section] eventImage]];
       
       UILabel *nameLabel = (UILabel *)[cell viewWithTag:102];
       nameLabel.text = [[_events objectAtIndex:indexPath.section] getName];
    }
    else{
        static NSString *cellId = @"Cell right";
        cell = [tableView dequeueReusableCellWithIdentifier:cellId];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
        }
        UIImageView *eventImage = (UIImageView *)[cell viewWithTag:103];
        eventImage.image = [UIImage imageNamed:[[_events objectAtIndex:indexPath.section] eventImage]];
        
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:104];
        nameLabel.text = [[_events objectAtIndex:indexPath.section] getName];
    }
    return cell;
}

@end
