//
//  Settings.m
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "Settings.h"

@implementation Settings

+ (id) sharedSettingsData {
    static Settings *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id) init {
    self = [super init];
    if(self) {
        [self initDefaults];
    }
    return self;
}

- (void) initDefaults {
    _innerRadius      = 1;
    _externalRadius   = 2;
    _languageFallback = @"en";
}

- (void) injectDataWithName: (NSString*) name andValue: (NSString*) value {
    if ([name isEqualToString:       @"lang_fallback"]) {
        _languageFallback = value;
    }
    else if ([name isEqualToString:  @"internal_object_radius"]) {
        _innerRadius = [value integerValue];
    }
    else if ([name isEqualToString:  @"external_object_radius"]) {
        _externalRadius = [value integerValue];
    }
}

@end