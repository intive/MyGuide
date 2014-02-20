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

- (NSData *)getDataXML{
    NSBundle *bundle = [NSBundle mainBundle];
    NSData *data = [NSData dataWithContentsOfFile:[bundle pathForResource:@"data" ofType:@"xml"]];
    return data;
}
- (NSData *)getOptionsXML{
#warning METHOD FOR FUTURE options.xml USE    /// af-19.02
    NSBundle *bundle = [NSBundle mainBundle];
    NSData *data = nil;
    return data;
}
- (void)parse{
    _errorParsing = NO;
    
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:[self getDataXML]];
    [parser setDelegate:self];
    [parser parse];
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
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName
                                     namespaceURI:(NSString *)namespaceURI
                                     qualifiedName:(NSString *)qName
                                     attributes:(NSDictionary *)attributeDict{
    
    _currentElement = [elementName copy];
    _elementValue = [[NSMutableString alloc] init];
    
    
#warning ADD MORE CONDITONS IN FUTURE DOWN HERE    /// af-19.02
    if ([elementName isEqualToString:@"animals"]) {
        _animalsArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:@"ways"]) {
        _waysArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:@"junctions"]) {
        _junctionsArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:@"animal"]) {
        AFNode *tempNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:@"lat"] andLongitude:[attributeDict valueForKey:@"lon"]];
        _currentAnimal = [[AFAnimal alloc] init];
        [_currentAnimal setCoordinates:tempNode];
    }
    else if ([elementName isEqualToString:@"way"]) {
        _currentWay = [[AFWay alloc] init];
        _nodesArray = [[NSMutableArray alloc] init];
        [_currentWay setWayID:[attributeDict valueForKey:@"id"]];
    }
    else if ([elementName isEqualToString:@"node"]) {
        _currentNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:@"lat"] andLongitude:[attributeDict valueForKey:@"lon"]];
    }
    else if ([elementName isEqualToString:@"junction"]) {
        AFNode *tempNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:@"lat"] andLongitude:[attributeDict valueForKey:@"lon"]];
        _waysArray = [[NSMutableArray alloc] init];
        _currentJunction = [[AFJunction alloc] init];
        [_currentJunction setCoordinates:tempNode];
    }
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string{
    [_elementValue appendString:string];
}
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName
                                     namespaceURI:(NSString *)namespaceURI
                                     qualifiedName:(NSString *)qName{
    
    AFParsedData *sharedData = [AFParsedData sharedParsedData];

#warning ADD MORE CONDITONS IN FUTURE DOWN HERE    /// af-19.02
    if ([elementName isEqualToString:@"animals"]) {
        [sharedData setAnimalsArray:_animalsArray];
        _animalsArray = nil;
    }
    else if ([elementName isEqualToString:@"ways"]) {
        [sharedData setWaysArray:_waysArray];
        _nodesArray = nil;
    }
    else if ([elementName isEqualToString:@"junctions"]) {
        [sharedData setJunctionsArray:_junctionsArray];
        _junctionsArray = nil;
    }
    else if ([elementName isEqualToString:@"animal"]) {
        [_currentAnimal setName:_elementValue];
        [_animalsArray addObject:_currentAnimal];
    }
    else if ([elementName isEqualToString:@"way"]) {
        if(_nodesArray != nil)[_currentWay setNodesArray:_nodesArray];
        [_waysArray addObject:_currentWay];
    }
    else if ([elementName isEqualToString:@"node"]) {
        [_nodesArray addObject:_currentNode];
    }
    else if ([elementName isEqualToString:@"junction"]) {
        [_currentJunction setWaysArray:_waysArray];
        [_junctionsArray addObject:_currentJunction];
    }
}
- (void)parserDidEndDocument:(NSXMLParser *)parser {
    if (_errorParsing == NO)
    {
        NSLog(@"Parsing complete.");
    } else {
        NSLog(@"An error occurred during XML processing.");
    }
}
@end
