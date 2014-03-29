//
//  AFAnimal.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNode.h"

@interface AFAnimal : NSObject

@property (nonatomic, readonly) AFNode   *coordinates;
@property (nonatomic, readonly) NSString *namePL;
@property (nonatomic, readonly) NSString *nameEN;
@property (nonatomic, readonly) NSDictionary *animalInfoDictionary;

- (id)init;
- (id)initWithPosition:(AFNode *)coordinates namePL:(NSString *)namePL andNameEN:(NSString *)nameEN;

- (void)setCoordinates:(AFNode *)coordinates;
- (void)setDictionary:(NSDictionary *)dictionary;
- (void)setNamePL:(NSString *)namePL;
- (void)setNameEN:(NSString *)nameEN;

@end
