package com.github.michalhodan.jiraalert

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.michalhodan.jiraalert.storage.TileData
import com.github.michalhodan.jiraalert.storage.UrlImage
import kotlinx.android.synthetic.main.content_issue.view.*
import kotlinx.android.synthetic.main.content_tile.view.*

class TileView(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs) {

   init {
       (getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
           .inflate(R.layout.content_tile, this, true)
   }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TileView)?.apply {
            getString(R.styleable.TileView_issueKey)?.let {
                tile_issue_key.text = it
            }
            getString(R.styleable.TileView_summary)?.let {
                tile_issue_summary.text = it
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
        tile_issue_key.text = data.issue.key
        tile_issue_summary.text = data.issue.summary

        data.issue.storyPoints?.let {
            story_points.text = it.toString()
        }

        assigne_name.text = data.assignee.displayName
        UrlImage.user(activity).apply { scale = 32 }.image(data.assignee.avatarUrl) {
            assigne_image.setImageBitmap(it)
        }
        if (data.issueType.name == "Feature") {
            issue_type_image.setImageResource(R.drawable.ic_task_feature)
        } else if (data.issueType.name == "Bug") {
            issue_type_image.setImageResource(R.drawable.ic_task_bug)
        }

        UrlImage.issueType(activity).apply { scale = 32 }.image(data.issueType.iconUrl) {
            issue_type_image.setImageBitmap(it)
        }

        when (data.priority.name) {
            "High" -> issue_priority_image.setImageResource(R.drawable.ic_major)
            "Medium" -> issue_priority_image.setImageResource(R.drawable.ic_minor)
            "Low" -> issue_priority_image.setImageResource(R.drawable.ic_trivial)
        }

        UrlImage.priority(activity).apply { scale = 24 }.image(data.priority.iconUrl) {
            issue_type_image.setImageBitmap(it)
        }
    }

    private fun Int.notZero(callback: (Int) -> Unit) =  if (this != 0) callback(this) else Unit
}