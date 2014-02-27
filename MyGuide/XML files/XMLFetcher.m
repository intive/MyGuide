//
//  XMLFetcher.m
//  MyGuide
//
//  Created by squixy on 23.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "XMLFetcher.h"

@implementation XMLFetcher
+ (NSData *) fetchDataFromXML: (NSString *) fileName {
    NSBundle *bundle = [NSBundle mainBundle];
    NSString *path   = [bundle pathForResource: fileName ofType: @"xml"];
    return [NSData dataWithContentsOfFile: path];
}
@end