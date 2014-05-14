//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Vertex.h"

@implementation Vertex

- (id) init
{
    self = [super init];
    if (self) {
        _edges = [NSMutableArray new];
    }
    return self;
}

- (NSComparisonResult) compare: (id)other
{
    return [self.weight compare: [(Vertex *)other weight]];
}

@end
