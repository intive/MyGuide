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
        _waysArray      = [NSArray new];
        _animalsPL      = [NSArray new];
        _animalsEN      = [NSArray new];
        _junctionsArray = [NSArray new];
        _graph          = [Graph   new];
        _sharedSettings = [Settings sharedSettingsData];
    }
    return self;
}

- (NSArray *)localizeAnimalsArray
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    if([[userDefaults valueForKey:@"current language code"] isEqualToString:@"PL"]){
        return _animalsPL;
    }
    else {
        return _animalsEN;
    }
}

@end