//
//  AFXMLParser.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFXMLParser.h"
#import "AFParsedData.h"

@implementation AFXMLParser

#pragma mark - general methods

- (NSData *)getXMLData{
    NSBundle *bundle = [NSBundle mainBundle];
    NSData *data = [NSData dataWithContentsOfFile:[bundle pathForResource:@"data" ofType:@"xml"]];
    return data;
}
- (void)parse{
    _errorParsing = NO;
    
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:[self getXMLData]];
    [parser setDelegate:self];

//    ??
    
//    [parser setShouldProcessNamespaces:NO];
//    [parser setShouldReportNamespacePrefixes:NO];
//    [parser setShouldResolveExternalEntities:NO];
    [parser parse];
    
    
    
    AFParsedData *sharedData = [AFParsedData sharedParsedData];
    [sharedData setAnimalsArray:_animalsArray andWaysArray:_waysArray];
}


#pragma mark - parser delegate methods

- (void)parserDidStartDocument:(NSXMLParser *)parser{
    NSLog(@"File found. Parsing started.");
}
- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError {
    
    NSString *errorString = [NSString stringWithFormat:@"Error code %i", [parseError code]];
    NSLog(@"Error parsing XML: %@", errorString);
    _errorParsing=YES;
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI
                                                                                                    qualifiedName:(NSString *)qName
                                                                                                    attributes:(NSDictionary *)attributeDict{
    _currentElement = [elementName copy];
    _elementValue = [[NSMutableString alloc] init];
    
    if ([elementName isEqualToString:@"animal"]) {
        // *************************************************************                TODO            *****************************
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
    }
    else if ([elementName isEqualToString:@"way"]) {
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
    }
    else if ([elementName isEqualToString:@"node"]) {
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                                                            *
        // *************************************************************                TODO            *****************************
    }
    
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string{
    [_elementValue appendString:string];
}
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName{
    
    // *************************************************************                TODO            *****************************
    if ([elementName isEqualToString:@"animal"]) {
    //    [articles addObject:[item copy]];                                                                                     *
    } else {
    //    [item setObject:_elementValue forKey:elementName];                                                                    *
    }
    // *************************************************************                TODO            *****************************
    
}
- (void)parserDidEndDocument:(NSXMLParser *)parser {
    
    if (_errorParsing == YES)
    {
        NSLog(@"Parsing complete.");
    } else {
        NSLog(@"An error occurred during XML processing.");
    }
    
}
@end
