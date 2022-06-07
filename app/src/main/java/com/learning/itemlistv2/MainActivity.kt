package com.learning.itemlistv2

//----------------------------------------------------------
// Imports
//----------------------------------------------------------
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learning.itemlistv2.models.Item
import com.learning.itemlistv2.screens.ItemDetailScreen
import com.learning.itemlistv2.screens.ItemListScreen
import com.learning.itemlistv2.ui.theme.ItemListV2Theme

//----------------------------------------------------------
// Main View Model
//----------------------------------------------------------
class MainViewModel {

	//------------------------------------------------------
	// Items
	//------------------------------------------------------
	private var allItems: List<Item>		// All Items List
	var showItems: List<Item>		// Show Items List
	var specificItem: Item			// Specific Item

	//------------------------------------------------------
	// Filtering Variables
	//------------------------------------------------------
	var filtering: Boolean			// Filtering Boolean
	var filterText: String			// Filter Text

	//------------------------------------------------------
	// Sorting Variables
	//------------------------------------------------------
	var sortAscending: Boolean		// Sort Order

	//------------------------------------------------------
	// Initialize View Model
	//------------------------------------------------------
	init {

		val item01 = Item(id = 1, title = "First Item")
		val item02 = Item(id = 2, title = "Second Item")
		val item03 = Item(id = 3, title = "Third Item")

		allItems = mutableListOf(item01, item02, item03)
		specificItem = Item(id = 0, title = "Initial Item")

		filtering = false
		filterText = ""

		sortAscending = true

		showItems = allItems		//	Start by Showing All

	}   // End Initializer

	//------------------------------------------------------
	// Item Methods
	//------------------------------------------------------
	fun insertItem(item: Item) {
		allItems = allItems + item
	}

	fun updateItem(item: Item) {
		allItems.find { it.id == item.id }?.title = item.title
	}

	fun updateShowList() {		// Used on Item Details Screen

		var tempItems: List<Item> = if (filtering) {
			allItems.filter { it.title.contains(filterText) }
		} else {
			allItems
		}

		tempItems = if (sortAscending) {
			tempItems.sortedBy { it.id }
		} else {
			tempItems.sortedByDescending { it.id }
		}

		showItems = tempItems

	}

	fun returnShowList():List<Item> {	// Used on Item List Screen

		var tempItems: List<Item> = if (filtering) {
			allItems.filter { it.title.contains(filterText) }
		} else {
			allItems
		}

		tempItems = if (sortAscending) {
			tempItems.sortedBy { it.id }
		} else {
			tempItems.sortedByDescending { it.id }
		}

		return tempItems

	}

	fun getItemByID(id: Int) {
		allItems.forEach {
			if (it.id == id) {
				specificItem.id = it.id
				specificItem.title = it.title
			}
		}
	}

	fun deleteItem(item: Item) {
		allItems = allItems - item
	}

}   // End Main View Model


//----------------------------------------------------------
// Navigation Routes
//----------------------------------------------------------
sealed class NavRoutes(val route: String) {
	object ItemListScreen : NavRoutes("itemList")
	object ItemDetailScreen : NavRoutes("itemDetail")
}

//----------------------------------------------------------
// Main Activity
//----------------------------------------------------------
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ItemListV2Theme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					AppSetup()
				}
			}
		}
	}
}

//---------------------------------------------------------
// Setup App
//---------------------------------------------------------
@Composable
fun AppSetup(
	viewModel: MainViewModel = MainViewModel()
) {

	//--- Navigation Setup
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = NavRoutes.ItemListScreen.route,
	) {

		//--- Item List Screen
		composable(NavRoutes.ItemListScreen.route) {
			ItemListScreen(
				navController = navController,
				viewModel = viewModel
			)
		}

		//--- Item Detail Screen
		composable(NavRoutes.ItemDetailScreen.route + "/{id}" + "/{crud}") { backStackEntry ->
			val id = backStackEntry.arguments?.getInt("id")
			val crud = backStackEntry.arguments?.getString("crud")
			ItemDetailScreen(
				navController = navController,
				viewModel = viewModel,
				id = id,
				crud = crud
			)
		}

	}	// End Nav Host

}