//
//  HistoryParser.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Translatable.h"

@interface HistoryParser : NSObject <NSXMLParserDelegate>

- (void) parse;

@end