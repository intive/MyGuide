//
//  AFNode.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFNode.h"

@implementation AFNode

- (id)initWithLatitude:(double)latitude andLongitude:(double)longitude{
    self = [super init];
    if(self){
        _latitude = latitude;
        _longitude = longitude;
    }
    return self;
}

@end
