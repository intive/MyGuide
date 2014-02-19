//
//  AFXMLParser.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFAnimal.h"
#import "AFWay.h"
#import "AFNode.h"

@interface AFXMLParser : NSObject <NSXMLParserDelegate>

@property (nonatomic, readonly) NSMutableArray *animalsArray;
@property (nonatomic, readonly) NSMutableArray *waysArray;
@property (nonatomic, readonly) NSString *currentElement;
@property (nonatomic, readonly) NSMutableString *elementValue;
@property (nonatomic, readonly) BOOL errorParsing;

- (NSData *)getXMLData;
- (void)parse;

@end
