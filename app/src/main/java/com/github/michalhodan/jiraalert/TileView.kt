package com.github.michalhodan.jiraalert

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.github.michalhodan.jiraalert.database.IssueEntity
import com.github.michalhodan.jiraalert.storage.TileData
import com.github.michalhodan.jiraalert.storage.UrlImage
import kotlinx.android.synthetic.main.content_tile.view.*

class TileView(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs) {

   init {
       (getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
           .inflate(R.layout.content_tile, this, true)
   }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TileView)?.apply {
            getString(R.styleable.TileView_issueKey)?.let {
                issue_key.text = it
            }
            getString(R.styleable.TileView_summary)?.let {
                summary.text = it
            }
            getString(R.styleable.TileView_storyPoints)?.let {
                story_points.text = it
            }

            getInteger(R.styleable.TileView_task, 0).notZero{

            }
            getInteger(R.styleable.TileView_priority, 0).notZero {

            }
            getInteger(R.styleable.TileView_assignee, 0).notZero {

            }

            recycle()
        }
    }

    constructor(activity: Activity, data: TileData): this(activity, null) {
        issue_key.text = data.issue.key
        summary.text = data.issue.summary

        data.issue.storyPoints?.let {
            story_points.text = it.toString()
        }

        UrlImage.user(activity).apply { scale = 32 }.image(data.assignee.avatarUrl) {
            assignee_image.setImageBitmap(it)
        }
        if (data.issueType.name == "Feature") {
            issue_type_image.setImageResource(R.drawable.ic_task_feature)
        }
        UrlImage.issueType(activity).apply { scale = 32 }.image(data.issueType.iconUrl) {
            issue_type_image.setImageBitmap(it)
        }

        if (data.priority.name == "High") {
            priority_image.setImageResource(R.drawable.ic_major)
        } else if (data.priority.name == "Medium") {
            priority_image.setImageResource(R.drawable.ic_minor)
        }
        UrlImage.priority(activity).apply { scale = 24 }.image(data.priority.iconUrl) {
            priority_image.setImageBitmap(it)
        }
    }

    private fun Int.notZero(callback: (Int) -> Unit) =  if (this != 0) callback(this) else Unit
}