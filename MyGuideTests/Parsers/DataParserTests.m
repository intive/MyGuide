//
//  DataParserTests.m
//  MyGuide
//
//  Created by afilipowicz on 23.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "AFXMLParser.h"
#import "AFParsedData.h"
#import "AFAnimal.h"
#import "AFWay.h"
#import "AFNode.h"
#import "AFJunction.h"
#import "XMLFetcher.h"

@interface DataParserTests : XCTestCase

@property (nonatomic, strong) AFXMLParser *dataXmlParser;
@property (nonatomic, strong) AFParsedData *sharedData;

@end

@implementation DataParserTests

static unsigned long vAnimalsAmount = 55;
static unsigned long vWaysAmount = 82;
static unsigned long vJunctionsAmount = 20;

- (void)setUp
{
    [super setUp];
    _sharedData = [AFParsedData sharedParsedData];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

#pragma mark - general tests
- (void)testIfDataFileExists
{
    NSData *data = [XMLFetcher fetchDataFromXML:@"data"];    
    XCTAssertNotNil(data, @"File data.xml does not exist in the main bundle and the documents folder");
}
- (void)testIfDataSingletonIsNotNil
{
    XCTAssertNotNil(_sharedData, @"Data wasn't properly stored in the singleton");
}
- (void)testIfParsingDataHaveSucceeded
{
    [_dataXmlParser parse];
    XCTAssertFalse(_dataXmlParser.parsingError, @"Paring data.xml failed");
}
- (void)testAmountOfAnimalsInSingleton
{
    XCTAssertEqual((unsigned long)[_sharedData.animalsArray count], vAnimalsAmount, @"The amount of animals (%lu) is not equal to the one expected on 22.02.2014 (%lu)", (unsigned long)[_sharedData.animalsArray count], vAnimalsAmount);
}
- (void)testAmountOfWaysInSingleton
{
    XCTAssertEqual((unsigned long)[_sharedData.waysArray count], vWaysAmount, @"The amount of ways (%lu) is not equal to the one expected on 22.02.2014 (%lu)", (unsigned long)[_sharedData.waysArray count], vWaysAmount);
}
- (void)testAmountOfJunctionsInSingleton
{
    XCTAssertEqual((unsigned long)[_sharedData.junctionsArray count], vJunctionsAmount, @"The amount of junctions (%lu) is not equal to the one expected on 22.02.2014 (%lu)", (unsigned long)[_sharedData.junctionsArray count], vJunctionsAmount);
}
#pragma mark - detail tests
- (void)testIfEachAnimalHasAName
{
    bool check = NO;
    for(AFAnimal *animal in _sharedData.animalsArray)
    {
        if([animal.name length] - 3 <= 0) check = YES;
    }
    XCTAssertFalse(check, @"There is an animal without a name");
}
- (void)testIfEachAnimalHasCoordinates
{
    bool check = NO;
    for(AFAnimal *animal in _sharedData.animalsArray)
    {
        if([animal.coordinates.latitude length] == 0 || [animal.coordinates.longitude length] == 0) check = YES;
    }
    XCTAssertFalse(check, @"There is an animal without coordinates");
}
- (void)testIfEachWayHasAnID
{
    bool check = NO;
    for(AFWay *way in _sharedData.waysArray)
    {
        if([way.wayID length] == 0) check = YES;
    }
    XCTAssertFalse(check, @"There is a way without an ID");
}
- (void)testIfEachWayHasANonEmptyNodesArray
{
    bool check = NO;
    for(AFWay *way in _sharedData.waysArray)
    {
        if(way.nodesArray == nil) check = YES;
    }
    XCTAssertFalse(check, @"There is a way with an empty nodes array");
}
- (void)testIfEachNodeHasValidCoordinates
{
    bool check = NO;
    for(AFAnimal *animal in _sharedData.animalsArray)
    {
        double lat = -360;
        double lon = -360;
        if(animal.coordinates.latitude != nil && [animal.coordinates.latitude length] > 0) lat = [animal.coordinates.latitude doubleValue];
        if(animal.coordinates.longitude != nil && [animal.coordinates.longitude length] > 0) lon = [animal.coordinates.longitude doubleValue];
        if(lat > 90 || lat < -90 || lon > 180 || lon < -180 ) check = YES;
    }
    for(AFWay *way in _sharedData.waysArray)
    {
        for(AFNode *node in way.nodesArray)
        {
            double lat = -360;
            double lon = -360;
            if(node.latitude != nil && [node.latitude length] > 0) lat = [node.latitude doubleValue];
            if(node.longitude != nil && [node.longitude length] > 0) lon = [node.longitude doubleValue];
            if(lat > 90 || lat < -90 || lon > 180 || lon < -180 ) check = YES;
        }
    }
    for(AFJunction *junction in _sharedData.junctionsArray)
    {
        double lat = -360;
        double lon = -360;
        if(junction.coordinates.latitude != nil && [junction.coordinates.latitude length] > 0) lat = [junction.coordinates.latitude doubleValue];
        if(junction.coordinates.longitude != nil && [junction.coordinates.longitude length] > 0) lon = [junction.coordinates.longitude doubleValue];
        if(lat > 90 || lat < -90 || lon > 180 || lon < -180 ) check = YES;
    }
    XCTAssertFalse(check, @"There is an node with invalid coordinates");
}
- (void)testIfEachJunctionsHasCoordinates
{
    bool check = NO;
    for(AFJunction *junction in _sharedData.junctionsArray)
    {
        if([junction.coordinates.latitude length] == 0 || [junction.coordinates.longitude length] == 0) check = YES;
    }
    XCTAssertFalse(check, @"There is a junction without coordinates");
}
- (void)testIfEachJunctionHasANonEmptyWaysArray
{
    bool check = NO;
    for(AFJunction *junction in _sharedData.junctionsArray)
    {
        if(junction.waysArray == nil) check = YES;
    }
    XCTAssertFalse(check, @"There is a junction without ways array");
}
- (void)testIfEachWayInWaysArrayOfEachJunctionHasAValidID
{
    bool majorCheck = YES;
    bool minorCheck = YES;
    for(AFJunction *junction in _sharedData.junctionsArray)
    {
        for(AFWay *way in junction.waysArray)
        {
            minorCheck = YES;
            for(AFWay *way2 in _sharedData.waysArray)
            {
                if([way.wayID isEqualToString: way2.wayID]) minorCheck = NO;
            }
            if(minorCheck == YES) majorCheck = NO;
        }
    }
    XCTAssertTrue(majorCheck, @"There is a way with an invalid ID within ways array of a certain junction");
    
}

@end
