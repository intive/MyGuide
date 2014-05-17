//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Edge.h"

@implementation Edge

- (BOOL) isEqual: (id)other
{
    if (self == other) {
        return YES;
    }

    if (![other isKindOfClass: [Edge class]]) {
        return NO;
    }

    Edge *otherEdge = (Edge *) other;

    return ([self.firstVertex isEqual: otherEdge.firstVertex] && [self.secondVertex isEqual: otherEdge.secondVertex])
           ||
           ([self.firstVertex isEqual: otherEdge.secondVertex] && [self.secondVertex isEqual: otherEdge.firstVertex]);
}

@end