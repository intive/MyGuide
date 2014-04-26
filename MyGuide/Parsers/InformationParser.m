//
//  InformationParser.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "InformationParser.h"
#import "Opening.h"
#import "Ticket.h"
#import "InformationData.h"

static NSString *kXmlOpening    = @"opening";
static NSString *kXmlWeekdays   = @"weekdays";
static NSString *kXmlWeekends   = @"weekends";
static NSString *kXmlEmail      = @"email";
static NSString *kXmlWebsite    = @"website";
static NSString *kXmlPhone      = @"telephone";
static NSString *kXmlAddress    = @"address";
static NSString *kXmlTicket     = @"ticket";
static NSString *kXmlGroup      = @"group";
static NSString *kXmlIndividual = @"individual";
static NSString *kXmlPrice      = @"price";
static NSString *kXmlTrams      = @"trams";
static NSString *kXmlParking    = @"parkings_information";

@interface InformationParser ()

@property (nonatomic) BOOL insideOpening;
@property (nonatomic) BOOL insideTicket;
@property (nonatomic) BOOL insideIndividual;
@property (nonatomic) BOOL insideGroup;

@property (nonatomic) Opening *currentOpening;
@property (nonatomic) Ticket  *currentTicket;

@property (nonatomic) NSArray *openings;
@property (nonatomic) NSArray *tickets;

@end


@implementation InformationParser

- (id) init {
    self = [super init];
    if(self) {
        _openings = @[];
        _tickets  = @[];
        
        self.fileName = @"information";
    }
    return self;
}

- (void) parser: (NSXMLParser *)  parser
didStartElement: (NSString *)     elementName
   namespaceURI: (NSString *)     namespaceURI
  qualifiedName: (NSString *)     qName
     attributes: (NSDictionary *) attributeDict
{
    [super parser: parser didStartElement:elementName namespaceURI:namespaceURI qualifiedName:qName attributes:attributeDict];
    
    if ([elementName isEqualToString: kXmlOpening]) {
        self.insideOpening = YES;
        self.currentOpening = [Opening new];
    }
    else if ([elementName isEqualToString: kXmlTicket]) {
        self.insideTicket = YES;
        self.currentTicket = [Ticket new];
    }
    else if ([elementName isEqualToString: kXmlIndividual]) {
        self.insideIndividual = YES;
    }
    else if ([elementName isEqualToString: kXmlGroup]) {
        self.insideGroup = YES;
    }
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString: kXmlOpening]) {
        self.insideOpening = NO;
    }
    else if ([elementName isEqualToString: kXmlTicket]) {
        self.insideTicket = NO;
    }
    else if ([elementName isEqualToString: kXmlIndividual]) {
        self.insideIndividual = NO;
    }
    else if ([elementName isEqualToString: kXmlGroup]) {
        self.insideGroup = NO;
    }
}

- (void) parserDidEndDocument: (NSXMLParser *)parser {
    [super parserDidEndDocument: parser];
    InformationData *sharedParsedData = [InformationData sharedParsedData];
}

@end
