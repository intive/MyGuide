#import "UIBubbleHeaderTableViewCell.h"

@interface UIBubbleHeaderTableViewCell ()

@property UILabel *label;

@end

#define HEADER_HEIGHT 28.0
#define FONT_SIZE 12

@implementation UIBubbleHeaderTableViewCell

- (id) init
{
    self = [super init];
    if(self) {
        self.label                  = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, HEADER_HEIGHT)];
        self.label.textAlignment    = NSTextAlignmentCenter;
        self.label.shadowOffset     = CGSizeMake(0, 1);
        self.label.font             = [UIFont fontWithName: @"HelveticaNeue-CondensedBold" size: FONT_SIZE];
        self.label.shadowColor      = [UIColor whiteColor];
        self.label.backgroundColor  = [UIColor clearColor];
        self.selectionStyle         = UITableViewCellSelectionStyleNone;
        [self addSubview: self.label];
    }
    return self;
}

+ (CGFloat) height
{
    return HEADER_HEIGHT;
}

- (void)setDate:(NSString *)value
{
    self.label.text = value;
}

@end