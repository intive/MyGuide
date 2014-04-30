//
//  AFEventsData.h
//  MyGuide
//
//  Created by afilipowicz on 30.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFEventsData : NSObject

@property (nonatomic) NSArray *events;

+ (id)sharedParsedData;

@end
