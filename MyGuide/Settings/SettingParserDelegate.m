//
//  SettingParserDelegate.m
//  MyGuide
//
//  Created by squixy on 23.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "SettingParserDelegate.h"

@implementation SettingParserDelegate {
    NSMutableString               *_cacheElement;
    SettingParserDelegateCallback  _callback;
}

- (id) initWithCallback: (SettingParserDelegateCallback) callback {
    self = [super init];
    if (self) {
        _callback = callback;
    }
    return self;
}

- (void) parserDidStartDocument:(NSXMLParser *) parser { MWLogInfo(@"Loading settings from file..."); }

- (void) parser: (NSXMLParser *) parser didStartElement: (NSString *)     elementName
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
    if(_callback) _callback(elementName, _cacheElement);
}

- (void) parserDidEndDocument:(NSXMLParser *) parser { MWLogInfo(@"Settings loaded!"); }
@end