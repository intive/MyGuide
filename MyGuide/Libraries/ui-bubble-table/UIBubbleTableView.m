#import "UIBubbleTableView.h"
#import "NSBubbleData.h"
#import "UIBubbleHeaderTableViewCell.h"

@interface UIBubbleTableView ()

@property (nonatomic) NSMutableArray *bubbleSection;

@end

@implementation UIBubbleTableView

#pragma mark - Initializators

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.delegate = self;
        self.dataSource = self;
        self.typingBubble = NSBubbleTypingTypeNobody;
    }
    return self;
}

#pragma mark - Override

- (void) reloadData
{
    self.showsVerticalScrollIndicator = NO;
    self.showsHorizontalScrollIndicator = NO;
    self.bubbleSection = [[NSMutableArray alloc] init];
    
    NSInteger count = 0;
    if (self.bubbleDataSource && (count = [self.bubbleDataSource rowsForBubbleTable:self]) > 0)
    {
        for (int i = 0; i < count; i++)
        {
            NSObject *data = [self.bubbleDataSource bubbleTableView:self dataForRow:i];
            [self.bubbleSection addObject: @[data]];
        }
    }
    
    [super reloadData];
}

#pragma mark - UITableViewDataSource implementation

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.bubbleSection count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if (section >= [self.bubbleSection count]) return 1;
    return [[self.bubbleSection objectAtIndex:section] count] + 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0)
    {
        return [UIBubbleHeaderTableViewCell height];
    }
    
    NSBubbleData *data = [[self.bubbleSection objectAtIndex:indexPath.section] objectAtIndex:indexPath.row - 1];
    return MAX(data.insets.top + data.view.frame.size.height + data.insets.bottom, self.showAvatars ? 52 : 0);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    // Header with date and time
    if (indexPath.row == 0)
    {
        static NSString *cellId = @"tblBubbleHeaderCell";
        UIBubbleHeaderTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
        NSBubbleData *data = [[self.bubbleSection objectAtIndex:indexPath.section] objectAtIndex:0];
        
        if (cell == nil) cell = [[UIBubbleHeaderTableViewCell alloc] init];
        
        cell.date = data.date;
        
        return cell;
    }
    
    // Standard bubble
    static NSString *cellId = @"tblBubbleCell";
    UIBubbleTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
    NSBubbleData *data = [[self.bubbleSection objectAtIndex:indexPath.section] objectAtIndex:indexPath.row - 1];
    
    if (cell == nil) cell = [[UIBubbleTableViewCell alloc] init];
    
    cell.data = data;
    cell.showAvatar = self.showAvatars;
    
    return cell;
}

@end