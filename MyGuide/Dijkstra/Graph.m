//
// Created by Kamil Lelonek on 5/14/14.
// Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Graph.h"
#import "Vertex.h"
#import "Edge.h"
#import "Heap.h"
#import "AFWay.h"
#import "AFJunction.h"

static double const LON_COEFFICIENT    = 0.6278947332157663;
static double const METERS_COEFFICIENT = 111324.25554213152;

@interface Graph ()

@property (nonatomic) NSMutableArray *vertices;
@property (nonatomic) NSMutableArray *edges;
@property (nonatomic) NSMutableArray *toRemove;

@end

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
    double deltaLat = sourceNode.latitude  - destinationNode.latitude;
    double deltaLon = (sourceNode.longitude - destinationNode.longitude) * LON_COEFFICIENT;
    return sqrt(deltaLat * deltaLat + deltaLon * deltaLon);
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
            result = METERS_COEFFICIENT * endVertex.weight;
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
        Edge   *sourceEdge        = v.edges[0];
        Edge   *destinationEdge   = v.edges[1];
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
    double      minLength   = INFINITY;
    Vertex      *nearVertex = nil;
    Edge        *nearEdge   = nil;
    for (Vertex *v in self.vertices) {
        double l = [self distanceApproximateBetween: node and: v.position];
        if (l < minLength || minLength == INFINITY) {
            nearVertex = v;
            minLength  = l;
        }
    }
    for (Edge   *e in self.edges) {
        double x0 = node.latitude;
        double y0 = node.longitude * LON_COEFFICIENT;
        double x1 = e.firstVertex.position.latitude;
        double y1 = e.firstVertex.position.longitude * LON_COEFFICIENT;
        double x2 = e.secondVertex.position.latitude;
        double y2 = e.secondVertex.position.longitude * LON_COEFFICIENT;
        double t  = ((x0 - x2) * (x1 - x2) + (y0 - y2) * (y1 - y2))
                    / ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        if (t > 0 && t < 1) {
            double x3       = x2 + (x1 - x2) * t;
            double y3       = (y2 + (y1 - y2) * t) / LON_COEFFICIENT;
            AFNode *newNode = [[AFNode alloc] initWithLatitude: x3 andLongitude: y3];
            double l = [self distanceApproximateBetween: node and: newNode];
            if (l < minLength) {
                minLength  = l;
                nearVertex = [Vertex new];
                [nearVertex setPosition: newNode];
                nearEdge = e;
            }
        }
    }
    if (nearEdge) {
        [self.vertices addObject: nearVertex];
        [self.toRemove addObject: nearVertex];

        Vertex *v1 = nearEdge.firstVertex;
        Vertex *v2 = nearEdge.secondVertex;

        Edge *e1 = [[Edge alloc] initWithFirstVertex: nearVertex
                                        secondVertex: v1
                                              length: [self distanceApproximateBetween: nearVertex.position
                                                                                   and: v1.position]];
        Edge *e2 = [[Edge alloc] initWithFirstVertex: nearVertex
                                        secondVertex: v2
                                              length: [self distanceApproximateBetween: nearVertex.position
                                                                                   and: v2.position]];

        [v1.edges addObject: e1];
        [v2.edges addObject: e2];

        [nearVertex.edges addObject: e1];
        [nearVertex.edges addObject: e2];
    }
    return nearVertex;
}

- (Vertex *) findVertexInGraph: (AFNode *)node
{
    for (Vertex *v in self.vertices) {
        if ([self doubleEqual:v.position.latitude withDouble:node.latitude] &&
            [self doubleEqual:v.position.longitude withDouble:node.longitude]) {
            return v;
        }
    }
    return nil;
}

- (BOOL) doubleEqual:(double)firstDouble withDouble:(double)secondDouble
{
    return fabs(firstDouble - secondDouble) <= DBL_EPSILON;
}


