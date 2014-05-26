//
//  AFNode.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFNode.h"

@implementation AFNode

- (id) initWithLatitude: (double)latitude andLongitude:(double)longitude
{
    self = [super init];
    if(self) {
        _latitude  = latitude;
        _longitude = longitude;
    }
    return self;
}

- (void) setLatitude: (double)latitude
{
    self.latitude = latitude;
}

- (void) setLongitude: (double)longitude
{
    self.longitude = longitude;
}
- (NSInteger)distanceFromLocation:(CLLocation *)location
{
    CLLocation *nodeLocation  = [[CLLocation alloc] initWithLatitude: self.latitude longitude: self.longitude];
    return [location distanceFromLocation:nodeLocation];
}

@end