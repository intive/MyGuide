//
//  HistoryData.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

@interface HistoryData : NSObject

@property (nonatomic) NSArray *historyEvents;

+ (id) sharedParsedData;

@end
