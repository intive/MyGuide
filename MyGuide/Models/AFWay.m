//
//  AFWay.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFWay.h"

@implementation AFWay

- (id)init{
    self = [super init];
    if(self){
        _wayID = 0;
        _nodesArray = [[NSArray alloc] init];
    }
    return self;
}
- (id)initWithID:(NSString *)wayID andNodesArray:(NSArray *)nodesArray{
    self = [super init];
    if(self){
        _wayID = wayID;
        _nodesArray = nodesArray;
    }
    return self;
}
- (void)setWayID:(NSString *)wayID{
    _wayID = wayID;
}
- (void)setNodesArray:(NSArray *)nodesArray{
    _nodesArray = nodesArray;
}

@end
