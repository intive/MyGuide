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

@property (nonatomic, readonly) AFAnimal *animal;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControlOutlet;
@property (strong, nonatomic) IBOutlet UIImageView *animalImage;
@property (strong, nonatomic) IBOutlet UITextView *descriptionTextViewOutlet;

- (void)setAnimal:(AFAnimal *)animal;

@end
