//
//  Translatable.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Translatable : NSObject

@property (nonatomic) NSMutableDictionary *name;

- (void) setName: (NSString *)name withLanguage: (NSString *)language;
- (NSString *) getNameForLanguage: (NSString *)language;
- (NSString *) getName;

@end