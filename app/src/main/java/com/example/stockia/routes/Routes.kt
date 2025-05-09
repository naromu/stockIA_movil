package com.example.stockia.routes

object Routes {
    const val LoginView = "loginView"
    const val RegisterView = "RegisterView"
    const val ConfirmEmailView = "ConfirmEmailView"
    const val HomeView = "HomeView"

    const val ResetPasswordOneView  = "ResetPasswordOneViewModel "
    const val ResetPasswordTwoView  = "ResetPasswordTwoViewModel "
    const val ResetPasswordThreeView  = "ResetPasswordThreeViewModel "

    const val CategoriesView = "CategoriesView"
    const val createCategoryView = "createCategoryView"
    const val EditCategoryView = "EditCategoryView"

    const val ClientsView = "ClientsView"
    const val NewClientView = "NewClientView"
    const val EditClientView = "EditClientView"

    //Procductos
    const val ProductsView = "ProductsView"
    const val CreateProductView = "CreateProductView"
    const val EditProductView = "EditProductView"

    // Proveedores
    const val ProvidersView = "ProvidersView"
    const val NewProviderView = "NewProviderView"
    const val EditProviderView = "EditProviderView"

    //Ordenes de Venta
    const val SalesOrdersView = "SalesOrdersView"
    const val NewSalesOrderView = "NewSalesOrderView"
    const val CompleteSalesOrderView = "CompleteSalesOrderView"
    const val CompleteSalesOrderViewWithArgs = "$CompleteSalesOrderView?selectedProducts={selectedProducts}"
    const val EditSalesOrderView = "EditSalesOrderView"

    //Ordenes de compra
    const val  PurchasesOrdersView = "PurchasesOrdersView"
    const val  NewPurchaseOrderView = "NewPurchaseOrderView"
    const val  CompletePurchasesOrderViewWithArgs = "CompletePurchasesOrderViewWithArgs"


    //IA
    const val  PredictionView = "PredictionView"

}