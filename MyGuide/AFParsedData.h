//
//  AFSharedParsedData.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFParsedData : NSObject

#warning ADD MORE PROPERTIES AND METHODS IN FUTURE    /// af-19.02
@property (nonatomic, readonly) NSArray *animalsArray;
@property (nonatomic, readonly) NSArray *waysArray;
@property (nonatomic, readonly) NSArray *junctionsArray;

+ (id)sharedParsedData;

- (void)setAnimalsArray:(NSArray *)animalsArray;
- (void)setWaysArray:(NSArray *)waysArray;
- (void)setJunctionsArray:(NSArray *)junctionsArray;

@end
