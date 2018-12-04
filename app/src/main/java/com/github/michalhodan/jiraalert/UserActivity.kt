package com.github.michalhodan.jiraalert

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github.michalhodan.jiraalert.database.Database
import com.github.michalhodan.jiraalert.database.User as UserEntity
import com.github.michalhodan.jiraalert.database.Issue as IssueEntity
import com.github.michalhodan.jiraalert.database.Board as BoardEntity
import com.github.michalhodan.jiraalert.database.Sprint as SprintEntity
import com.github.michalhodan.jiraalert.storage.UrlImage
import com.github.michalhodan.jiraalert.storage.JIRADataViewModel
import com.github.michalhodan.jiraalert.storage.JIRADataViewModelFactory
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.app_bar_user.*
import kotlinx.android.synthetic.main.content_user.*
import kotlinx.android.synthetic.main.nav_header_user.*

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: JIRADataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val preferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)
        val url = preferences.getString("url", null)
        val authToken = preferences.getString("authToken", null)
        if (url == null || authToken == null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            return
        }

        Database.bootstrap(applicationContext)
        JIRADataViewModelFactory.bootstrap(url, authToken)

        viewModel = ViewModelProviders
            .of(this, JIRADataViewModelFactory())
            .get(JIRADataViewModel::class.java)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        viewModel.user().observe(this, Observer<UserEntity> {
            it ?: return@Observer

            nav_email.text = it.email
            nav_display_name.text = it.displayName

            UrlImage(this, "user.png").image(it.avatarUrl) { bitmap ->
                user_image.setImageBitmap(bitmap)
            }
        })

        viewModel.boards().observe(this, Observer<Map<Int, BoardEntity>> {
            it ?: return@Observer

            it.forEach { index, board ->
                nav_view.menu.add(0, board.id, index, board.name)
            }
        })

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.boards().observe(this, Observer<Map<Int, BoardEntity>> {
            val board = it?.get(item.itemId) ?: return@Observer

            viewModel.sprints(board.id).observe(this,  Observer<Map<Int, SprintEntity>>SprintObserver@{
                val sprint = it?.get(48) ?: return@SprintObserver

                viewModel.issues(board.id, sprint.id).observe(this, Observer<Map<Int, IssueEntity>>IssueObserver@ {
                    val issues = it ?: return@IssueObserver


                    issues.forEach {
//                        val view = TextView(this).apply {
//                            id = it.value.id
//                            text = it.value.key
//                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//                        }
//                        R.layout.content_tile
                           val v = TileView(baseContext, it.value)

                        linear_layout.addView(v)

//                        Toast.makeText(baseContext, "${it.value.key}", Toast.LENGTH_SHORT).show()
                    }
                })
            })

            Toast.makeText(baseContext, "selected ${board.name}", Toast.LENGTH_SHORT).show()
        })

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}