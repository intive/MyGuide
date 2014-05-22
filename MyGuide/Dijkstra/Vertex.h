//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNode.h"

@interface Vertex : NSObject

@property (nonatomic) double         weight;
@property (nonatomic) NSMutableArray *edges;
@property (nonatomic) Vertex         *predecessor;
@property (nonatomic) AFNode         *position;
@property (nonatomic) NSInteger      heapIndex;

@end
