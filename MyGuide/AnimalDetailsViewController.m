//
//  AnimalDetailsViewController.m
//  MyGuide
//
//  Created by afilipowicz on 23.03.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AnimalDetailsViewController.h"

@interface AnimalDetailsViewController ()

@property (strong, nonatomic) UIViewController *detailsMapController;

@end

@implementation AnimalDetailsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
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
    if(selectedIndex == 0){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"adultDescription"]];
    }
    else if(selectedIndex == 1){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"childImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"childDescription"]];
    }
    else{
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}

@end