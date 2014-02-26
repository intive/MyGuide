//
//  XMLFetcher.m
//  MyGuide
//
//  Created by squixy on 23.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "XMLFetcher.h"

@implementation XMLFetcher
+ (NSData *) fetchDataFromXML: (NSString *) fileName
{
    NSString *_fileName = [NSString stringWithFormat:@"/%@.xml", fileName];
    NSData *ans;
    
    NSBundle *bundle = [NSBundle mainBundle];
    NSString *bundlePath = [bundle pathForResource:fileName ofType: @"xml"];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    paths = [paths arrayByAddingObject:_fileName];
    NSString *documentsPath = [NSString pathWithComponents:paths];
    bool fileExists = [[NSFileManager defaultManager] fileExistsAtPath:documentsPath];
    
    if(fileExists) ans = [NSData dataWithContentsOfFile:documentsPath];
    else ans = [NSData dataWithContentsOfFile:bundlePath];
    
    return ans;
}
@end