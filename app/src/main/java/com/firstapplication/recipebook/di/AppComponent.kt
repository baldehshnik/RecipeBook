package com.firstapplication.recipebook.di

import android.content.Context
import com.firstapplication.recipebook.ui.fragments.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppBindModule::class, ListenersBindModule::class])
interface AppComponent {

    fun inject(fragment: HomeFragment)
    fun inject(fragment: RecipeAddingFragment)
    fun inject(fragment: HubFragment)
    fun inject(fragment: RecipeSearchingFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context): Builder

        fun build(): AppComponent
    }
}