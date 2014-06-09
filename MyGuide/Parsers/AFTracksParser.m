//
//  AFTracksParser.m
//  MyGuide
//
//  Created by afilipowicz on 02.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFTracksParser.h"
#import "AFTracksData.h"
#import "AFTrack.h"

static NSString *kXmlTrack        = @"track";
static NSString *kXmlName         = @"name";
static NSString *kXmlDescription  = @"description";
static NSString *kXmlImage        = @"image";
static NSString *kXmlAnimals      = @"animals";
static NSString *kXmlAnimalID     = @"animal_id";
static NSString *kXmlVersion      = @"version";

@interface AFTracksParser ()

@property (nonatomic) AFTrack        *currentTrack;
@property (nonatomic) NSMutableArray *tracks;
@property (nonatomic) NSMutableArray *animalsArray;
@property (nonatomic) NSString       *tracksPlistFilePath;
@property (nonatomic) NSNumber       *tracksXmlVersion;
@property (nonatomic) BOOL            insideName;

@end

@implementation AFTracksParser

- (id)init
{
    self = [super init];
    if(self) {
        self.fileName = @"tracks";
        _tracks  = [NSMutableArray new];
        _tracksPlistFilePath = [[NSBundle mainBundle] pathForResource:@"Tracks" ofType:@"plist"];
        _insideName = NO;
        _animalsArray = [NSMutableArray new];
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
    
    if ([elementName isEqualToString:kXmlTrack]) {
        self.currentTrack = [AFTrack new];
    }
    else if ([elementName isEqualToString:kXmlName]) {
        self.insideName = YES;
    }
}

- (void) parser: (NSXMLParser *) parser
  didEndElement: (NSString *)    elementName
   namespaceURI: (NSString *)    namespaceURI
  qualifiedName: (NSString *)    qName
{
    if ([elementName isEqualToString:kXmlTrack]) {
        [self.tracks addObject:self.currentTrack];
    }
    else if ([elementName isEqualToString:kXmlEN] || [elementName isEqualToString: kXmlPL]) {
        if(self.insideName){
            [self.currentTrack setName:[self currentElement] withLanguage:elementName];
        }
        else{
            [self.currentTrack setDescription:[self currentElement] withLanguage:elementName];
        }
    }
    else if ([elementName isEqualToString:kXmlName]){
        self.insideName = NO;
    }
    else if ([elementName isEqualToString:kXmlAnimals]){
        self.currentTrack.animalsArray = self.animalsArray;
        self.animalsArray = [NSMutableArray new];
    }
    else if ([elementName isEqualToString:kXmlAnimalID]){
        [self.animalsArray addObject:self.currentElement];
    }
    else if ([elementName isEqualToString: kXmlImage]) {
        self.currentTrack.image = self.currentElement;
    }
    else if ([elementName isEqualToString:kXmlVersion]) {
        self.tracksXmlVersion = [NSNumber numberWithUnsignedInteger:[self.currentElement integerValue]];
    }
}

- (void)parserDidEndDocument:(NSXMLParser *)parser
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    AFTracksData *sharedData = [AFTracksData sharedParsedData];

    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path;
    path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Tracks.plist"];

    NSFileManager *manager = [NSFileManager defaultManager];
    if ([manager fileExistsAtPath:path]) {
        NSDictionary *attributes = [manager attributesOfItemAtPath:path error:nil];
        unsigned long long size = [attributes fileSize];
    
        if((attributes && size == 0) || [[userDefaults valueForKey:@"tracks xml version"] integerValue] < self.tracksXmlVersion.integerValue) {
            [userDefaults setObject:[[self.tracks objectAtIndex:0] getName] forKey:@"current track"];
            [userDefaults setObject:self.tracksXmlVersion forKey:@"tracks xml version"];
        
            sharedData.tracks = self.tracks;
        }
        else{
            sharedData.tracks = [NSKeyedUnarchiver unarchiveObjectWithFile:path];
        }
    }
    else{
        [userDefaults setObject:[[self.tracks objectAtIndex:0] getName] forKey:@"current track"];
        [userDefaults setObject:self.tracksXmlVersion forKey:@"tracks xml version"];
        
        sharedData.tracks = self.tracks;
    }
    [userDefaults synchronize];

    [super parserDidEndDocument:parser];
}

@end
