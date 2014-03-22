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
    NSString *pathForFileInResources = [[NSBundle mainBundle] pathForResource: fileName ofType: @"xml"];
    NSString *fileNameWithExtension  = [fileName stringByAppendingPathExtension: @"xml"];
    NSString *pathForDocuments       = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    NSString *pathForFileInDocuments = [pathForDocuments stringByAppendingPathComponent: fileNameWithExtension];
    
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath: pathForFileInDocuments];
    
    return [NSData dataWithContentsOfFile: fileExists ? pathForFileInDocuments : pathForFileInResources];
}
@end