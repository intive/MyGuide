//
//  AFAnimal.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFAnimal.h"

@implementation AFAnimal

- (id)init
{
    self = [super init];
    if(self) {
        _coordinates = [[AFNode alloc] init];
        _name = [[NSString alloc] init];
        _animalInfoDictionary = [[NSDictionary alloc] init];
        _distanceFromUser = 0;
        _animalID = 0;
    }
    return self;
}

- (void)setName:(NSString *)name
{
    _name = name;
}
- (void)setCoordinates:(AFNode *)coordinates
{
    _coordinates = coordinates;
}
- (void)setDictionary:(NSDictionary *)dictionary
{
    _animalInfoDictionary = dictionary;
}
- (void)setDistanceFromUser:(NSInteger)distanceFromUser
{
    _distanceFromUser = distanceFromUser;
}
- (BOOL)isWithinDistance:(NSInteger)distance fromLocation:(CLLocation *)location
{
    BOOL ans = NO;
    if([_coordinates distanceFromLocation:location] <= distance){
        ans = YES;
    }
    return ans;
}
- (CLLocationCoordinate2D)getLocationCoordinate
{
    return CLLocationCoordinate2DMake(self.coordinates.latitude, self.coordinates.longitude);
}

@end