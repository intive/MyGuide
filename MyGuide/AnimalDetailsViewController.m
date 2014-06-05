//
//  AnimalDetailsViewController.m
//  MyGuide
//
//  Created by afilipowicz on 23.03.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AnimalDetailsViewController.h"
#import "DetailsMapViewController.h"

@interface AnimalDetailsViewController ()

@property (strong, nonatomic) DetailsMapViewController *detailsMapController;

@end

@implementation AnimalDetailsViewController

- (void) viewDidLoad
{
    [super viewDidLoad];
    [self prepareNextViewController];
}

- (void) viewWillAppear: (BOOL)animated
{
    [super viewWillAppear: animated];
    [self.segmentedControl setSelectedSegmentIndex: 0];
    [self.segmentedControl sendActionsForControlEvents: UIControlEventValueChanged];
}

- (void) prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName: @"Main" bundle: [NSBundle mainBundle]];
    self.detailsMapController = [storyboard instantiateViewControllerWithIdentifier: @"detailsMap"];
}

- (IBAction) switchControllers: (UISegmentedControl *)segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if (selectedIndex == 0) {
        [self prepareWebView];
    }
    else if (selectedIndex == 1) {
        [self prepareMapView];
    }
}

- (void) prepareWebView
{
    NSString     *path    = [self getFilePath];
    NSURL        *url     = [NSURL fileURLWithPath: path];
    NSURLRequest *request = [NSURLRequest requestWithURL: url];
    [self.webView loadRequest: request];
}

- (void) prepareMapView
{
    self.detailsMapController.latitude  = self.animal.coordinates.latitude;
    self.detailsMapController.longitude = self.animal.coordinates.longitude;
    [self.detailsMapController drawPathToAnimal];
    [self.navigationController pushViewController: self.detailsMapController animated: YES];
}

- (NSString *) getFilePath
{
    NSString *animalName = [self cleanUp:self.title];
    NSString *fileName   = [NSString stringWithFormat: @"%@_%@", animalName, [[NSLocale preferredLanguages] objectAtIndex: 0]];
    return [self getHtmlFile: fileName] ? [self getHtmlFile: fileName] : [self getHtmlFile: @"404"];
}

- (NSString *) getHtmlFile: (NSString *) fileName
{
    return [[NSBundle mainBundle] pathForResource: fileName ofType: @"html"];
}

- (NSString *)cleanUp: (NSString *)aString {
    NSCharacterSet *trimmingSet = [NSCharacterSet whitespaceAndNewlineCharacterSet];
    aString = [aString stringByTrimmingCharactersInSet:trimmingSet];
    aString = [aString lowercaseString];
    aString = [aString stringByReplacingOccurrencesOfString: @"ą" withString:@"a"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ć" withString:@"c"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ę" withString:@"e"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ł" withString:@"l"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ń" withString:@"n"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ó" withString:@"o"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ś" withString:@"s"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ź" withString:@"z"];
    aString = [aString stringByReplacingOccurrencesOfString: @"ż" withString:@"z"];
    aString = [aString  stringByReplacingOccurrencesOfString:@" " withString:@"_"];
	return aString;
}

@end
