//
//  TrackSlideMenuViewController.m
//  MyGuide
//
//  Created by afilipowicz on 01.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "TrackSlideMenuViewController.h"
#import "SWRevealViewController.h"
#import "AFTracksData.h"
#import "AFTrack.h"
#import "TrackDetailsViewController.h"

@interface TrackSlideMenuViewController ()

@property (nonatomic) NSArray *tracksArray;

@end

@implementation TrackSlideMenuViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.tracksArray = [[AFTracksData sharedParsedData] tracks];

    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.0f, 44.0f)];
    headerView.backgroundColor = [UIColor colorWithRed:1.0f green:0.584f blue:0.0f alpha:1.0f];
    self.tableView.tableHeaderView = headerView;
    self.tableView.tableFooterView = [[UIView alloc] init];
    
    CALayer *bgLayer = [CALayer layer];
    bgLayer.backgroundColor = headerView.backgroundColor.CGColor;
    bgLayer.frame = CGRectMake(0, -22.0f, 320.0f, 44.0f+22.0f);
    [self.tableView.layer addSublayer:bgLayer];
}
- (void)viewDidAppear:(BOOL)animated
{
    [self.tableView reloadData];
}

#pragma mark - Table view methods
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.tracksArray.count+1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.row != 0){
        NSString *cellIdentifier = @"Cell track";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:101];
        nameLabel.text = [[self.tracksArray objectAtIndex:indexPath.row -1] getName];
        
        UILabel *progressLabel = (UILabel *)[cell viewWithTag:102];
        progressLabel.text = [[self.tracksArray objectAtIndex:indexPath.row -1] progressText];
        
        UIProgressView *progressView = (UIProgressView *)[cell viewWithTag:103];
        [progressView setProgress:[[self.tracksArray objectAtIndex:indexPath.row -1] progressRatio] animated:YES];
        
        return cell;
    }
    else{
        NSString *cellIdentifier = @"Cell map";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:101];
        nameLabel.text = NSLocalizedString(@"cellLabelMap", nil);
        
        return cell;
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    if(indexPath.row != 0){
        TrackDetailsViewController *destViewController = (TrackDetailsViewController*) segue.destinationViewController;
        destViewController.title = [[self.tracksArray objectAtIndex:indexPath.row -1] getName];
        destViewController.trackRow = indexPath.row -1;
    }
    SWRevealViewControllerSegue *swSegue = (SWRevealViewControllerSegue*) segue;
    swSegue.performBlock = ^(SWRevealViewControllerSegue* rvc_segue, UIViewController* svc, UIViewController* dvc) {
        UINavigationController* navController = (UINavigationController*) self.revealViewController.frontViewController;
        [navController setViewControllers: @[dvc] animated: NO ];
        [self.revealViewController setFrontViewPosition: FrontViewPositionLeft animated: YES];
    };
}

@end