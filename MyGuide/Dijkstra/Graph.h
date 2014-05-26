//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNode.h"

@interface Graph : NSObject

- (id) initWithWays: (NSArray *)ways andJunctions: (NSArray *)junctions;
- (NSArray *) findPathBetweenNode: (AFNode *)sourceNode andNode: (AFNode *)destinationNode;

@end
