//
//  AFAnimal.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFAnimal.h"

@implementation AFAnimal

- (id)init
{
    self = [super init];
    if(self) {
        _coordinates = [[AFNode alloc] init];
        _namePL = [[NSString alloc] init];
        _nameEN = [[NSString alloc] init];
        _animalInfoDictionary = [[NSDictionary alloc] init];
    }
    return self;
}
- (id)initWithPosition:(AFNode *)coordinates namePL:(NSString *)namePL andNameEN:(NSString *)nameEN
{
    self = [super init];
    if(self) {
        _coordinates = coordinates;
        _namePL = namePL;
        _nameEN = nameEN;
        _animalInfoDictionary = [[NSDictionary alloc] init];
    }
    return self;
}

- (void)setCoordinates:(AFNode *)coordinates
{
    _coordinates = coordinates;
}
- (void)setDictionary:(NSDictionary *)dictionary
{
    _animalInfoDictionary = dictionary;
}
- (void)setNamePL:(NSString *)namePL
{
    _namePL = namePL;
}
- (void)setNameEN:(NSString *)nameEN
{
    _nameEN = nameEN;
}

@end