- (id) initWithWays: (NSArray *)ways andJunctions: (NSArray *)junctions
{
    self = [super init];

    if(self) {
        self.vertices = [NSMutableArray new];
        self.edges    = [NSMutableArray new];

        NSMutableDictionary *verticesInWays = [NSMutableDictionary new];

        for (AFWay *w in ways) {
            NSMutableArray *verticesInWay = [NSMutableArray new];
            Vertex         *last          = nil;
            for (AFNode    *n in w.nodesArray) {
                Vertex *v = [self findVertexInGraph: n];
                if (!v) {
                    v = [Vertex new];
                    v.position = [[AFNode alloc] initWithLatitude: n.latitude andLongitude: n.longitude];
                    [self.vertices addObject: v];
                }
                [verticesInWay addObject: v];
                if (last) {
                    Edge *e = [[Edge alloc] initWithFirstVertex: last
                                                   secondVertex: v
                                                         length: [self distanceApproximateBetween: last.position
                                                                                              and: v.position]];
                    [self.edges addObject: e];
                    [v.edges addObject: e];
                    [last.edges addObject: e];
                }
                last = v;
            }
            [verticesInWays setObject: verticesInWay forKey: w.wayID];
        }

        for (AFJunction *j in junctions) {
            Vertex *v = [self findVertexInGraph: j.coordinates];
            if (!v) {
                v = [Vertex new];
                v.position = [[AFNode alloc] initWithLatitude: j.coordinates.latitude andLongitude: j.coordinates.longitude];
                [self.vertices addObject: v];
            }
            for (AFWay *w in j.waysArray) {
                double      length = INFINITY;
                Vertex      *near  = nil;
                for (Vertex *v2 in [verticesInWays objectForKey: w.wayID]) {
                    double distance = [self distanceApproximateBetween: v.position and: v2.position];
                    if (length == INFINITY || length > distance) {
                        near   = v2;
                        length = distance;
                    }
                }
                if (near) {
                    Edge *e = [[Edge alloc] initWithFirstVertex: v secondVertex: near length: length];
                    if (![v isEqual: near] && [near.edges indexOfObject: e] == -1) {
                        [self.edges addObject: e];
                        [near.edges addObject: e];
                        [v.edges addObject: e];
                    }
                }
            }
        }
    }
    return self;
}

- (NSArray *) findPathBetweenNode: (AFNode *)sourceNode andNode: (AFNode *)destinationNode
{
    self.toRemove = [NSMutableArray new];
    Vertex         *startVertex = [self findNearVertex: sourceNode];
    Vertex         *endVertex   = [self findNearVertex: destinationNode];
    NSMutableArray *path        = [NSMutableArray new];

    if (startVertex && endVertex) {
        NSArray *vertices = [self findPathBetweenVertex: startVertex andVertex: endVertex];

        for (int i = (int)vertices.count - 1; i >= 0; i--) {
            [path addObject: ((Vertex *) vertices[(NSUInteger) i]).position];
        }
    }
    [self removeTemporaryVertices];
    return path;
}

- (NSArray *) findPathBetweenVertex: (Vertex *)sourceVertex andVertex: (Vertex *)destinationVertex
{
    for (Vertex *v in self.vertices) {
        v.weight      = INFINITY;
        v.predecessor = nil;
    }
    sourceVertex.weight      = 0;

    Heap *heap = [[Heap alloc] initWithCapacity: [self.vertices count]];

    Vertex *u = sourceVertex;
    while (u && ![u isEqual: destinationVertex]) {
        for (Edge *e in u.edges) {
            Vertex *v = e.firstVertex;
            if ([v isEqual: u]) {
                v = e.secondVertex;
            }
            if (v.weight == INFINITY) {
                v.weight      = u.weight + e.length;
                v.predecessor = u;
                [heap add: v];
            }
            else if (u.weight + e.length < v.weight) {
                v.weight      = u.weight + e.length;
                v.predecessor = u;
                [heap repairUp: (NSUInteger) v.heapIndex];
            }
        }
        u = [heap poll];
    }

    NSMutableArray *path = [NSMutableArray new];
    if (!destinationVertex.predecessor && ![sourceVertex isEqual: destinationVertex]) return path;
    [path addObject: destinationVertex];
    while (destinationVertex.predecessor) {
        [path addObject: destinationVertex.predecessor];
        destinationVertex = destinationVertex.predecessor;
    }
    return path;
}

@end
