package com.m_amer.bookshelf.navigation

enum class BookShelfScreens(private val routeTemplate: String) {
    SplashScreen("splash"),
    HomeScreen("home"),
    SearchScreen("search"),
    ShelfScreen("shelf"),
    BookScreen("book/{bookId}"),
    CategoryScreen("category/{categoryId}"),
    FavouriteScreen("favourite"),
    ReviewScreen("review/{bookId}");

    fun createRoute(vararg args: String): String {
        var route = routeTemplate
        args.forEach { value ->
            route = route.replaceFirst("\\{[^}]+}".toRegex(), value)
        }
        return route
    }
}