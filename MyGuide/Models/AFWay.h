//
//  AFWay.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFWay : NSObject

@property (nonatomic, readonly) NSString *wayID;
@property (nonatomic, readonly) NSArray *nodesArray;

- (id)init;
- (id)initWithID:(NSString *)wayID andNodesArray:(NSArray *)nodesArray;

- (void)setWayID:     (NSString *)wayID;
- (void)setNodesArray:(NSArray  *)nodesArray;

@end
