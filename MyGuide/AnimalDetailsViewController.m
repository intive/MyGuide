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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self prepareNextViewController];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.segmentedControl setSelectedSegmentIndex: 0];
    [self.animalImage setImage:[UIImage imageNamed:[self.animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    [self.descriptionTextView setText:[self.animal.animalInfoDictionary valueForKey:@"adultDescription"]];
}

- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    self.detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        [self.animalImage setImage:[UIImage imageNamed:[self.animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
        [self.descriptionTextView setText:[self.animal.animalInfoDictionary valueForKey:@"adultDescription"]];
    }
    else if(selectedIndex == 1) {
        self.detailsMapController.latitude  = self.animal.coordinates.latitude;
        self.detailsMapController.longitude = self.animal.coordinates.longitude;
        [self.detailsMapController drawPathToAnimal];
        [self.navigationController pushViewController:self.detailsMapController animated:YES];
    }
}

@end