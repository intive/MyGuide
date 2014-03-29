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
@property (strong, nonatomic) NSString *languageCode;

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
    _languageCode = [[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString];
    if(![_languageCode isEqualToString:@"PL"]){
        _languageCode = @"EN";
    }
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(![_languageCode isEqual:[[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString]]){
        _languageCode = [[[[NSLocale preferredLanguages] objectAtIndex:0] substringToIndex:2] uppercaseString];
        if(![_languageCode isEqualToString:@"PL"]){
            _languageCode = @"EN";
        }
    }
    [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    [_descriptionTextViewOutlet setText:[_animal.animalInfoDictionary valueForKey:[NSString stringWithFormat:@"adultDescription%@", _languageCode]]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    NSLog(@" will disappear");
}
- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _detailsMapController = [storyboard instantiateViewControllerWithIdentifier:@"detailsMap"];
}
- (void)alternateBetweenContent
{
    if([_segmentedControlOutlet selectedSegmentIndex] == 0){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
        [_descriptionTextViewOutlet setText:[_animal.animalInfoDictionary valueForKey:[NSString stringWithFormat:@"adultDescription%@", _languageCode]]];
    }
    else if([_segmentedControlOutlet selectedSegmentIndex] == 1){
        [_animalImage setImage:[UIImage imageNamed:[_animal.animalInfoDictionary valueForKey:@"childImageName"]]];
        [_descriptionTextViewOutlet setText:[_animal.animalInfoDictionary valueForKey:[NSString stringWithFormat:@"childDescription%@", _languageCode]]];
    }
    else{
        [self.navigationController pushViewController:_detailsMapController animated:YES];
    }
}
- (void)setAnimal:(AFAnimal *)animal
{
    _animal = animal;
}

@end
