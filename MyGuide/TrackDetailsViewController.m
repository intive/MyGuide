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
#import "AFTracksData.h"
#import "AFTrack.h"
#import "LocationManager.h"

@interface TrackDetailsViewController ()

@property (nonatomic) AFTrack *track;

@end

@implementation TrackDetailsViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.track = [[[AFTracksData sharedParsedData] tracks] objectAtIndex:self.trackRow];
    self.trackImage.image = [UIImage imageNamed: [NSString stringWithFormat:@"tracks_%@", [[self.track getNameForLanguage:@"pl"] lowercaseString]]];
    [self loadMenuBar];
}

- (void)viewDidAppear:(BOOL)animated
{
    [self loadViewContent];
}

- (void)loadViewContent
{
    UILabel *progressLabel = (UILabel *)[self.view viewWithTag:101];
    progressLabel.text = self.track.progressText;
    
    UIProgressView *progressView = (UIProgressView *)[self.view viewWithTag:102];
    [progressView setProgress:self.track.progressRatio animated:YES];
    
    UITextView *description = (UITextView *)[self.view viewWithTag:103];
    description.text = self.track.getDescription;
}

- (void)loadMenuBar
{
    UIBarButtonItem *start = [[UIBarButtonItem alloc] initWithTitle:self.track.activeStatus style:UIBarButtonItemStylePlain target:self action:@selector(startOrStopProgress)];
    UIBarButtonItem *trash = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"Buzz-Trash-icon"] style:UIBarButtonItemStylePlain target:self action:@selector(clearProgress)];
    [self.navigationItem setLeftBarButtonItems:@[start, trash]];
    self.rightSidebarButton.target = self.revealViewController;
    self.rightSidebarButton.action = @selector(rightRevealToggle:);
}

# define EXPLORATION_TRACK_NAME [[[[AFTracksData sharedParsedData] tracks] objectAtIndex:0] getName]
- (void)startOrStopProgress
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    UIBarButtonItem *startButton = [[self.navigationItem leftBarButtonItems] objectAtIndex:0];
    if([startButton.title isEqualToString:@"start"]) {
        self.track.activeStatus = @"stop";
        startButton.title = self.track.activeStatus;
        [userDefaults setObject:[self.track getName] forKey:@"current track"];
        [[LocationManager sharedLocationManager] loadTrackRegionsToMonitor:self.track];
    }
    else {
        self.track.activeStatus = @"start";
        startButton.title = self.track.activeStatus;
        [userDefaults setObject:EXPLORATION_TRACK_NAME forKey:@"current track"];
        [[LocationManager sharedLocationManager] clearMonitoredTrack];
    }
    [userDefaults synchronize];
    [self loadViewContent];
    [self returnToMainMap];
}

- (void)clearProgress
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    UIBarButtonItem *startButton = [[self.navigationItem leftBarButtonItems] objectAtIndex:0];
    self.track.activeStatus = @"start";
    startButton.title = self.track.activeStatus;
    [userDefaults setObject:EXPLORATION_TRACK_NAME forKey:@"current track"];
    [userDefaults synchronize];
    [[LocationManager sharedLocationManager] clearMonitoredTrack];
    [[[[AFTracksData sharedParsedData] tracks] objectAtIndex:self.trackRow] clearProgress];
    [self loadViewContent];
}

- (void)returnToMainMap
{
    UIViewController *initialViewController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateInitialViewController];
    if([self.track.activeStatus isEqualToString:@"stop"]){
        [[AFTracksData sharedParsedData] setCurrentTrackForMap:self.track.animalsArray];
        [[AFTracksData sharedParsedData] setShouldShowTrackOnMap:YES];
    }
    else{
        [[AFTracksData sharedParsedData] setShouldShowTrackOnMap:NO];
    }
    
    [self presentViewController:initialViewController animated:NO completion:^{
        self.view = nil;
    }];
}

@end
