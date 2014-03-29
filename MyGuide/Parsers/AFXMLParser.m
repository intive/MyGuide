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

static NSString *kXmlAnimals            = @"animals";
static NSString *kXmlWays               = @"ways";
static NSString *kXmlJunctions          = @"junctions";
static NSString *kXmlAnimal             = @"animal";
static NSString *kXmlWay                = @"way";
static NSString *kXmlNode               = @"node";
static NSString *kXmlJunction           = @"junction";
static NSString *kXmlLatitude           = @"lat";
static NSString *kXmlLongitude          = @"lon";
static NSString *kXmlId                 = @"id";
static NSString *kXmlName               = @"name";
static NSString *kXmlDescriptionAdult   = @"description_adult";
static NSString *kXmlDescriptionChild   = @"description_child";
static NSString *kXmlImage              = @"image";
static NSString *kXmlPL                 = @"pl";
static NSString *kXmlEN                 = @"en";

@implementation AFXMLParser

- (NSData *)getDataXML
{
    return [XMLFetcher fetchDataFromXML:@"data"];
}

- (void)parse
{
    _parsingError         = NO;
    _nameFlag             = NO;
    _descriptionAdultFlag = NO;
    _descriptionChildFlag = NO;
    
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:[self getDataXML]];
    [parser setDelegate:self];
    [parser parse];
}


#pragma mark - parser delegate methods
- (void)parserDidStartDocument:(NSXMLParser *)parser
{
    MWLogInfo(@"File found. Parsing data.xml started.");
}

- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError
{
    NSString *errorString = [NSString stringWithFormat:@"Error code %li", (long)[parseError code]];
    MWLogError(@"Error while parsing data.xml: %@", errorString);
    _parsingError = YES;
}

- (void) parser: (NSXMLParser *) parser didStartElement: (NSString *)     elementName
                                        namespaceURI:    (NSString *)     namespaceURI
                                        qualifiedName:   (NSString *)     qName
                                        attributes:      (NSDictionary *) attributeDict
{
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
        _temporaryNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:kXmlLatitude] andLongitude:[attributeDict valueForKey:kXmlLongitude]];
        _currentAnimal = [[AFAnimal alloc] init];
        _animalInfoDictionary = [[NSMutableDictionary alloc] init];
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
    else if ([elementName isEqualToString:kXmlName]) {
        _nameFlag = YES;
    }
    else if ([elementName isEqualToString:kXmlDescriptionAdult]) {
        _descriptionAdultFlag = YES;
    }
    else if ([elementName isEqualToString:kXmlDescriptionChild]) {
        _descriptionChildFlag = YES;
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    [_elementValue appendString:string];
}

- (void)parser:(NSXMLParser *)parser didEndElement:    (NSString *) elementName
                                     namespaceURI:     (NSString *) namespaceURI
                                     qualifiedName:    (NSString *) qName
{
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
        [_currentAnimal setDictionary:_animalInfoDictionary];
        [_currentAnimal setCoordinates:_temporaryNode];
        [_animalsArray addObject:_currentAnimal];
        _animalInfoDictionary = nil;
        _temporaryNode = nil;
    }
    else if ([elementName isEqualToString:kXmlWay]) {
        if(_waysArray == nil) {
            _waysArray = [[NSMutableArray alloc] init];
        }
        if(_nodesArray != nil) {
            [_currentWay setNodesArray:_nodesArray];
        }
        else {
            _currentWay.nodesArray = nil;
        }
        [_waysArray addObject:_currentWay];
    }
    else if ([elementName isEqualToString:kXmlNode]) {
        if(_nodesArray == nil) {
            _nodesArray = [[NSMutableArray alloc] init];
        }
        [_nodesArray addObject:_currentNode];
    }
    else if ([elementName isEqualToString:kXmlJunction]) {
        if(_waysArray != nil) {
            [_currentJunction setWaysArray:_waysArray];
        }
        else {
            _currentJunction.waysArray = nil;
        }
        [_junctionsArray addObject:_currentJunction];
    }
    else if ([elementName isEqualToString:kXmlName]) {
        _nameFlag = NO;
    }
    else if ([elementName isEqualToString:kXmlDescriptionAdult]) {
        _descriptionAdultFlag = NO;
    }
    else if ([elementName isEqualToString:kXmlDescriptionChild]) {
        _descriptionChildFlag = NO;
    }
    else if ([elementName isEqualToString:kXmlImage]) {
        if(_descriptionAdultFlag == YES){
            [_animalInfoDictionary setValue:_elementValue forKey:@"adultImageName"];
        }
        else{
            [_animalInfoDictionary setValue:_elementValue forKey:@"childImageName"];
        }
    }
    else if ([elementName isEqualToString:kXmlPL]) {
        if(_nameFlag == YES){
            [_currentAnimal setNamePL:_elementValue];
        }
        else if(_descriptionAdultFlag == YES){
            [_animalInfoDictionary setValue:_elementValue forKey:@"adultDescriptionPL"];
        }
        else{
            [_animalInfoDictionary setValue:_elementValue forKey:@"childDescriptionPL"];
        }
    }
    else if ([elementName isEqualToString:kXmlEN]) {
        if(_nameFlag == YES){
            [_currentAnimal setNameEN:_elementValue];
        }
        else if(_descriptionAdultFlag == YES){
            [_animalInfoDictionary setValue:_elementValue forKey:@"adultDescriptionEN"];
        }
        else{
            [_animalInfoDictionary setValue:_elementValue forKey:@"childDescriptionEN"];
        }
    }
}

- (void)parserDidEndDocument:(NSXMLParser *)parser
{
    if (!_parsingError) {
        AFParsedData *sharedData = [AFParsedData sharedParsedData];
        MWLogInfo(@"animals count: %d",   [sharedData.animalsArray   count]);
        MWLogInfo(@"ways count: %d",      [sharedData.waysArray      count]);
        MWLogInfo(@"junctions count: %d", [sharedData.junctionsArray count]);

        MWLogInfo(@"Parsing data.xml complete.");
    }
    else {
        MWLogInfo(@"An error occurred during data.xml processing.");
    }
}

@end