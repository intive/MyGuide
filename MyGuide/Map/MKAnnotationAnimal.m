//
//  MKAnnotationAnimal.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "MKAnnotationAnimal.h"

@implementation MKAnnotationAnimal

- (id)initWithAnimal:(AFAnimal *)animal
{
    self = [super init];
    if (self) {
        _coordinate = [self buildCoordinate:animal];
        _title = animal.name;
        _animal = animal;
        _visited = NO;
        _isOnTrack = NO;
    }
    return self;
}

- (CLLocationCoordinate2D)buildCoordinate:(AFAnimal *)animal
{
    return CLLocationCoordinate2DMake(animal.coordinates.latitude, animal.coordinates.longitude);
}
- (MKAnnotationView *)annotationView
{
    MKAnnotationView *annotationView = [[MKAnnotationView alloc] initWithAnnotation:self reuseIdentifier:@"animalAnnotationView"];
    annotationView.enabled = YES;
    annotationView.canShowCallout = YES;
    annotationView.image = [UIImage imageNamed:self.isOnTrack ? @"pinOrange" : (self.visited ? @"pinGreen" : @"pinBlack")];
    annotationView.leftCalloutAccessoryView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:[self.animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    UITextView *annotationRightView = [[UITextView alloc] init];
    annotationRightView.text = [self.animal.animalInfoDictionary valueForKey:@"adultDescription"];
    annotationView.rightCalloutAccessoryView = annotationRightView;
    
    return annotationView;
}
+ (NSArray *)buildAnimalMKAnnotations:(NSArray *)animals
{
    MKAnnotationAnimal* annotation;
    NSMutableArray* result = [[NSMutableArray alloc] init];
    
    for (AFAnimal* animal in animals) {
        annotation = [[MKAnnotationAnimal alloc] initWithAnimal:animal];
        [result addObject:annotation];
    }
    
    return result;
}

@end