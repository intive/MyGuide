//
//  AFVisitedPOIsData.h
//  MyGuide
//
//  Created by afilipowicz on 23.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFVisitedPOIsData : NSObject

@property (nonatomic) NSMutableArray *visitedPOIs;

+ (id)sharedData;

@end
