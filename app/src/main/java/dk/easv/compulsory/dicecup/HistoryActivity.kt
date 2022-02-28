package dk.easv.compulsory.dicecup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dk.easv.compulsory.dicecup.models.BeRoll


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

        val adapter = HistoryAdapter(this, rollHistory, false)
        val lvRollHistory = findViewById<ListView>(R.id.lvRollHistory)
        lvRollHistory.adapter = adapter

        // Trash icon
        val trashHistory = findViewById<ImageView>(R.id.imgTrash)
        trashHistory.setOnClickListener { deleteRollHistory() }

        // Toggle button to view pips as images or as text
        val togglePipImage = findViewById<ToggleButton>(R.id.tBPip)
        togglePipImage.setOnClickListener { changePipViewType(togglePipImage.isChecked) }

        if (savedInstanceState != null) {
            idCounter = savedInstanceState.getInt("idCounter")
        }
    }

    private fun changePipViewType(checked: Boolean) {
        if(checked){
            val adapter = HistoryAdapter(this, rollHistory, true)
            val lvRollHistory = findViewById<ListView>(R.id.lvRollHistory)
            lvRollHistory.adapter = adapter
        } else {
            val adapter = HistoryAdapter(this, rollHistory, false)
            val lvRollHistory = findViewById<ListView>(R.id.lvRollHistory)
            lvRollHistory.adapter = adapter
        }
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

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        state.putInt("idCounter", idCounter)
    }

    internal class HistoryAdapter(context: Context, private val rollHistory: ArrayList<BeRoll>, private val asImage: Boolean): ArrayAdapter<BeRoll>(context,  0, rollHistory) {

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

            if (asImage) { // show pips as image
                rollsView.visibility = View.GONE
                setDiceVisibility(roll.rolls.size, resView)
                showDiceImages(roll, resView)
            } else { // show pips as text
                setDiceVisibility(0, resView) // Hide all images
                // Display the dice of a roll
                val rollsTemp = ArrayList<Int>()
                for (i in 0 until roll.rolls.size step 1) {
                    rollsTemp.add(roll.rolls[i].pips)
                }

                rollsView.text = rollsTemp.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", " -")
            }

            return resView
        }

        private fun showDiceImages(diceRoll: BeRoll, v: View) {
            val imageViewList = ArrayList<ImageView>()
            imageViewList.add(v.findViewById(R.id.pipImg1))
            imageViewList.add(v.findViewById(R.id.pipImg2))
            imageViewList.add(v.findViewById(R.id.pipImg3))
            imageViewList.add(v.findViewById(R.id.pipImg4))
            imageViewList.add(v.findViewById(R.id.pipImg5))
            imageViewList.add(v.findViewById(R.id.pipImg6))

            // Sets the dice images depending on the dice that were rolled
            when (diceRoll.rolls.size) {
                1 -> {
                    for (i in 0 until imageViewList.size-5 step 1) {
                        // Using picasso to cache the images to reduce lag in larger list views
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
                2 -> {
                    for (i in 0 until imageViewList.size-4 step 1) {
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
                3 -> {
                    for (i in 0 until imageViewList.size-3 step 1) {
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
                4 -> {
                    for (i in 0 until imageViewList.size-2 step 1) {
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
                5 -> {
                    for (i in 0 until imageViewList.size-1 step 1) {
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
                6 -> {
                    for (i in 0 until imageViewList.size step 1) {
                        Picasso
                            .get()
                            .load(diceRoll.rolls[i].img)
                            .resize(100,100)
                            .onlyScaleDown()
                            .into(imageViewList[i])
                    }
                }
            }
        }

        private fun setDiceVisibility(diceAmount: Int, v: View) {
            val imageViewList = ArrayList<ImageView>()
            imageViewList.add(v.findViewById(R.id.pipImg1))
            imageViewList.add(v.findViewById(R.id.pipImg2))
            imageViewList.add(v.findViewById(R.id.pipImg3))
            imageViewList.add(v.findViewById(R.id.pipImg4))
            imageViewList.add(v.findViewById(R.id.pipImg5))
            imageViewList.add(v.findViewById(R.id.pipImg6))

            for (i in 0 until imageViewList.size step 1) {
                imageViewList[i].visibility = View.GONE
            }

            when(diceAmount) {
                1 -> {
                    for (i in 0 until imageViewList.size-5 step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
                2 -> {
                    for (i in 0 until imageViewList.size-4 step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
                3 -> {
                    for (i in 0 until imageViewList.size-3 step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
                4 -> {
                    for (i in 0 until imageViewList.size-2 step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
                5 -> {
                    for (i in 0 until imageViewList.size-1 step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
                6 -> {
                    for (i in 0 until imageViewList.size step 1) {
                        imageViewList[i].visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

