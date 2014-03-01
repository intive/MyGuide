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
    NSBundle *bundle = [NSBundle mainBundle];
    NSString *bundlePath = [bundle pathForResource: fileName ofType: @"xml"];
    
    NSString *documentsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    documentsPath = [documentsPath stringByAppendingPathComponent: fileNameWithExtension];
    bool fileExists = [[NSFileManager defaultManager] fileExistsAtPath: documentsPath];
    
    return fileExists ? [NSData dataWithContentsOfFile: documentsPath] : [NSData dataWithContentsOfFile: bundlePath];
}
@end