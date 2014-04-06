//
//  GastronomyListViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyListViewController.h"
#import "SWRevealViewController.h"

@interface GastronomyListViewController ()

@end

@implementation GastronomyListViewController

- (id) initWithNibName: (NSString *) nibNameOrNil bundle: (NSBundle *) nibBundleOrNil
{
    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self initMenuBar];
}

- (void) initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
}

@end