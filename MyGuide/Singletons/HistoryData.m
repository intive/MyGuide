//
//  HistoryData.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "HistoryData.h"
#import "Settings.h"

@implementation HistoryData

+ (id) sharedParsedData
{
    static HistoryData *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id) init
{
    self = [super init];
    if(self) {
        self.historyEvents = @[];
    }
    return self;
}

@end