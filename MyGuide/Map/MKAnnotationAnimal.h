//
//  MKAnnotationAnimal.h
//  MyGuide
//
//  Created by Kamil Lelonek on 3/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "AFAnimal.h"
#import "AFNode.h"

@interface MKAnnotationAnimal : NSObject <MKAnnotation>

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, readonly, copy) NSString *title;

- (id) initWithAnimal: (AFAnimal *) animal;
+ (NSArray *) buildAnimalMKAnnotations: (NSArray *) animals;

@end