package dk.easv.compulsory.dicecup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import dk.easv.compulsory.dicecup.models.BeDie
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private val dieOne = BeDie(1, R.drawable.dice1)
    private val dieTwo = BeDie(2, R.drawable.dice2)
    private val dieThree = BeDie(3, R.drawable.dice3)
    private val dieFour = BeDie(4, R.drawable.dice4)
    private val dieFive = BeDie(5, R.drawable.dice5)
    private val dieSix = BeDie(6, R.drawable.dice6)

    private val diceMap = mapOf(1 to dieOne, 2 to dieTwo, 3 to dieThree,
                                4 to dieFour, 5 to dieFive, 6 to dieSix)

    private val rng = Random()

    private var roll = 1
    private var rollsAsInt = ArrayList<Int>() // Needed for turn-safety

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Dice amount picker
        val rollPicker = findViewById<NumberPicker>(R.id.nPRollAmount)
        rollPicker.minValue = 1
        rollPicker.maxValue = 6
        rollPicker.value = 6
        roll = rollPicker.value // Saving the number picker value for turn safety

        rollPicker.setOnValueChangedListener { _, _, newVal ->
            setDiceVisibility(newVal)
            roll = newVal
        }

        // Roll Button
        val btnRoll = findViewById<Button>(R.id.btnRoll)
        btnRoll.setOnClickListener { onRollButtonClick(rollPicker.value) }

        if (savedInstanceState != null) {
            rollPicker.value = savedInstanceState.getInt("pickerValue") // Set number picker to previous value
            rollsAsInt = savedInstanceState.getIntegerArrayList("rolls") as ArrayList<Int>

            val rollsTemp = ArrayList<BeDie>()

            for (i in 1..rollsAsInt.size step 1) {
                rollsTemp.add(diceMap[rollsAsInt[i-1]]!!)
            }

            updateDiceImage(rollsTemp)
            setDiceVisibility(rollsTemp.size)
        }
    }

    private fun onRollButtonClick(rollAmount: Int) {
        val roll = rollDice(rollAmount)
        updateDiceImage(roll)
    }

    private fun rollDice(rollAmount: Int): ArrayList<BeDie> {
        val rolls = arrayListOf<BeDie>()
        try {

            this.rollsAsInt.clear()

            // Depending on how many dice are chosen to be rolled, generate a random number between 1 and 6
            for (i in 1..rollAmount step 1) {
                val roll = rng.nextInt(6) + 1

                rolls.add(diceMap[roll]!!)
                this.rollsAsInt.add(roll)
            }
        } catch (e: Exception) {
            Log.d("EXCEPTION", e.message!!)
        }

        return rolls
    }

    private fun setDiceVisibility(diceAmount: Int) {
        try {
            val diceImageOne = findViewById<ImageView>(R.id.imgDice1)
            val diceImageTwo = findViewById<ImageView>(R.id.imgDice2)
            val diceImageThree = findViewById<ImageView>(R.id.imgDice3)
            val diceImageFour = findViewById<ImageView>(R.id.imgDice4)
            val diceImageFive = findViewById<ImageView>(R.id.imgDice5)
            val diceImageSix = findViewById<ImageView>(R.id.imgDice6)

            // Remove visibility of all dice images
            diceImageOne.visibility = View.GONE
            diceImageTwo.visibility = View.GONE
            diceImageThree.visibility = View.GONE
            diceImageFour.visibility = View.GONE
            diceImageFive.visibility = View.GONE
            diceImageSix.visibility = View.GONE

            // Adds dice image visibility depending on the amount of dice that need to be shown
            when(diceAmount) {
                1 -> {
                    diceImageOne.visibility = View.VISIBLE
                }
                2 -> {
                    diceImageOne.visibility = View.VISIBLE
                    diceImageTwo.visibility = View.VISIBLE
                }
                3 -> {
                    diceImageOne.visibility = View.VISIBLE
                    diceImageTwo.visibility = View.VISIBLE
                    diceImageThree.visibility = View.VISIBLE
                }
                4 -> {
                    diceImageOne.visibility = View.VISIBLE
                    diceImageTwo.visibility = View.VISIBLE
                    diceImageThree.visibility = View.VISIBLE
                    diceImageFour.visibility = View.VISIBLE
                }
                5 -> {
                    diceImageOne.visibility = View.VISIBLE
                    diceImageTwo.visibility = View.VISIBLE
                    diceImageThree.visibility = View.VISIBLE
                    diceImageFour.visibility = View.VISIBLE
                    diceImageFive.visibility = View.VISIBLE
                }
                6 -> {
                    diceImageOne.visibility = View.VISIBLE
                    diceImageTwo.visibility = View.VISIBLE
                    diceImageThree.visibility = View.VISIBLE
                    diceImageFour.visibility = View.VISIBLE
                    diceImageFive.visibility = View.VISIBLE
                    diceImageSix.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            Log.d("EXCEPTION", e.message!!)
        }
    }

    private fun updateDiceImage(dice: ArrayList<BeDie>) {
        try {
            val diceImageOne = findViewById<ImageView>(R.id.imgDice1)
            val diceImageTwo = findViewById<ImageView>(R.id.imgDice2)
            val diceImageThree = findViewById<ImageView>(R.id.imgDice3)
            val diceImageFour = findViewById<ImageView>(R.id.imgDice4)
            val diceImageFive = findViewById<ImageView>(R.id.imgDice5)
            val diceImageSix = findViewById<ImageView>(R.id.imgDice6)

            // Sets the dice images depending on how many dice are have been rolled
            when (dice.size) {
                1 -> diceImageOne.setImageResource(dice[0].img)
                2 -> {
                    diceImageOne.setImageResource(dice[0].img)
                    diceImageTwo.setImageResource(dice[1].img)
                }
                3 -> {
                    diceImageOne.setImageResource(dice[0].img)
                    diceImageTwo.setImageResource(dice[1].img)
                    diceImageThree.setImageResource(dice[2].img)
                }
                4 -> {
                    diceImageOne.setImageResource(dice[0].img)
                    diceImageTwo.setImageResource(dice[1].img)
                    diceImageThree.setImageResource(dice[2].img)
                    diceImageFour.setImageResource(dice[3].img)
                }
                5 -> {
                    diceImageOne.setImageResource(dice[0].img)
                    diceImageTwo.setImageResource(dice[1].img)
                    diceImageThree.setImageResource(dice[2].img)
                    diceImageFour.setImageResource(dice[3].img)
                    diceImageFive.setImageResource(dice[4].img)
                }
                6 -> {
                    diceImageOne.setImageResource(dice[0].img)
                    diceImageTwo.setImageResource(dice[1].img)
                    diceImageThree.setImageResource(dice[2].img)
                    diceImageFour.setImageResource(dice[3].img)
                    diceImageFive.setImageResource(dice[4].img)
                    diceImageSix.setImageResource(dice[5].img)
                }
            }
        } catch (e: Exception) {
            Log.d("EXCEPTION", e.message!!)
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        state.putIntegerArrayList("rolls", rollsAsInt)
        state.putInt("pickerValue", roll)
    }
}