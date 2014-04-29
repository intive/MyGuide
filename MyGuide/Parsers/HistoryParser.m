//
//  HistoryParser.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "HistoryParser.h"
#import "HistoryEvent.h"
#import "HistoryData.h"

static NSString *kXmlEvent = @"history_event";
static NSString *kXmlDate  = @"date";
static NSString *kXmlImage = @"image";

@interface HistoryParser ()

@property (nonatomic, readonly) NSMutableArray  *historyEvents;
@property (nonatomic) HistoryEvent *currentHistoryEvent;

@end

@implementation HistoryParser

- (id) init {
    self = [super init];
    if(self) {
        _historyEvents  = [NSMutableArray new];
        self.fileName = @"history";
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
        _currentHistoryEvent = [HistoryEvent new];
    }
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString: kXmlEvent]) {
        [_historyEvents addObject: _currentHistoryEvent];
    }
    else if ([elementName isEqualToString: kXmlEN] || [elementName isEqualToString: kXmlPL]) {
        [_currentHistoryEvent setName: [self currentElement] withLanguage: elementName];
    }
    else if ([elementName isEqualToString: kXmlDate]) {
        _currentHistoryEvent.date = [self currentElement];
    }
    else if ([elementName isEqualToString: kXmlImage]) {
        _currentHistoryEvent.image = [self currentElement];
    }
}

- (void) parserDidEndDocument: (NSXMLParser *)parser {
    [super parserDidEndDocument: parser];
    HistoryData *sharedParsedData = [HistoryData sharedParsedData];
    sharedParsedData.historyEvents = _historyEvents;
}

@end