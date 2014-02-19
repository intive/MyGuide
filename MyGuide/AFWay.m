//
//  AFWay.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFWay.h"

@implementation AFWay

- (id)initWithID:(long)wayID andNodesArray:(NSArray *)nodesArray{
    self = [super init];
    if(self){
        _wayID = wayID;
        _nodesArray = nodesArray;
    }
    return self;
}

@end
