//
//  AnimalViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 3/23/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AnimalsListViewController.h"

@interface AnimalsListViewController ()

@property (strong, nonatomic) NSArray *animalsArray;
@property (strong, nonatomic) NSMutableArray *filteredAnimalsArray;
@property (strong, nonatomic) AnimalDetailsViewController *detailsController;

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
    [self prepareNextViewController];
    
    [self setTitle: NSLocalizedString(@"titleControllerAnimalsList", nil)];
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.tableView deselectRowAtIndexPath:[self.tableView indexPathForSelectedRow] animated:YES];
}

- (void)initMenuBar
{
    _sidebarButton.target = self.revealViewController;
    _sidebarButton.action = @selector(revealToggle:);
    [self.view addGestureRecognizer:self.revealViewController.panGestureRecognizer];
}
- (void)prepareNextViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    _detailsController = (AnimalDetailsViewController *)[storyboard instantiateViewControllerWithIdentifier:@"details"];
}

- (void)initTableData
{
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"name" ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    
    AFParsedData *data = [AFParsedData sharedParsedData];

    _animalsArray = [data.animalsArray sortedArrayUsingDescriptors:sortDescriptors];
    _filteredAnimalsArray = [NSMutableArray arrayWithCapacity:_animalsArray.count];
    
    self.tableView.dataSource = self;
    self.tableView.delegate   = self;
}

#pragma mark - TableView delegate methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return [_filteredAnimalsArray count];
    } else {
        return [_animalsArray count];
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellId = @"MyReuseIdentifier";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
    }
    AFAnimal *animal;
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        animal = [_filteredAnimalsArray objectAtIndex:indexPath.section];
    } else {
        animal = [_animalsArray objectAtIndex:indexPath.section];
    }
    cell.textLabel.text = animal.name;
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(_filteredAnimalsArray.count != 0){
        [_detailsController setTitle:[[_filteredAnimalsArray objectAtIndex:[indexPath indexAtPosition:0]] name]];
    }
    else{
        [_detailsController setTitle:[[[self.tableView cellForRowAtIndexPath:indexPath] textLabel] text]];
    }
    [self.navigationController pushViewController:_detailsController animated:YES];
}
- (void)tableView:(UITableView *)tableView didHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark - Searchable methods

- (void)filterContentForSearchText:(NSString *)searchText scope:(NSString *)scope
{
    [_filteredAnimalsArray removeAllObjects];
    NSPredicate *predicate = [NSPredicate predicateWithFormat: @"SELF.name contains[cd] %@", searchText];
    _filteredAnimalsArray = [NSMutableArray arrayWithArray: [_animalsArray filteredArrayUsingPredicate: predicate]];
}

- (BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
    [self filterContentForSearchText:searchString scope:
     [[self.searchDisplayController.searchBar scopeButtonTitles] objectAtIndex:[self.searchDisplayController.searchBar selectedScopeButtonIndex]]];
    return YES;
}

@end