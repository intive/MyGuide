//
//  AFParsedData.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFParsedData.h"

@implementation AFParsedData

+ (id)sharedParsedData
{
    static AFParsedData *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id)init
{
    if(!self) {
        self = [super init];
        _waysArray      = [[NSArray alloc] init];
        _animalsArray   = [[NSArray alloc] init];
        _junctionsArray = [[NSArray alloc] init];
    }
    return self;
}

- (void)setAnimalsArray:(NSArray *)animalsArray
{
    _animalsArray = animalsArray;
}

- (void)setWaysArray:(NSArray *)waysArray
{
    _waysArray = waysArray;
}

- (void)setJunctionsArray:(NSArray *)junctionsArray
{
    _junctionsArray = junctionsArray;
}

@end