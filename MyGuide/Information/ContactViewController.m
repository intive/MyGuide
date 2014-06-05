//
//  ContactViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "ContactViewController.h"
#import "InformationData.h"
#import "Opening.h"

@interface ContactViewController ()

@property (nonatomic) InformationData *informationData;
@property (nonatomic) NSArray *openingHours;

@end

@implementation ContactViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    self.informationData = [InformationData sharedParsedData];
    [self setupScrollViewConstraints];
    [self prepareLabels];
    [self prepareOpeningHours];
}

-(void) setupScrollViewConstraints
{
    if ([[UIScreen mainScreen] bounds].size.height == 568) {
        self.scrollViewConstraint.constant = -10;
    }
}

- (void) prepareLabels
{
    self.labelAddress.text    = self.informationData.address;
    self.textViewPhone.text   = self.informationData.telephone;
    self.textViewWebsite.text = self.informationData.website;
}

- (void) prepareOpeningHours
{
    self.openingHours = self.informationData.openings;
    self.tableViewHours.dataSource       = self;
    self.tableViewHours.delegate         = self;
    self.tableViewHours.scrollEnabled    = NO;
    self.tableViewHours.allowsSelection  = NO;
    self.textViewOpeningInformation.text = [self.informationData.openingInformation getName];
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return self.openingHours.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"OpeningHoursCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier: simpleTableIdentifier];
    }
    
    Opening *opening = (Opening *)(self.openingHours[indexPath.row]);

    ((UILabel *)[cell.contentView viewWithTag: 1]).text = [opening getName];
    ((UILabel *)[cell.contentView viewWithTag: 2]).text = opening.hours[@"keyWeekdays"];
    ((UILabel *)[cell.contentView viewWithTag: 3]).text = opening.hours[@"keyWeekends"];
    
    return cell;
}

@end