package com.example.stockia.view.categories

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.CategoryTextButton
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.viewmodel.categories.CategoriesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun CategoriesView(
    navController: NavController,
    CategoriesViewModel: CategoriesViewModel = viewModel()

) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(CategoriesViewModel.resultMessage) {
        CategoriesViewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            CategoriesViewModel.clearResultMessage()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        // cada vez que cambie el backStack, recargamos
        CategoriesViewModel.loadCategories()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            HeaderWithBackArrow(
                text = "Categorías",
                onClick = { navController?.popBackStack() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = CategoriesViewModel.searchCategory,
                    onValueChange = CategoriesViewModel::onSearchCategoryChange,
                    label = "Buscar categoría",
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Nuevo",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.newCategoryView)
                        }
                )
            }
            Spacer(Modifier.height(16.dp))

            val isRefreshing = CategoriesViewModel.isLoading


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { CategoriesViewModel.loadCategories() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoriesViewModel.filteredCategories) { category ->
                        CategoryTextButton(
                            text = category.name,
                            onClick = {
                                navController.navigate("${Routes.EditCategoryView}/${category.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesViewPreview() {
    StockIATheme {
        CategoriesView(navController = rememberNavController())
    }
}