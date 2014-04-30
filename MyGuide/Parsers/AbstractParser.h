//
//  AbstractParser.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "XMLFetcher.h"

static NSString *kXmlPL = @"pl";
static NSString *kXmlEN = @"en";

@interface AbstractParser : NSObject <NSXMLParserDelegate>

@property (nonatomic, readonly) NSMutableString *cacheElement;
@property (nonatomic) NSString *fileName;

- (NSString *) currentElement;
- (void) parse;

@end
