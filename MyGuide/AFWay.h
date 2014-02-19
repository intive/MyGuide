//
//  AFWay.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
//#import "AFNode.h"

@interface AFWay : NSObject

@property (nonatomic, readonly) long wayID;
@property (nonatomic, readonly) NSArray *nodesArray;

- (id)initWithID:(long)wayID andNodesArray:(NSArray *)nodesArray;

@end
