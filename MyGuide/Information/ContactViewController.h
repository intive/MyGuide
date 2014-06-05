//
//  ContactViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ContactViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel       *labelAddress;
@property (weak, nonatomic) IBOutlet UITextView    *textViewPhone;
@property (weak, nonatomic) IBOutlet UITextView    *textViewWebsite;
@property (weak, nonatomic) IBOutlet UITextView    *textViewOpeningInformation;
@property (weak, nonatomic) IBOutlet UITableView   *tableViewHours;
@property (weak, nonatomic) IBOutlet UIScrollView  *scrollView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scrollViewConstraint;

@end