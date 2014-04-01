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
    [_segmentedControlOutlet addTarget:self action:@selector(alternateBetweenContent) forControlEvents:UIControlEventValueChanged];
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
    if([_segmentedControlOutlet selectedSegmentIndex] == 0){
        [_animalImage setImage:[UIImage imageNamed:@"placeholder_adult.png"]];
    }
    else if([_segmentedControlOutlet selectedSegmentIndex] == 1){
        [_animalImage setImage:[UIImage imageNamed:@"placeholder_child.png"]];
    }
    else{
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}

@end
