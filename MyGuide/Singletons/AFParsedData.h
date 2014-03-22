//
//  AFParsedData.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFParsedData : NSObject

@property (nonatomic, readonly) NSArray *animalsArray;
@property (nonatomic, readonly) NSArray *waysArray;
@property (nonatomic, readonly) NSArray *junctionsArray;

+ (id)sharedParsedData;

- (void) setWaysArray:      (NSArray *) waysArray;
- (void) setAnimalsArray:   (NSArray *) animalsArray;
- (void) setJunctionsArray: (NSArray *) junctionsArray;

@end
