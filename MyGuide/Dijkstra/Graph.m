//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Graph.h"
#import "AFNode.h"
#import "Vertex.h"
#import "Edge.h"

static double const ZOO_LATITUDE       = 51.10503;
static double const LON_COEFFICIENT    = 0.6278947332157663;
static double const METERS_COEFFICIENT = 111324.25554213152;

@interface Graph ()

@property (nonatomic) NSMutableArray *vertices;
@property (nonatomic) NSMutableArray *edges;
@property (nonatomic) NSMutableArray *toRemove;

@end

// TODO - steal code from https://github.com/blstream/MyGuide/blob/android/src/com/blstream/myguide/path/Graph.java
@implementation Graph

- (id) init
{
    self = [super init];
    if (self) {
        _vertices = [NSMutableArray new];
        _edges    = [NSMutableArray new];
    }
    return self;
}

- (double) distanceApproximateBetween: (AFNode *)sourceNode and: (AFNode *)destinationNode
{
    double deltaLat = [sourceNode.latitude doubleValue] - [destinationNode.latitude doubleValue];
    double deltaLon = [sourceNode.longitude doubleValue] - [destinationNode.longitude doubleValue];
    return sqrt(pow(deltaLat, 2) + pow(deltaLon, 2));
}

- (double) findDistanceBetweenNode: (AFNode *)sourceNode andNode: (AFNode *)destinationNode
{
    self.toRemove = [NSMutableArray new];
    Vertex *startVertex = [self findNearVertex: sourceNode];
    Vertex *endVertex   = [self findNearVertex: destinationNode];
    double result       = INFINITY;

    if (startVertex && endVertex) {
        NSArray *vertices = [self findPathBetweenVertex: startVertex andVertex: endVertex];
        if (vertices.count) {
            result = METERS_COEFFICIENT * [endVertex.weight doubleValue];
            result += METERS_COEFFICIENT * [self distanceApproximateBetween: destinationNode and: endVertex.position];
            result += METERS_COEFFICIENT * [self distanceApproximateBetween: sourceNode and: startVertex.position];
        }
    }

    [self removeTemporaryVertices];
    return result;
}

- (void) removeTemporaryVertices
{
    if (!self.toRemove) return;
    for (Vertex *v in self.toRemove) {
        Edge   *sourceEdge        = [v.edges firstObject];
        Edge   *destinationEdge   = [v.edges objectAtIndex: 1];
        Vertex *sourceVertex      = sourceEdge.secondVertex;
        Vertex *destinationVertex = destinationEdge.secondVertex;
        [self.vertices removeObject: v];
        [sourceVertex.edges removeObject: sourceEdge];
        [destinationVertex.edges removeObject: destinationEdge];
    }

    self.toRemove = nil;
}

- (Vertex *) findNearVertex: (AFNode *)node
{
    // TODO - Graph.java:77
    Vertex *nearVertex = nil;
    return nearVertex;
}

- (Vertex *) findVertexInGraph: (AFNode *)node
{
    for (Vertex *v in self.vertices) {
        if (v.position.latitude == node.latitude && v.position.longitude == node.longitude) {
            return v;
        }
    }
    return nil;
}

- (void) createGraphWithWays: (NSArray *)ways andJunctions: (NSArray *)junctions
{
    // TODO - Graph.java:141
}

- (NSArray *) findPathBetweenNode: (AFNode *)sourceNode andNode: (AFNode *)destinationNode
{
    Vertex         *startVertex = [self findNearVertex: sourceNode];
    Vertex         *endVertex   = [self findNearVertex: destinationNode];
    NSMutableArray *path        = [NSMutableArray new];

    if (startVertex && endVertex) {
        NSArray *vertices = [self findPathBetweenVertex: startVertex andVertex: endVertex];

        for (int i = vertices.count - 1; i >= 0; i--) {
            [path addObject: ((Vertex *) vertices[i]).position];
        }
    }
    [self removeTemporaryVertices];
    return path;
}

- (NSArray *) findPathBetweenVertex: (Vertex *)sourceVertex andVertex: (Vertex *)destinationVertex
{
    // TODO - Graph.java:222
    NSMutableArray *path = [NSMutableArray new];
    return path;
}

@end
