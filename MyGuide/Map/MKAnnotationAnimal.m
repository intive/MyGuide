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
        _title      = animal.name;
        _subtitle   = [animal.animalInfoDictionary valueForKey:@"adultDescription"];
        _animal     = animal;
        _visited    = NO;
        _isOnTrack  = NO;
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
   UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:[self.animal.animalInfoDictionary valueForKey:@"adultImageName"]]];
    imageView.frame = CGRectMake(0,0,40,40);
    annotationView.leftCalloutAccessoryView = imageView;
    
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