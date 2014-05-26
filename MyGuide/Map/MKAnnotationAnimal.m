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
        _coordinate = [self buildCoordinates:animal];
        _title = animal.name;
    }
    return self;
}

- (CLLocationCoordinate2D)buildCoordinates:(AFAnimal *)animal
{
    return CLLocationCoordinate2DMake(animal.coordinates.latitude, animal.coordinates.longitude);
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