//
//  AFTrack.h
//  MyGuide
//
//  Created by afilipowicz on 02.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Translatable.h"

@interface AFTrack : NSObject <NSCoding>

@property (nonatomic) NSString *image;
@property (nonatomic) NSString *activeStatus;
@property (nonatomic) NSString *progressText;
@property (nonatomic) NSArray  *animalsArray;
@property (nonatomic) NSArray  *visitedAnimalsArray;
@property (nonatomic, setter = changeProgress:) NSUInteger progressBase;
@property (nonatomic) float     progressRatio;

- (void)setDescription:(NSString *)description withLanguage:(NSString *)language;
- (NSString *)getDescriptionForLanguage: (NSString *)language;
- (NSString *)getDescription;
- (void)setName:(NSString *)name withLanguage:(NSString *)language;
- (NSString *)getNameForLanguage:(NSString *)language;
- (NSString *)getName;

- (void)encodeWithCoder:(NSCoder *)aCoder;
- (id)initWithCoder:(NSCoder *)aDecoder;

- (void)changeProgress:(NSUInteger)progressBase;
- (void)clearProgress;
- (void)incrementProgress;

@end
