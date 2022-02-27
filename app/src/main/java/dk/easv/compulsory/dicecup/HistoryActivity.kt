package dk.easv.compulsory.dicecup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import dk.easv.compulsory.dicecup.models.BeRoll
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {

    private var idCounter = -1 // Init roll history id counter

    private var rollHistory = ArrayList<BeRoll>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Back arrow
        val returnToMainActivity = findViewById<ImageView>(R.id.imgBackArrow)
        returnToMainActivity.setOnClickListener{ startMainActivity() }

        // Gets rolling history from main activity
        val historyBundle = intent.getBundleExtra("historyBundle")
        rollHistory = historyBundle?.getSerializable("rollingHistory") as ArrayList<BeRoll>

        // Get the id counter from the main activity
        idCounter = intent.getIntExtra("idCounter", -1)

        val adapter = HistoryAdapter(this, rollHistory)
        val lvRollHistory = findViewById<ListView>(R.id.lvRollHistory)
        lvRollHistory.adapter = adapter

        // Trash icon
        val trashHistory = findViewById<ImageView>(R.id.imgTrash)
        trashHistory.setOnClickListener { deleteRollHistory() }

        // Toggle button to view pips as images or as text
        val togglePipImage = findViewById<ToggleButton>(R.id.tBPip)
    }

    private fun deleteRollHistory() {
        val lvRollHistory = findViewById<ListView>(R.id.lvRollHistory)
        lvRollHistory.adapter = null
        rollHistory.clear()
        idCounter = 1
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()

        rollHistory.reverse() // Reverse to make the history list go from bottom up
        // Saves rolling history in a separate bundle for serialization
        bundle.putSerializable("rollingHistory", rollHistory)
        intent.putExtra("idCounter", idCounter)
        intent.putExtra("historyBundle", bundle)

        startActivity(intent)
    }

    internal class HistoryAdapter(context: Context, private val rollHistory: ArrayList<BeRoll>): ArrayAdapter<BeRoll>(context,  0, rollHistory) {

        private val colours = intArrayOf(
            Color.parseColor("#bca7f2"),
            Color.parseColor("#8a6ad9")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v

            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.cell_extended, null)
            }

            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])

            val roll = rollHistory[position]
            val idView = resView.findViewById<TextView>(R.id.tvRollId)
            val timestampView = resView.findViewById<TextView>(R.id.tvRollTimeStamp)
            val rollsView = resView.findViewById<TextView>(R.id.tvRolls)

            idView.text = roll.id.toString()
            timestampView.text = roll.timestamp

            // Display the dice of a roll
            val rollsTemp = ArrayList<Int>()
            for (i in 0 until roll.rolls.size step 1) {
                rollsTemp.add(roll.rolls[i].pips)
            }

            rollsView.text = rollsTemp.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", " -")

            return resView
        }
    }
}

