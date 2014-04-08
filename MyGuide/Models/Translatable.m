//
//  Translatable.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/8/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Translatable.h"

@interface Translatable ()

@property (nonatomic) NSMutableDictionary *name;

@end

@implementation Translatable

- (id) init {
    self = [super self];
    if(self) {
        self.name = [
                     @{
                       @"en" : @"",
                       @"pl" : @""
                      }
                     mutableCopy
                     ];
    }
    return self;
}

- (void) setName: (NSString *)name withLanguage: (NSString *)language
{
    self.name[language] = name;
}

- (NSString *) getNameForLanguage: (NSString *)language
{
    return self.name[language];
}

- (NSString *) getName
{
    return self.name[@"en"];
}

@end