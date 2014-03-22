//
//  AFAnimal.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFAnimal.h"

@implementation AFAnimal

- (id) init
{
    self = [super init];
    if(self) {
        _coordinates = [[AFNode alloc] init];
        _name = [[NSString alloc] init];
    }
    return self;
}

- (id) initWithPosition: (AFNode   *) coordinates
                andName: (NSString *) name
{
    self = [super init];
    if(self) {
        _coordinates = coordinates;
        _name = name;
    }
    return self;
}

- (void) setCoordinates: (AFNode *) coordinates
{
    _coordinates = coordinates;
}

- (void) setName: (NSString *) name
{
    _name = name;
}

@end