//
//  AFAnimal.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "AFNode.h"

@interface AFAnimal : NSObject

@property (nonatomic, readonly) AFNode   *coordinates;
@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) NSInteger distanceFromUser;
@property (nonatomic)           NSInteger animalID;
@property (nonatomic)           NSInteger radius;
@property (nonatomic, readonly) NSDictionary *animalInfoDictionary;

- (id)init;

- (void)setCoordinates:(AFNode *)coordinates;
- (void)setDictionary:(NSDictionary *)dictionary;
- (void)setName:(NSString *)name;
- (void)setDistanceFromUser:(NSInteger)distanceFromUser;
- (BOOL)isWithinDistance:(NSInteger)distance fromLocation:(CLLocation *)location;
- (CLLocationCoordinate2D)getLocationCoordinate;

@end
