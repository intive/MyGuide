//
//  TrackDetailsViewController.m
//  MyGuide
//
//  Created by afilipowicz on 01.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "TrackDetailsViewController.h"
#import "SWRevealViewController.h"
#import <MapKit/MapKit.h>

@interface TrackDetailsViewController ()

@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@end

@implementation TrackDetailsViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadMenuBar];
}

- (void)loadMenuBar
{
    UIBarButtonItem *start = [[UIBarButtonItem alloc] initWithTitle:@"start" style:UIBarButtonItemStylePlain target:self action:nil];
    UIBarButtonItem *trash = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"Buzz-Trash-icon"] style:UIBarButtonItemStylePlain target:self action:@selector(clearProgress)];
    [self.navigationItem setLeftBarButtonItems:@[start, trash]];
    self.rightSidebarButton.target = self.revealViewController;
    self.rightSidebarButton.action = @selector(rightRevealToggle:);
}

- (void)clearProgress
{
    
}

@end
