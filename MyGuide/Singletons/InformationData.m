//
//  InformationData.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "InformationData.h"

@implementation InformationData

+ (id) sharedParsedData
{
    static InformationData *sharedData = nil;
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
        _website            = @"";
        _telephone          = @"";
        _address            = @"";
        _trams              = @"";
        _openings           = @[];
        _tickets            = @[];
        _emails             = @[];
        _ticketsInformation = [Translatable new];
        _parkingInformation = [Translatable new];
        _openingInformation = [Translatable new];
    }
    return self;
}

@end
