//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Heap.h"

@interface Heap ()

@property (nonatomic) NSUInteger     lastIndex;
@property (nonatomic) NSMutableArray *values;

@end

@implementation Heap

- (id) initWithCapacity: (NSInteger)capacity
{
    self = [super init];
    if (self) {
        _lastIndex = 0;
        _values    = [NSMutableArray arrayWithCapacity: capacity + 1];
    }
    return self;
}

- (void) add: (Vertex *)vertex
{
    if (self.lastIndex + 1 < self.values.count) {
        self.lastIndex++;
        self.values[self.lastIndex] = vertex;
        [self repairUp: self.lastIndex];
    }
}

- (void) repairUp: (NSUInteger)index
{
    if (index > self.lastIndex) return;
    Vertex *vertex = self.values[index];
    while (
            [self parent: index] >= 1
            &&
            [self.values[[self parent: index]] compare: vertex] == NSOrderedAscending) {

        self.values[index] = self.values[[self parent: index]];
        [(Vertex *) self.values[index] setHeapIndex: index];
        index = [self parent: index];
    }
}

- (Vertex *) poll
{
    if (self.lastIndex < 1) return nil;
    Vertex *vertex = self.values[1];
    self.values[1] = [self.values objectAtIndex: self.lastIndex];
    self.lastIndex--;
    [self repairDown: 1];
    return vertex;
}

- (void) repairDown: (NSUInteger)index
{
    Vertex *vertex = self.values[index];
    while ([self left: index] <= self.lastIndex) {
        NSUInteger leftIndex  = [self left: index];
        NSUInteger rightIndex = [self right: index];
        NSUInteger smallest   = leftIndex;
        if (rightIndex <= self.lastIndex && [self.values[rightIndex] compare: self.values[leftIndex]] == NSOrderedDescending) {
            smallest = rightIndex;
        }
        if ([self.values[smallest] compare: vertex] == NSOrderedDescending) break;
        self.values[index] = self.values[smallest];
        [(Vertex *) self.values[index] setHeapIndex: index];
        index = smallest;
    }
    self.values[index] = vertex;
    [(Vertex *) self.values[index] setHeapIndex: index];
}

- (NSUInteger) parent: (NSUInteger)i
{
    return i / 2;
}

- (NSUInteger) left: (NSUInteger)i
{
    return i * 2;
}

- (NSUInteger) right: (NSUInteger)i
{
    return i * 2 + 1;
}

@end
