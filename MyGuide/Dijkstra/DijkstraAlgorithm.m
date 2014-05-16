//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "DijkstraAlgorithm.h"
#import "AFParsedData.h"
#import "AFWay.h"

@interface DijkstraAlgorithm ()

@property NSMutableArray *nodes;
@property AFParsedData   *data;

@end

@implementation DijkstraAlgorithm

- (id) init
{
    self = [super self];
    if (self) {
        _nodes = [NSMutableArray new];
        _data  = [AFParsedData sharedParsedData];
        [self buildNodesArray];
    }
    return self;
}

- (void) buildNodesArray
{
    for (AFWay *way in self.data.waysArray) {
        for (AFNode *node in way.nodesArray) {
            [self.nodes addObject: node];
        }
    }
}

- (NSArray *) findShortestBathBetween: (CLLocation *)sourceLocation and: (CLLocation *)destinationLocation
{
    NSMutableArray *result          = [NSMutableArray new];
    AFNode         *sourceNode      = [self findClosestNodeForLocation: sourceLocation];
    AFNode         *destinationNode = [self findClosestNodeForLocation: destinationLocation];
    // TODO
    return result;
}

- (AFNode *) findClosestNodeForLocation: (CLLocation *)location
{
    AFNode      *closestNode = [self.nodes firstObject];
    for (AFNode *node in self.nodes) {
        NSInteger closestDistance = [closestNode distanceFromLocation: location];
        NSInteger currentDistance = [node distanceFromLocation: location];
        if (currentDistance < closestDistance) {
            closestNode = node;
        }
    }
    return closestNode;
}


- (AFNode *) findClosestNodeForNode: (AFNode *)destinationNode withinNodes: (NSArray *)nodesArray
{
    AFNode      *closestNode = [nodesArray firstObject];
    for (AFNode *node in nodesArray) {
        double closestDistance = [self distanveBetweenSourceNode: closestNode andDestinationNode: destinationNode];
        double currentDistance = [self distanveBetweenSourceNode: node andDestinationNode: destinationNode];
        if (currentDistance < closestDistance) {
            closestNode = node;
        }
    }
    return closestNode;
}

- (double) distanveBetweenSourceNode: (AFNode *)sourceNode andDestinationNode: (AFNode *)destinationNode
{
    double dx = [sourceNode.latitude doubleValue] - [destinationNode.latitude doubleValue];
    double dy = [sourceNode.longitude doubleValue] - [destinationNode.longitude doubleValue];
    return sqrt(dx * dx + dy * dy);
}

@end
