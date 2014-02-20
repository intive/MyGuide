//
//  SettingsParser.m
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "SettingsParser.h"
#import "Settings.h"

NSString * const CONFIG_FILE_NAME = @"config";

@implementation SettingsParser {
    NSMutableString *cacheElement;
    Settings *settings;
}

- (id) init {
    self = [super init];
    if(self) {
        settings = [Settings sharedSettingsData];
    }
    return self;
}

- (NSData *) getOptionsXML {
    NSBundle *bundle = [NSBundle mainBundle];
    NSString *path   = [bundle pathForResource: CONFIG_FILE_NAME ofType: @"xml"];
    return [NSData dataWithContentsOfFile: path];
}

- (void) loadSettings {
    NSData *configData = [self getOptionsXML];
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData: configData];
    [parser setDelegate: self];
    [parser parse];
}

- (void) parserDidStartDocument:(NSXMLParser *) parser {
    NSLog(@"Loading settings from file...");
}

- (void) parser: (NSXMLParser *) parser didStartElement: (NSString *)     elementName
                                        namespaceURI:    (NSString *)     namespaceURI
                                        qualifiedName:   (NSString *)     qName
                                        attributes:      (NSDictionary *) attributeDict
{
    cacheElement = [[NSMutableString alloc] init];
}

- (void) parser: (NSXMLParser *) parser foundCharacters: (NSString *) string {
    [cacheElement appendString: string];
}

- (void) parser: (NSXMLParser *) parser didEndElement:  (NSString *) elementName
                                        namespaceURI:   (NSString *) namespaceURI
                                        qualifiedName:  (NSString *) qName
{
    if ([elementName isEqualToString: @"configuration"]) return;
    
    NSLog(@"Injecting %@ with value \"%@\" to settings.", elementName, cacheElement);
    [settings injectDataWithName: elementName andValue: cacheElement];
}

- (void) parserDidEndDocument:(NSXMLParser *) parser {
    NSLog(@"Settings loaded!");
}
@end