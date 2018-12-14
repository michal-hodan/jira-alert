package com.github.michalhodan.jiraalert

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.github.michalhodan.jiraalert.storage.TileData

class TileDialog(context: Context): AlertDialog(context) {

    companion object {
        fun create(context: Context, tileData: TileData): AlertDialog = AlertDialog.Builder(context).apply {
            setTitle(R.string.tile_select_action)
            setItems(R.array.tile_select_actions) { _, which ->
                when(which) {
                    // Detail
                    0 ->  {
                        val intent = Intent(context, IssueActivity::class.java)
                        intent.putExtra("agileIssue", tileData.issue.id)
                        context.startActivity(intent)
                    }
                    // Move
                    1 ->  {

                    }
                    // Refresh
                    2 ->  {
                        Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }.create()
    }
}