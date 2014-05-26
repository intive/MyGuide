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

- (NSComparisonResult) compare: (Vertex *)other
{
    if (other.weight > self.weight) return NSOrderedDescending;
    if (other.weight < self.weight) return NSOrderedAscending;
    return NSOrderedSame;
}

- (BOOL) isEqual: (id)object {
    if (self == object) {
        return YES;
    }
    
    if (![object isKindOfClass:[Vertex class]]) {
        return NO;
    }
    
    Vertex *other = (Vertex *)object;
    return self.weight == other.weight && [self.edges isEqualToArray: other.edges];
}

@end
