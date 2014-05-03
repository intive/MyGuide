//
//  AbstractParser.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AbstractParser.h"

@implementation AbstractParser

- (void) parse
{
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData: [XMLFetcher fetchDataFromXML: self.fileName]];
    [parser setDelegate: self];
    [parser parse];
}

- (void) parserDidStartDocument: (NSXMLParser *)parser { MWLogInfo([NSString stringWithFormat: @"Reading %@ from file...", self.fileName]); }

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    _cacheElement = [NSMutableString new];
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    [_cacheElement appendString: string];
}

- (void) parserDidEndDocument: (NSXMLParser *)parser {
    MWLogInfo([NSString stringWithFormat: @"File %@ loaded!", self.fileName]);
}

- (NSString *) currentElement {
    return [self normalize: _cacheElement];
}

- (NSString *) normalize: (NSString*) aString
{
    return [aString stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError
{
    NSDictionary *userInfo = parseError.userInfo;
    NSNumber *lineNumber   = userInfo[@"NSXMLParserErrorLineNumber"];
    NSNumber *errorColumn  = userInfo[@"NSXMLParserErrorColumn"];
    NSString *errorMessage = userInfo[@"NSXMLParserErrorMessage"];
    
    NSLog(@"Error occured in line %@ and column %@\nWith message: %@", lineNumber, errorColumn, errorMessage);
}

@end
