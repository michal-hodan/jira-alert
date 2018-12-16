package com.github.michalhodan.jiraalert

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.michalhodan.jiraalert.storage.*

import kotlinx.android.synthetic.main.activity_issue.*
import kotlinx.android.synthetic.main.content_issue.*

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

        viewModel.detail(issue).observe(this, Observer<IssueDetailData> {
            it!!

            title = it.issue.key

            issue_key.text = it.issue.summary

            if (it.issueType.name == "Feature") {
                task_type_image.setImageResource(R.drawable.ic_task_feature)
            } else if (it.issueType.name == "Bug") {
                task_type_image.setImageResource(R.drawable.ic_task_bug)
            }
            task_type_label.text = it.issueType.name

            Log.d("priority-type", it.priority.name)

            when (it.priority.name) {
                "High" -> priority_image.setImageResource(R.drawable.ic_major)
                "Medium" -> priority_image.setImageResource(R.drawable.ic_minor)
                "Low" -> priority_image.setImageResource(R.drawable.ic_trivial)
            }
            priority.text = it.priority.name

            sprint.text = it.sprint.name

            UrlImage.user(this).image(it.assignee.avatarUrl) { assignee_image.setImageBitmap(it) }
            assignee_name.text = it.assignee.displayName

            UrlImage.user(this).image(it.creator.avatarUrl) { reporter_image.setImageBitmap(it) }
            reporter_name.text = it.creator.displayName

            project_name.text = it.project.key

            created_date.text = it.issue.created.substring(0, 10) + " " + it.issue.created.substring(11, 16)

            description.text = it.issue.description
        })
    }

}
