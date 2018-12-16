package com.github.michalhodan.jiraalert

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.michalhodan.jiraalert.database.Column
import com.github.michalhodan.jiraalert.storage.TileData
import kotlinx.android.synthetic.main.content_tiles.view.*

class TilesView(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs)  {

    init {
        (getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.content_tiles, this, true)
    }

    constructor(activity: Activity, column: Column, tiles: List<TileData>, issueSelector: (TileData) -> Unit): this(activity.applicationContext, null) {
        column_name.text = column.name

        tiles.forEach { tileData ->
            val tile = TileView(activity, tileData)
            tile.setOnClickListener {
                issueSelector(tileData)
            }
            tiles_layout.addView(tile)
        }
    }
}