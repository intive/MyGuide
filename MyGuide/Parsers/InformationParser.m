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

static NSString *kXmlParkingsInformation = @"parkings_information";
static NSString *kXmlTicketsInformation  = @"information";
static NSString *kXmlOpeningInformation  = @"opening_information";

@interface InformationParser ()

@property (nonatomic) BOOL insideTicketsInformation;
@property (nonatomic) BOOL insideParkingsInformation;
@property (nonatomic) BOOL insideOpeningInformation;
@property (nonatomic) BOOL insideOpening;
@property (nonatomic) BOOL insideTicket;
@property (nonatomic) BOOL insideIndividual;
@property (nonatomic) BOOL insideGroup;

@property (nonatomic) Opening *currentOpening;
@property (nonatomic) Ticket  *currentTicket;
@property (nonatomic) Translatable *currentTicketsInformation;
@property (nonatomic) Translatable *currentParkingsInformation;
@property (nonatomic) Translatable *currentOpeningInformation;

@property (nonatomic) NSMutableArray  *openings;
@property (nonatomic) NSMutableArray  *tickets;
@property (nonatomic) NSMutableArray  *emails;

@property (nonatomic) InformationData *sharedParsedData;

@end

@implementation InformationParser

- (id) init {
    self = [super init];
    if(self) {
        _openings = [NSMutableArray arrayWithArray: @[]];
        _tickets  = [NSMutableArray arrayWithArray: @[]];
        _emails   = [NSMutableArray arrayWithArray: @[]];
        _sharedParsedData = [InformationData sharedParsedData];
        
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
        if (self.insideIndividual) {
            [self.currentTicket setTicketKind: Individual];
        }
        else if (self.insideGroup) {
            [self.currentTicket setTicketKind: Group];
        }
    }
    else if ([elementName isEqualToString: kXmlIndividual]) {
        self.insideIndividual = YES;
    }
    else if ([elementName isEqualToString: kXmlGroup]) {
        self.insideGroup = YES;
    }
    else if ([elementName isEqualToString: kXmlParkingsInformation]) {
        self.insideParkingsInformation = YES;
        self.currentParkingsInformation = [Translatable new];
    }
    else if ([elementName isEqualToString: kXmlTicketsInformation]) {
        self.insideTicketsInformation = YES;
        self.currentTicketsInformation = [Translatable new];
    }
    else if ([elementName isEqualToString: kXmlOpeningInformation]) {
        self.insideOpeningInformation = YES;
        self.currentOpeningInformation = [Translatable new];
    }
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString: kXmlOpening]) {
        self.insideOpening = NO;
        [self.openings addObject: self.currentOpening];
    }
    else if ([elementName isEqualToString: kXmlTicket]) {
        self.insideTicket = NO;
        [self.tickets addObject: self.currentTicket];
    }
    else if ([elementName isEqualToString: kXmlEmail]) {
        [self.emails addObject: self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlIndividual]) {
        self.insideIndividual = NO;
    }
    else if ([elementName isEqualToString: kXmlGroup]) {
        self.insideGroup = NO;
    }
    else if ([elementName isEqualToString: kXmlParkingsInformation]) {
        self.insideParkingsInformation = NO;
    }
    else if ([elementName isEqualToString: kXmlTicketsInformation]) {
        self.insideTicketsInformation = NO;
    }
    else if ([elementName isEqualToString: kXmlOpeningInformation]) {
        self.insideOpeningInformation = NO;
    }
    else if ([elementName isEqualToString: kXmlWeekdays]) {
        [self.currentOpening setWeekdaysHours: self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlWeekends]) {
        [self.currentOpening setWeekendsHours: self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlPrice]) {
        [self.currentTicket setPrice: [NSNumber numberWithDouble: [self.currentElement doubleValue]]];
    }
    else if ([elementName isEqualToString: kXmlEN] || [elementName isEqualToString: kXmlPL]) {
        if(self.insideOpening) {
            [self.currentOpening setName: self.currentElement withLanguage: elementName];
        }
        else if (self.insideTicket) {
            [self.currentTicket setName: self.currentElement withLanguage: elementName];
        }
        else if (self.insideTicketsInformation) {
            [self.currentTicketsInformation setName: self.currentElement withLanguage: elementName];
        }
        else if (self.insideParkingsInformation) {
            [self.currentParkingsInformation setName: self.currentElement withLanguage: elementName];
        }
        else if (self.insideParkingsInformation) {
            [self.currentParkingsInformation setName: self.currentElement withLanguage: elementName];
        }
        else if (self.insideOpeningInformation) {
            [self.currentOpeningInformation setName: self.currentElement withLanguage: elementName];
        }
    }
    else if ([elementName isEqualToString: kXmlTrams]) {
        [self.sharedParsedData setTrams: self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlAddress]) {
        NSString *address = [self.currentElement stringByReplacingOccurrencesOfString: @"\\n" withString: @"\n"];
        [self.sharedParsedData setAddress: address];
    }
    else if ([elementName isEqualToString: kXmlPhone]) {
        [self.sharedParsedData setTelephone: self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlWebsite]) {
        [self.sharedParsedData setWebsite: self.currentElement];
    }
}

- (void) parserDidEndDocument: (NSXMLParser *)parser {
    [super parserDidEndDocument: parser];
    
    [self.sharedParsedData setTicketsInformation: self.currentTicketsInformation];
    [self.sharedParsedData setParkingInformation: self.currentParkingsInformation];
    [self.sharedParsedData setOpeningInformation: self.currentOpeningInformation];
    
    [self.sharedParsedData setOpenings: self.openings];
    [self.sharedParsedData setTickets:  self.tickets];
    [self.sharedParsedData setEmails:   self.emails];
}

@end
