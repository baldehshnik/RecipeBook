package com.firstapplication.recipebook.di

import android.content.Context
import com.firstapplication.recipebook.ui.fragments.HomeFragment
import com.firstapplication.recipebook.ui.fragments.IngredientsAddingDialogFragment
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppBindModule::class])
interface AppComponent {

    fun inject(fragment: HomeFragment)
    fun inject(fragment: RecipeAddingFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context): Builder

        fun build(): AppComponent
    }
}