//
//  AFEventsData.m
//  MyGuide
//
//  Created by afilipowicz on 30.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFEventsData.h"

@implementation AFEventsData

+ (id)sharedParsedData
{
    static AFEventsData *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id)init
{
    self = [super init];
    if(self) {
        _events = @[];
    }
    return self;
}

@end
