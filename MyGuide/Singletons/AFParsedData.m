//
//  AFParsedData.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFParsedData.h"

@implementation AFParsedData{
    Settings *_sharedSettings;
}

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
        _animalsPL = [[NSArray alloc] init];
        _animalsEN = [[NSArray alloc] init];
        _junctionsArray = [[NSArray alloc] init];
    }
    return self;
}

- (NSArray *)localizeAnimalsArray
{
    _sharedSettings = [Settings sharedSettingsData];
    if([_sharedSettings.currentLanguageCode isEqualToString:@"PL"]){
        return _animalsPL;
    }
    else {
        return _animalsEN;
    }
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