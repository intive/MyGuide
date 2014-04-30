//
//  AFEventsParser.m
//  MyGuide
//
//  Created by afilipowicz on 30.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFEventsParser.h"
#import "AFEvent.h"
#import "AFEventsData.h"

static NSString *kXmlEvent        = @"event";
static NSString *kXmlName         = @"name";
static NSString *kXmlTime         = @"time";
static NSString *kXmlTimeCristmas = @"time_christmas";
static NSString *kXmlTimeWeekends = @"time_weekends";
static NSString *kXmlImage        = @"image";

@interface AFEventsParser ()

@property (nonatomic) AFEvent        *currentEvent;
@property (nonatomic) NSMutableArray *events;

@end

@implementation AFEventsParser

- (id)init
{
    self = [super init];
    if(self) {
        _events  = [NSMutableArray new];
        self.fileName = @"events";
    }
    return self;
}

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    [super parser:parser didStartElement:elementName namespaceURI:namespaceURI qualifiedName:qName attributes:attributeDict];
    
    if ([elementName isEqualToString: kXmlEvent]) {
        _currentEvent = [AFEvent new];
    }
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString:kXmlEvent]) {
        [_events addObject:_currentEvent];
    }
    else if ([elementName isEqualToString:kXmlEN] || [elementName isEqualToString: kXmlPL]) {
        [_currentEvent setName:[self currentElement] withLanguage:elementName];
    }
    else if ([elementName isEqualToString:kXmlTime]){
        _currentEvent.time = self.currentElement;
    }
    else if ([elementName isEqualToString:kXmlTimeCristmas]){
        _currentEvent.timeChristmas = self.currentElement;
    }
    else if ([elementName isEqualToString:kXmlTimeWeekends]){
        _currentEvent.timeWeekends = self.currentElement;
    }
    else if ([elementName isEqualToString: kXmlImage]) {
        _currentEvent.eventImage = self.currentElement;
    }
}

- (void) parserDidEndDocument: (NSXMLParser *)parser
{
    [super parserDidEndDocument: parser];
    AFEventsData *sharedParsedData = [AFEventsData sharedParsedData];
    sharedParsedData.events = _events;
}

@end
