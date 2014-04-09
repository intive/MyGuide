//
//  AnimalDetailsViewController.h
//  MyGuide
//
//  Created by afilipowicz on 23.03.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AFAnimal.h"
@interface AnimalDetailsViewController : UIViewController

@property (nonatomic) AFAnimal *animal;
@property (weak, nonatomic) IBOutlet UITextView  *descriptionTextView;
@property (weak, nonatomic) IBOutlet UIImageView *animalImage;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;

- (IBAction) switchControllers: (UISegmentedControl *) segmentControl;

@end