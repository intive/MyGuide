//
//  AFVisitedPOIsData.m
//  MyGuide
//
//  Created by afilipowicz on 23.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFVisitedPOIsData.h"

@implementation AFVisitedPOIsData

+ (id)sharedData
{
    static AFVisitedPOIsData *sharedData = nil;
    
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
        _visitedPOIs = [NSMutableSet new];
    }
    return self;
}

@end
