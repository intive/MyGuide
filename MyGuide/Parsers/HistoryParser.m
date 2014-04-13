//
//  HistoryParser.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "HistoryParser.h"
#import "XMLFetcher.h"
#import "HistoryEvent.h"
#import "HistoryData.h"

static NSString *kXmlEvent = @"history_event";
static NSString *kXmlDate  = @"date";
static NSString *kXmlImage = @"image";
static NSString *kXmlPL    = @"pl";
static NSString *kXmlEN    = @"en";

@interface HistoryParser ()

@property (nonatomic, readonly) NSMutableArray  *historyEvents;
@property (nonatomic, readonly) NSMutableString *cacheElement;
@property (nonatomic) HistoryEvent *currentHistoryEvent;

@end

@implementation HistoryParser

- (id) init {
    self = [super init];
    if(self) {
        _historyEvents  = [NSMutableArray new];
    }
    return self;
}

- (void) parse
{
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData: [XMLFetcher fetchDataFromXML: @"history"]];
    [parser setDelegate: self];
    [parser parse];
}

- (void) parserDidStartDocument: (NSXMLParser *)parser { MWLogInfo(@"Reading history from file..."); }

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    _cacheElement = [NSMutableString new];
    
    if ([elementName isEqualToString: kXmlEvent]) {
        _currentHistoryEvent = [HistoryEvent new];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    [_cacheElement appendString: string];
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
    HistoryData *sharedParsedData = [HistoryData sharedParsedData];
    sharedParsedData.historyEvents = _historyEvents;
    MWLogInfo(@"History loaded!");
}

- (NSString *) currentElement {
    return [self normalize: _cacheElement];
}

- (NSString *) normalize: (NSString*)aString
{
    return [aString stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

@end