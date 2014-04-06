//
//  AFNode.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFNode.h"

@implementation AFNode

- (id)init
{
    self = [super init];
    if(self) {
        _latitude  = nil;
        _longitude = nil;
    }
    return self;
}

- (id)initWithLatitude:(NSString *)latitude andLongitude:(NSString *)longitude
{
    self = [super init];
    if(self) {
        _latitude  = latitude;
        _longitude = longitude;
    }
    return self;
}

- (void)setLatitude:(NSString *)latitude
{
    _latitude = latitude;
}

- (void)setLongitude:(NSString *)longitude
{
    _longitude = longitude;
}
- (NSInteger)distanceFromLocation:(CLLocation *)location
{
    CLLocation *nodeLocation  = [[CLLocation alloc] initWithLatitude:[_latitude doubleValue] longitude:[_longitude doubleValue]];
    return [location distanceFromLocation:nodeLocation];
}

@end