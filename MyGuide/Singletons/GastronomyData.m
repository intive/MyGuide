//
//  GastronomyData.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyData.h"
#import "Settings.h"

@implementation GastronomyData

+ (id) sharedParsedData
{
    static GastronomyData *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id) init
{
    if(!self) {
        self = [super init];
        self.restaurants = @[];
    }
    return self;
}

@end