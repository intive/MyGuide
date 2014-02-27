//
//  Settings.h
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Settings : NSObject
@property (nonatomic, readonly) NSInteger innerRadius;
@property (nonatomic, readonly) NSInteger externalRadius;
@property (nonatomic, readonly) NSString  *languageFallback;

+ (id)   sharedSettingsData;
- (void) injectDataWithName: (NSString*) name andValue: (NSString*) value;
@end