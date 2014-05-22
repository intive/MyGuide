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
        _animalsArrayPL = [[NSMutableArray alloc] init];
        _animalsArrayEN = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlWays]) {
        _waysArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlJunctions]) {
        _junctionsArray = [[NSMutableArray alloc] init];
    }
    else if ([elementName isEqualToString:kXmlAnimal]) {
        _temporaryNode = [[AFNode alloc] initWithLatitude:[attributeDict valueForKey:kXmlLatitude] andLongitude:[attributeDict valueForKey:kXmlLongitude]];
        _currentAnimalPL = [[AFAnimal alloc] init];
        _currentAnimalEN = [[AFAnimal alloc] init];
        _animalInfoDictionaryPL = [[NSMutableDictionary alloc] init];
        _animalInfoDictionaryEN = [[NSMutableDictionary alloc] init];
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
        sharedData.animalsPL = _animalsArrayPL;
        sharedData.animalsEN = _animalsArrayEN;
        _animalsArrayPL = nil;
        _animalsArrayEN = nil;
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
        [_currentAnimalPL setDictionary:_animalInfoDictionaryPL];
        [_currentAnimalEN setDictionary:_animalInfoDictionaryEN];
        [_currentAnimalPL setCoordinates:_temporaryNode];
        [_currentAnimalEN setCoordinates:_temporaryNode];
        [_animalsArrayPL addObject:_currentAnimalPL];
        [_animalsArrayEN addObject:_currentAnimalEN];
        _animalInfoDictionaryPL = nil;
        _animalInfoDictionaryEN = nil;
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
            NSString *imageName = [[_elementValue componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] componentsJoinedByString:@""];
            [_animalInfoDictionaryPL setValue:imageName forKey:@"adultImageName"];
            [_animalInfoDictionaryEN setValue:imageName forKey:@"adultImageName"];
        }
        else{
            NSString *imageName = [[_elementValue componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] componentsJoinedByString:@""];
            [_animalInfoDictionaryPL setValue:imageName forKey:@"childImageName"];
            [_animalInfoDictionaryEN setValue:imageName forKey:@"childImageName"];
        }
    }
    else if ([elementName isEqualToString:kXmlPL]) {
        if(_nameFlag == YES){
            [_currentAnimalPL setName:_elementValue];
        }
        else if(_descriptionAdultFlag == YES){
            [_animalInfoDictionaryPL setValue:_elementValue forKey:@"adultDescription"];
        }
        else{
            [_animalInfoDictionaryPL setValue:_elementValue forKey:@"childDescription"];
        }
    }
    else if ([elementName isEqualToString:kXmlEN]) {
        if(_nameFlag == YES){
            [_currentAnimalEN setName:_elementValue];
        }
        else if(_descriptionAdultFlag == YES){
            [_animalInfoDictionaryEN setValue:_elementValue forKey:@"adultDescription"];
        }
        else{
            [_animalInfoDictionaryEN setValue:_elementValue forKey:@"childDescription"];
        }
    }
    else if ([elementName isEqualToString:@"id"]) {
        [_currentAnimalEN setAnimalID:[_elementValue integerValue]];
        [_currentAnimalPL setAnimalID:[_elementValue integerValue]];
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