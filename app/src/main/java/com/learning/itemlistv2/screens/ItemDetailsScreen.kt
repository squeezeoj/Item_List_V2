package com.learning.itemlistv2.screens

//---------------------------------------------------------
// Imports
//---------------------------------------------------------
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.learning.itemlistv2.MainViewModel
import com.learning.itemlistv2.NavRoutes
import com.learning.itemlistv2.R
import com.learning.itemlistv2.models.Item
import com.learning.itemlistv2.utilities.SpacerDividerSpacer

//---------------------------------------------------------
// Items Detail Screen
//---------------------------------------------------------
@Composable
fun ItemDetailScreen(
	navController: NavHostController,
	viewModel: MainViewModel,
	id: Int? = 0,
	crud: String? = "NONE"
) {

	//--- Local Variables
	var itemID: String
	var itemTitle: String
	var myItem: Item

	//--- Scaffolding
	val scaffoldState =	rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
	Scaffold(
		scaffoldState = scaffoldState,

		//-----------------------------------------------------------------
		// Top Bar
		//-----------------------------------------------------------------
		topBar = { DetailsTopBar(navController) },

		//-----------------------------------------------------------------
		// Bottom Bar
		//-----------------------------------------------------------------
		bottomBar = { DetailsBottomBar() },

		//-----------------------------------------------------------------
		// Content
		//-----------------------------------------------------------------
		content = {	padding ->

			when (crud) {
				//-------------------------------------------------
				// Create New Item
				//-------------------------------------------------
				"CREATE" -> {

					//--- Content Card
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(15.dp),
						elevation = 10.dp
					) {

						//--- Content Column
						Column(
							modifier = Modifier.padding(15.dp),
							horizontalAlignment = CenterHorizontally
						) {

							//--- Card Title
							Text(
								modifier = Modifier.fillMaxWidth(),
								textAlign = TextAlign.Center,
								text = "Add New Item",
								style = MaterialTheme.typography.h4
							)
							SpacerDividerSpacer()

							//--- Item Title
							var textTitle by remember { mutableStateOf(TextFieldValue("")) }
							OutlinedTextField(
								value = textTitle,
								label = { Text(text = "Title") },
								singleLine = true,
								onValueChange = {
									textTitle = it
								}
							)
							itemTitle = textTitle.text

							//--- Create New Item from Parts
							myItem = Item(
								id = (100000..200000).random(),
								title = itemTitle
							)

							SpacerDividerSpacer()

							//--- Buttons Row
							Row {

								//--- Submit Button
								Button(
									onClick = {
										navController.navigate(route = NavRoutes.ItemListScreen.route) {
											popUpTo(NavRoutes.ItemListScreen.route)
										}                                    // Go to Item List Screen
										viewModel.insertItem(myItem)        // Add Item to List
										viewModel.updateShowList()            // Triggers Item List Recompose
									}) {
									Text(
										text = "Submit",
										style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
									)
								}    // End Submit Button

								Spacer(Modifier.width(10.dp))

								//--- Cancel Button
								Button(
									onClick = {
										navController.navigate(route = NavRoutes.ItemListScreen.route) {
											popUpTo(NavRoutes.ItemListScreen.route)
										}                                    // Go to Item List Screen
									}) {
									Text(
										text = "Cancel",
										style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
									)
								}    // End Cancel Button

							}    // End Buttons Row

						}    // End Column

					}	// End Card

				}
				//-------------------------------------------------
				// Update Existing Item
				//-------------------------------------------------
				"UPDATE" -> {

					//--- Make sure id isn't null, because it's not guaranteed
					if (id != null) {

						//--- Populate specific item view model variable
						viewModel.getItemByID(id)

						//--- Get specific item info
						myItem = viewModel.specificItem
						itemID = myItem.id.toString()
						itemTitle = myItem.title

						//--- Content Card
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.padding(15.dp),
							elevation = 10.dp
						) {

							//--- Content Column
							Column(
								modifier = Modifier.padding(15.dp),
								horizontalAlignment = CenterHorizontally
							) {

								//--- Card Title
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Update Item",
									style = MaterialTheme.typography.h4
								)
								SpacerDividerSpacer()

								//--- Item ID Text Field (read only)
								OutlinedTextField(
									value = itemID,
									enabled = false,
									readOnly = true,
									label = { Text(text = "ID") },
									singleLine = true,
									onValueChange = { }
								)

								//--- Item Title Text Field
								var textTitle by remember { mutableStateOf(TextFieldValue(itemTitle)) }
								OutlinedTextField(
									value = textTitle,
									label = { Text(text = "Title") },
									singleLine = true,
									onValueChange = {
										textTitle = it
									}
								)
								itemTitle = textTitle.text

								//--- Create Updated Item from Parts
								myItem = Item(
									id = itemID.toInt(),
									title = itemTitle
								)

								SpacerDividerSpacer()

								//--- Buttons Row
								Row {

									//--- Update Existing Item Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
											viewModel.updateItem(myItem)        // Update Item in List
											viewModel.updateShowList()            // Triggers Item List Recompose
										}) {
										Text(
											text = "Submit",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Update Existing Item Button

									Spacer(Modifier.width(10.dp))

									//--- Cancel Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
										}) {
										Text(
											text = "Cancel",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Cancel Button

								}    // End Buttons Row

							}   // End Column

						}	// End Card

					}   // End If Not Null

				}

				//-------------------------------------------------
				// Delete Item
				//-------------------------------------------------
				"DELETE" -> {

					//--- Make sure id isn't null, because it's not guaranteed
					if (id != null) {

						//--- Populate specific item view model variable
						viewModel.getItemByID(id)

						//--- Get specific item info
						myItem = viewModel.specificItem
						itemID = myItem.id.toString()
						itemTitle = myItem.title

						//--- Content Card
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.padding(15.dp),
							elevation = 10.dp
						) {

							//--- Content Column
							Column(
								modifier = Modifier.padding(15.dp),
								horizontalAlignment = CenterHorizontally
							) {

								//--- Card Title
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Delete Item",
									style = MaterialTheme.typography.h4
								)
								SpacerDividerSpacer()

								//--- Delete Confirmation
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Are you sure you want to permanently delete:\n\n",
									style = MaterialTheme.typography.body1
								)
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "$itemTitle\n",
									style = MaterialTheme.typography.h5
								)

								//--- Create Updated Item from Parts
								myItem = Item(
									id = itemID.toInt(),
									title = itemTitle
								)

								SpacerDividerSpacer()

								//--- Buttons Row
								Row(
									modifier = Modifier.fillMaxWidth(),
									horizontalArrangement = Arrangement.SpaceEvenly
								) {

									//--- Delete Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
											viewModel.deleteItem(myItem)        // Delete Item in List
											viewModel.updateShowList()            // Triggers Item List Recompose
										}) {
										Text(
											text = "Delete",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Update Existing Item Button

									Spacer(Modifier.width(10.dp))

									//--- Cancel Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
										}) {
										Text(
											text = "Cancel",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Cancel Button

								}    // End Buttons Row


							}   // End Column

						}	// End Card




					}   // End If Not Null

				}
				//-------------------------------------------------
				// Else
				//-------------------------------------------------
				else -> {
					// Nothing Else To Do
				}
			}   // End When

		},	// End Content

	)	// End Scaffold

}   	// End Item Details Screen

//---------------------------------------------------------
// Top Bar
//---------------------------------------------------------
@Composable
fun DetailsTopBar(navController: NavHostController) {
	TopAppBar(
		title = {
			Text(
				text = stringResource(R.string.app_name),
				style = MaterialTheme.typography.h5
			)
		},
		navigationIcon = {
			IconButton(onClick = {
				navController.navigate(route = NavRoutes.ItemListScreen.route) {
					popUpTo(NavRoutes.ItemListScreen.route)
				}
			}) {
				Icon(Icons.Filled.ArrowBack, "Back")
			}
		}	// End Navigation Icon
	)	// End Top App Bar
}	// End Details Top Bar Function


//---------------------------------------------------------
// Bottom Bar
//---------------------------------------------------------
@Composable
fun DetailsBottomBar() {
	BottomAppBar {
		Box(modifier = Modifier
			.fillMaxWidth(),
			contentAlignment = Alignment.BottomStart) {
			Text(
				text = "  " + stringResource(R.string.app_copyright),
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center
			)
		}
	}	// End Bottom App Bar
}	// End Details Bottom Bar Function
