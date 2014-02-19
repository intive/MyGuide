//
//  AFAnimal.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFAnimal : NSObject

@property (nonatomic, readonly) double latitude;
@property (nonatomic, readonly) double longitude;
@property (nonatomic, readonly) NSString *name;

- (id)initWithLatitude:(double)latitude longitude:(double)longitude andName:(NSString *)name;

@end
