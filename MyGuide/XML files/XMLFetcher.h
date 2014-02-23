//
//  XMLFetcher.h
//  MyGuide
//
//  Created by squixy on 23.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface XMLFetcher : NSObject
+ (NSData *) fetchDataFromXML: (NSString *) fileName;
@end