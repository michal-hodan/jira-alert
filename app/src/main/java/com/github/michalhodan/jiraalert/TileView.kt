package com.github.michalhodan.jiraalert

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.michalhodan.jiraalert.database.IssueEntity
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

    constructor(context: Context, issue: IssueEntity): this(context, null) {
        issue_key.text = issue.key
    }

    private fun Int.notZero(callback: (Int) -> Unit) =  if (this != 0) callback(this) else Unit
}