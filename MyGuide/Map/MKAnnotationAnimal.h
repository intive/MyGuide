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
@property (nonatomic) BOOL visited;
@property (nonatomic) BOOL isOnTrack;
@property (nonatomic) AFAnimal *animal;

- (id)initWithAnimal:(AFAnimal *)animal;
- (MKAnnotationView *)annotationView;
+ (NSArray *)buildAnimalMKAnnotations:(NSArray *)animals;

@end