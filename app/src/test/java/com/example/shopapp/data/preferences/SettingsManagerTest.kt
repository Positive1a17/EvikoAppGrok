package com.example.shopapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsManagerTest {
    private lateinit var settingsManager: SettingsManager
    private lateinit var testDataStore: DataStore<Preferences>
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { context.preferencesDataStoreFile("test_settings") }
        )
        settingsManager = SettingsManager(context)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `theme mode defaults to system`() = runTest {
        val themeMode = settingsManager.themeMode.first()
        assertEquals(SettingsManager.THEME_MODE_SYSTEM, themeMode)
    }

    @Test
    fun `language defaults to Russian`() = runTest {
        val language = settingsManager.language.first()
        assertEquals("ru", language)
    }

    @Test
    fun `notifications enabled by default`() = runTest {
        val notificationsEnabled = settingsManager.notificationsEnabled.first()
        assertEquals(true, notificationsEnabled)
    }

    @Test
    fun `can set and get theme mode`() = runTest {
        settingsManager.setThemeMode(SettingsManager.THEME_MODE_DARK)
        val themeMode = settingsManager.themeMode.first()
        assertEquals(SettingsManager.THEME_MODE_DARK, themeMode)
    }

    @Test
    fun `can set and get language`() = runTest {
        settingsManager.setLanguage("en")
        val language = settingsManager.language.first()
        assertEquals("en", language)
    }

    @Test
    fun `can set and get notifications enabled`() = runTest {
        settingsManager.setNotificationsEnabled(false)
        val notificationsEnabled = settingsManager.notificationsEnabled.first()
        assertEquals(false, notificationsEnabled)
    }
} 