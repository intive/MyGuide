//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Vertex.h"

@interface Edge : NSObject

@property (nonatomic) NSNumber *length;
@property (nonatomic) Vertex   *firstVertex;
@property (nonatomic) Vertex   *secondVertex;

@end
