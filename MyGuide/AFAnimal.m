//
//  AFAnimal.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFAnimal.h"

@implementation AFAnimal

- (id)initWithLatitude:(double)latitude longitude:(double)longitude andName:(NSString *)name{
    self = [super init];
    if(self){
        _latitude = latitude;
        _longitude = longitude;
        _name = name;
    }
    return self;
}

@end
