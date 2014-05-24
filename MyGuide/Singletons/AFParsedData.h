//
//  AFParsedData.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Settings.h"
#import "Graph.h"

@interface AFParsedData : NSObject

@property (nonatomic, readonly, getter = localizeAnimalsArray) NSArray *animalsArray;
@property (nonatomic) NSArray *animalsPL;
@property (nonatomic) NSArray *animalsEN;
@property (nonatomic) NSArray *waysArray;
@property (nonatomic) NSArray *junctionsArray;
@property (nonatomic) Graph   *graph;

+ (id) sharedParsedData;
- (NSArray *) localizeAnimalsArray;

@end
