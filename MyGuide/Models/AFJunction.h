//
//  AFJunction.h
//  MyGuide
//
//  Created by afilipowicz on 19.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNode.h"

@interface AFJunction : NSObject

@property (nonatomic, readonly) AFNode *coordinates;
@property (nonatomic, readonly) NSArray *waysArray;

- (id)init;
- (id)initWithCoordinates:(AFNode *)coordinates andWaysArray:(NSArray *)waysArray;
- (void)setCoordinates:(AFNode *)coordinates;
- (void)setWaysArray:(NSArray *)waysArray;

@end
