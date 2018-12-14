package com.github.michalhodan.jiraalert

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.github.michalhodan.jiraalert.storage.JIRADataViewModelFactory
import com.github.michalhodan.jiraalert.storage.JIRAIssueDataViewModel
import com.github.michalhodan.jiraalert.storage.TileData

import kotlinx.android.synthetic.main.activity_issue.*

class IssueActivity : AppCompatActivity() {

    private lateinit var viewModel: JIRAIssueDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders
            .of(this, JIRADataViewModelFactory())
            .get(JIRAIssueDataViewModel::class.java)

        val issue = intent?.extras?.getInt("agileIssue")!!
        title = issue.toString()

        viewModel.detail(issue).observe(this, Observer<TileData> {
            it!!

            title = it.issue.key

            Toast.makeText(this, "issue: ${it.issue.key}", Toast.LENGTH_LONG).show()
        })
    }

}
