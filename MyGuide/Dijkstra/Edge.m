//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Edge.h"

@implementation Edge

- (id) initWithFirstVertex: (Vertex *)firstVertex secondVertex: (Vertex *)secondVertex length: (double)length
{
    self = [super init];
    if (self) {
        _firstVertex  = firstVertex;
        _secondVertex = secondVertex;
        _length       = length;
    }
    return self;
}

- (BOOL) isEqual: (Edge *)otherEdge
{
    if (self == other) {
        return YES;
    }

    if (![other isKindOfClass: [Edge class]]) {
        return NO;
    }

    return ([self.firstVertex isEqual: otherEdge.firstVertex] && [self.secondVertex isEqual: otherEdge.secondVertex])
           ||
           ([self.firstVertex isEqual: otherEdge.secondVertex] && [self.secondVertex isEqual: otherEdge.firstVertex]);
}

@end
