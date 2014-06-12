//
//  GastronomyDetailsViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SegmentedViewController.h"
#import "Restaurant.h"

@interface GastronomyDetailsViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) Restaurant *restaurant;

@end