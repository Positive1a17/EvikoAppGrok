package com.example.shopapp.ui.base

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.data.preferences.SettingsManager
import com.example.shopapp.utils.LocaleUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : ComponentActivity() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(newBase: Context) {
        val language = runBlocking {
            try {
                settingsManager.language.first()
            } catch (e: Exception) {
                "ru" // Значение по умолчанию в случае ошибки
            }
        }
        val context = LocaleUtils.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::settingsManager.isInitialized) {
            observeLanguageChanges()
        }
    }

    private fun observeLanguageChanges() {
        lifecycleScope.launch {
            try {
                settingsManager.language.collect { language ->
                    LocaleUtils.setLocale(this@BaseActivity, language)
                    recreate()
                }
            } catch (e: Exception) {
                // Обработка ошибок при изменении языка
            }
        }
    }
} 