//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Vertex.h"

@interface Heap : NSObject

- (id) initWithCapacity: (NSUInteger)capacity;

- (void) add: (Vertex *)vertex;
- (Vertex *) poll;
- (void) repairUp: (NSUInteger)index;

@end
