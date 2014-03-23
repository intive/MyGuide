//
//  AFJunction.m
//  MyGuide
//
//  Created by afilipowicz on 19.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFJunction.h"

@implementation AFJunction

- (id)init
{
    self = [super init];
    if(self){
        _coordinates = [[AFNode alloc] init];
        _waysArray   = [[NSArray alloc] init];
    }
    return self;
}
- (id)initWithCoordinates:(AFNode *)coordinates andWaysArray:(NSArray *)waysArray
{
    self = [super init];
    if(self) {
        _coordinates = coordinates;
        _waysArray = waysArray;
    }
    return self;
}

- (void)setCoordinates:(AFNode *)coordinates
{
    _coordinates = coordinates;
}

- (void)setWaysArray:(NSArray *)waysArray
{
    _waysArray = waysArray;
}

@end