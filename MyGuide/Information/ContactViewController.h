//
//  ContactViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ContactViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *labelAddress;
@property (weak, nonatomic) IBOutlet UILabel *labelPhone;
@property (weak, nonatomic) IBOutlet UILabel *labelWebsite;
@property (weak, nonatomic) IBOutlet UITextView  *textViewOpeningInformation;
@property (weak, nonatomic) IBOutlet UITableView *tableViewHours;

@end