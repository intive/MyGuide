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
    NSString *fileNameWithExtension = [fileName stringByAppendingPathExtension:@"xml"];
    NSData *ans;
    
    NSBundle *bundle = [NSBundle mainBundle];
    NSString *bundlePath = [bundle pathForResource:fileName ofType: @"xml"];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsPath = paths[0];
    documentsPath = [documentsPath stringByAppendingPathComponent:fileNameWithExtension];
    bool fileExists = [[NSFileManager defaultManager] fileExistsAtPath:documentsPath];
    
    if(fileExists) ans = [NSData dataWithContentsOfFile:documentsPath];
    else ans = [NSData dataWithContentsOfFile:bundlePath];
    
    return ans;
}
@end