//
//  SettingsParser.m
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "SettingsParser.h"

NSString * const CONFIG_FILE_NAME = @"config";

@implementation SettingsParser {
    NSMutableString *_cacheElement;
    Settings        *_settings;
}

- (id) init {
    self = [super init];
    if(self) {
        _settings = [Settings sharedSettingsData];
    }
    return self;
}

- (void) loadSettings {
    NSData      *configData = [XMLFetcher fetchDataFromXML: CONFIG_FILE_NAME];
    NSXMLParser *parser     = [[NSXMLParser alloc] initWithData: configData];
    [parser setDelegate: self];
    [parser parse];
}

- (void) parserDidStartDocument:(NSXMLParser *) parser { MWLogInfo(@"Loading settings from file..."); }

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    _cacheElement = [[NSMutableString alloc] init];
}

- (void) parser: (NSXMLParser *) parser
foundCharacters: (NSString *)    string
{
    [_cacheElement appendString: string];
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString: @"configuration"]) return;
    
    MWLogInfo(@"Injecting %@ with value \"%@\" to settings.", elementName, _cacheElement);
    [_settings injectDataWithName: elementName andValue: _cacheElement];}

- (void) parserDidEndDocument:(NSXMLParser *) parser { MWLogInfo(@"Settings loaded!"); }
@end