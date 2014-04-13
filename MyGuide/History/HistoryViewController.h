//
//  HistoryViewController.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIBubbleTableViewDataSource.h"

@interface HistoryViewController : UIViewController <UIBubbleTableViewDataSource>

@property (weak, nonatomic) IBOutlet UIBarButtonItem *sidebarButton;

@end