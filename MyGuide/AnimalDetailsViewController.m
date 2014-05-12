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
    
    [_segmentedControl setSelectedSegmentIndex: 0];
    [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"adultDescription"]];
}

- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl
{
    NSInteger selectedIndex = [segmentControl selectedSegmentIndex];
    if(selectedIndex == 0) {
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"adultDescription"]];
    }
    else if(selectedIndex == 1) {
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"childImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"childDescription"]];
    }
    else {
        _detailsMapController.latitude  = [NSNumber numberWithFloat: [_animal.coordinates.latitude floatValue]];
        _detailsMapController.longitude = [NSNumber numberWithFloat: [_animal.coordinates.longitude floatValue]];
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}

@end