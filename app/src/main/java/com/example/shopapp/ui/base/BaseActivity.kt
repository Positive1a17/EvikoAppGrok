package com.example.shopapp.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
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
    protected lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(newBase: Context) {
        if (!::settingsManager.isInitialized) {
            super.attachBaseContext(newBase)
            return
        }
        val language = runBlocking {
            try {
                settingsManager.language.first()
            } catch (e: Exception) {
                Log.e("BaseActivity", "Error getting language", e)
                "ru"
            }
        }
        val context = LocaleUtils.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTheme()
        setupLanguage()
    }

    private fun setupTheme() {
        viewModelScope.launch {
            try {
                settingsManager.themeMode.collect { mode ->
                    setContent {
                        val themeMode by settingsManager.themeMode.collectAsState()
                        AppTheme(themeMode = themeMode) {
                            Content()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("BaseActivity", "Error setting up theme", e)
            }
        }
    }

    private fun setupLanguage() {
        viewModelScope.launch {
            try {
                settingsManager.language.collect { language ->
                    LocaleUtils.setLocale(this@BaseActivity, language)
                    recreate()
                }
            } catch (e: Exception) {
                Log.e("BaseActivity", "Error setting up language", e)
            }
        }
    }

    @Composable
    protected abstract fun Content()
} 