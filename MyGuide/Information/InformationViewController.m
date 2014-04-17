//
//  InformationViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/17/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "InformationViewController.h"
#import "SWRevealViewController.h"

@interface InformationViewController ()

@end

@implementation InformationViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
}

- (void) initMenuBar
{
    self.sidebarButton.target = self.revealViewController;
    self.sidebarButton.action = @selector(revealToggle:);
}

@end