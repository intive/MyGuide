//
//  AFXMLParser.m
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "AFXMLParser.h"
#import "AFParsedData.h"
#import "XMLFetcher.h"

static NSString *kXmlAnimals = @"animals";
static NSString *kXmlWays = @"ways";
static NSString *kXmlJunctions = @"junctions";
static NSString *kXmlAnimal = @"animal";
static NSString *kXmlWay = @"way";
static NSString *kXmlNode = @"node";
static NSString *kXmlJunction = @"junction";
static NSString *kXmlLatitude = @"lat";
static NSString *kXmlLongitude = @"lon";
static NSString *kXmlId = @"id";

@implementation AFXMLParser

#pragma mark - general methods

- (NSData *)getDataXML{
    NSData *data = [XMLFetcher fetchDataFromXML:@"data"];
    return data;
}
- (void)parse{
    _parsingError = NO;
    
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:[self getDataXML]];
    [parser setDelegate:self];
    [parser parse];
}


#pragma mark - parser delegate methods

- (void)parserDidStartDocument:(NSXMLParser *)parser{
    NSLog(@"File found. Parsing data.xml started.");
}
- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError {
    
    NSString *errorString = [NSString stringWithFormat:@"Error code %li", (long)[parseError code]];
    NSLog(@"Error while parsing data.xml: %@", errorString);
    _parsingError=YES;
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName
                                     namespaceURI:(NSString *)namespaceURI
                                     qualifiedName:(NSString *)qName
                                     attributes:(NSDictionary *)attributeDict{
    
    _currentElement = [elementName copy];
    _elementValue = [[NSMutableString alloc] init];
    
    
    if ([elementName isEqualToString:kXmlAnimals]) {
        _animalsArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlWays]) {
        _waysArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlJunctions]) {
        _junctionsArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlAnimal]) {
        AFNode *tempNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:kXmlLatitude] andLongitude:[attributeDict valueForKey:kXmlLongitude]];
        _currentAnimal = [[AFAnimal alloc] init];
        [_currentAnimal setCoordinates:tempNode];
    }
    else if ([elementName isEqualToString:kXmlWay]) {
        _currentWay = [[AFWay alloc] init];
        _nodesArray = nil;
        [_currentWay setWayID:[attributeDict valueForKey:kXmlId]];
    }
    else if ([elementName isEqualToString:kXmlNode]) {
        _currentNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:kXmlLatitude] andLongitude:[attributeDict valueForKey:kXmlLongitude]];
    }
    else if ([elementName isEqualToString:kXmlJunction]) {
        AFNode *tempNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:kXmlLatitude] andLongitude:[attributeDict valueForKey:kXmlLongitude]];
        _waysArray = nil;
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

    if ([elementName isEqualToString:kXmlAnimals]) {
        [sharedData setAnimalsArray:_animalsArray];
        _animalsArray = nil;
    }
    else if ([elementName isEqualToString:kXmlWays]) {
        [sharedData setWaysArray:_waysArray];
        _nodesArray = nil;
    }
    else if ([elementName isEqualToString:kXmlJunctions]) {
        [sharedData setJunctionsArray:_junctionsArray];
        _junctionsArray = nil;
    }
    else if ([elementName isEqualToString:kXmlAnimal]) {
        [_currentAnimal setName:_elementValue];
        [_animalsArray addObject:_currentAnimal];
    }
    else if ([elementName isEqualToString:kXmlWay]) {
        if(_waysArray == nil) _waysArray = [[NSMutableArray alloc] init];
        if(_nodesArray != nil)[_currentWay setNodesArray:_nodesArray];
        else _currentWay.nodesArray = nil;
        [_waysArray addObject:_currentWay];
    }
    else if ([elementName isEqualToString:kXmlNode]) {
        if(_nodesArray == nil) _nodesArray = [[NSMutableArray alloc] init];
        [_nodesArray addObject:_currentNode];
    }
    else if ([elementName isEqualToString:kXmlJunction]) {
        if(_waysArray != nil)[_currentJunction setWaysArray:_waysArray];
        else _currentJunction.waysArray = nil;
        [_junctionsArray addObject:_currentJunction];
    }
}
- (void)parserDidEndDocument:(NSXMLParser *)parser {
    if (_parsingError == NO)
    {
        NSLog(@"Parsing data.xml complete.");
    } else {
        NSLog(@"An error occurred during data.xml processing.");
    }
}
@end
