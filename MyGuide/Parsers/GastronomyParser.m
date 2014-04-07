//
//  GastronomyParser.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyParser.h"
#import "XMLFetcher.h"

@implementation GastronomyParser {
    NSMutableString *_cacheElement;
}

- (void) parse
{
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData: [XMLFetcher fetchDataFromXML: @"data"]];
    [parser setDelegate: self];
    [parser parse];
}

- (void) parserDidStartDocument: (NSXMLParser *)parser { MWLogInfo(@"Reading restaurants from file..."); }

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    _cacheElement = [[NSMutableString alloc] init];
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    [_cacheElement appendString:string];
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString:@"configuration"]) return;
}

- (void) parserDidEndDocument: (NSXMLParser *)parser { MWLogInfo(@"Restaurants loaded!"); }

@end