//
//  AnimalViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/23/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AnimalsListViewController.h"

@interface AnimalsListViewController ()

@property (strong, nonatomic) NSArray *_animalsArray;
@property (strong, nonatomic) NSMutableArray *_filteredAnimalsArray;

@end

@implementation AnimalsListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _tableView.translatesAutoresizingMaskIntoConstraints = YES;
    _tableView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    
    [self initMenuBar];
    [self initTableData];
}

- (void) initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
    [self.view addGestureRecognizer: self.revealViewController.panGestureRecognizer];
}

- (void) initTableData
{
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey: @"name" ascending: YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject: sortDescriptor];
    
    AFParsedData *data = [AFParsedData sharedParsedData];
    self._animalsArray = [data.animalsArray sortedArrayUsingDescriptors:sortDescriptors];
    self._filteredAnimalsArray = [NSMutableArray arrayWithCapacity: [self._animalsArray count]];
    
    self.tableView.dataSource = self;
    self.tableView.delegate   = self;
}

#pragma mark - TableView delegate methods

- (NSInteger) numberOfSectionsInTableView: (UITableView *) tableView
{
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return [self._filteredAnimalsArray count];
    } else {
        return [self._animalsArray count];
    }
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section
{
    return 1;
}

- (UITableViewCell *) tableView: (UITableView *) tableView cellForRowAtIndexPath: (NSIndexPath *) indexPath
{
    static NSString *cellId = @"MyReuseIdentifier";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellId];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle: UITableViewCellStyleDefault reuseIdentifier: cellId];
    }
    AFAnimal *animal;
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        animal = [self._filteredAnimalsArray objectAtIndex:indexPath.section];
    } else {
        animal = [self._animalsArray objectAtIndex:indexPath.section];
    }
    cell.textLabel.text = animal.name;
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    return cell;
}

#pragma mark - Searchable methods

- (void) filterContentForSearchText: (NSString*) searchText scope: (NSString*) scope
{
    [self._filteredAnimalsArray removeAllObjects];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat: @"SELF.name contains[c] %@", searchText];
    self._filteredAnimalsArray = [NSMutableArray arrayWithArray: [self._animalsArray filteredArrayUsingPredicate: predicate]];
}

- (BOOL) searchDisplayController: (UISearchDisplayController *) controller
         shouldReloadTableForSearchString: (NSString *) searchString
{
    [self filterContentForSearchText: searchString scope:
     [[self.searchDisplayController.searchBar scopeButtonTitles] objectAtIndex:[self.searchDisplayController.searchBar selectedScopeButtonIndex]]];
    return YES;
}

@end