//
//  AFTracksData.m
//  MyGuide
//
//  Created by afilipowicz on 02.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFTracksData.h"

@implementation AFTracksData

+ (id)sharedParsedData
{
    static AFTracksData *sharedData = nil;
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
        _tracks = @[];
        _currentTrackForMap = @[];
        self.shouldShowTrackOnMap = NO;
    }
    return self;
}

@end
