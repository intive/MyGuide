//
//  AFNode.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFNode : NSObject

@property (nonatomic, readonly) NSString *latitude;
@property (nonatomic, readonly) NSString *longitude;

- (id)init;
- (id)initWithLatitude:(NSString *)latitude andLongitude:(NSString *)longitude;
- (void)setLatitude:(NSString *)latitude;
- (void)setLongitude:(NSString *)longitude;

@end
