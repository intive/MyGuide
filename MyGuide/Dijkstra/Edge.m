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

- (BOOL) isEqual: (id)object
{
    if (self == object) {
        return YES;
    }

    if (![object isKindOfClass: [Edge class]]) {
        return NO;
    }
    
    Edge *other = (Edge *)object;
    return ([self.firstVertex isEqual: other.firstVertex] && [self.secondVertex isEqual: other.secondVertex])
           ||
           ([self.firstVertex isEqual: other.secondVertex] && [self.secondVertex isEqual: other.firstVertex]);
}

@end
