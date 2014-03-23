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
#import "AFJunction.h"

@interface AFXMLParser : NSObject <NSXMLParserDelegate>

@property (nonatomic, readonly) NSMutableArray  *animalsArray;
@property (nonatomic, readonly) NSMutableArray  *waysArray;
@property (nonatomic, readonly) NSMutableArray  *junctionsArray;
@property (nonatomic, readonly) NSMutableArray  *nodesArray;
@property (nonatomic, readonly) NSString        *currentElement;
@property (nonatomic, readonly) NSMutableString *elementValue;

@property (nonatomic, readonly) BOOL parsingError;

@property (nonatomic, readonly) AFAnimal    *currentAnimal;
@property (nonatomic, readonly) AFWay       *currentWay;
@property (nonatomic, readonly) AFNode      *currentNode;
@property (nonatomic, readonly) AFJunction  *currentJunction;

- (NSData *)getDataXML;
- (void)parse;

@end