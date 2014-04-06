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
    [_segmentedControl addTarget:self action:@selector(alternateBetweenContent) forControlEvents:UIControlEventValueChanged];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [_segmentedControl setSelectedSegmentIndex:0];
    [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"adultDescription"]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}
- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}
- (void)alternateBetweenContent
{
    if([_segmentedControl selectedSegmentIndex] == 0){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"adultDescription"]];
    }
    else if([_segmentedControl selectedSegmentIndex] == 1){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"childImageName"]]];
        [_descriptionTextView setText:[_animal.animalInfoDictionary valueForKey:@"childDescription"]];
    }
    else{
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}

@end
