package de.schiewe.volker.bgjugend2016.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import de.schiewe.volker.bgjugend2016.R
import de.schiewe.volker.bgjugend2016.database.DatabaseHelper
import de.schiewe.volker.bgjugend2016.fragments.*
import de.schiewe.volker.bgjugend2016.interfaces.OnListItemSelectedListener
import de.schiewe.volker.bgjugend2016.isNewVersion
import de.schiewe.volker.bgjugend2016.migrateToCurrentVersion
import de.schiewe.volker.bgjugend2016.models.Event
import de.schiewe.volker.bgjugend2016.views.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnListItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private val tag: String = this.javaClass.simpleName
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isNewVersion(PreferenceManager.getDefaultSharedPreferences(this), getString(R.string.last_version_key))) {
            migrateToCurrentVersion(this)
        }
        Fresco.initialize(this)
        auth = FirebaseAuth.getInstance()
        auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    //                    main_progress.visibility = View.GONE
                    if (task.isSuccessful) {
                        Log.d(tag, "Authentication successful")
                        val intentData = intent.data
                        if (intentData is Uri) {
                            // Deep Link and Notification click handling
                            handleIntentData(intentData)
                        } else if (savedInstanceState == null) {
                            // Open default Fragment
                            openFragment(EventListFragment.newInstance())
                        }
                    } else {
                        Log.d(tag, "Authentication failed.", task.exception)
                        // TODO show error screen
                    }
                }

        navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data is Uri)
            intent.data?.let { data ->
                handleIntentData(data)
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_events -> {
                val eventFragment = EventListFragment.newInstance()
                openFragment(eventFragment)
                return true
            }
            R.id.navigation_info -> {
                val infoFragment = InfoFragment.newInstance()
                openFragment(infoFragment)
                return true
            }
            R.id.navigation_settings -> {
                val preferenceFragment = PreferenceFragment.newInstance()
                openFragment(preferenceFragment)
                return true
            }
        }
        return false
    }

    override fun onFilterButtonClicked() {
        FilterModalBottomSheet().show(supportFragmentManager, "Sheet")
    }

    override fun onEventSelected() {
        val eventFragment = EventFragment.newInstance()
        openFragment(eventFragment, true)
    }

    private fun handleIntentData(uri: Uri) {
        DatabaseHelper(this).getEvents().observe(this, Observer { events ->
            val event = events?.singleOrNull { event -> event is Event && event.url !== "" && uri.toString().startsWith(event.url) }
            if (event != null) {
                val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
                sharedViewModel.select(event as Event)
                openFragment(EventFragment.newInstance())
            } else {
                Snackbar.make(container, "Falscher Link!", Snackbar.LENGTH_LONG).show()
                openFragment(EventListFragment.newInstance())

            }
            DatabaseHelper(this).getEvents().removeObservers(this@MainActivity)
        })
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (addToBackStack)
            transaction.addToBackStack(null)
        transaction.commit()
    }
}
