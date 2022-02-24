package dk.easv.compulsory.dicecup

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import dk.easv.compulsory.dicecup.models.BeDie
import java.util.*


class MainActivity : AppCompatActivity() {

    private val dieOne = BeDie(1, R.drawable.dice1)
    private val dieTwo = BeDie(2, R.drawable.dice2)
    private val dieThree = BeDie(3, R.drawable.dice3)
    private val dieFour = BeDie(4, R.drawable.dice4)
    private val dieFive = BeDie(5, R.drawable.dice5)
    private val dieSix = BeDie(6, R.drawable.dice6)

    private val diceMap = mapOf<Int, BeDie>(1 to dieOne, 2 to dieTwo, 3 to dieThree,
                                            4 to dieFour, 5 to dieFive, 6 to dieSix)

    private val rng = Random()

    private var roll = 1
    private var rollsAsInt = ArrayList<Int>() // Needed for turn-safe because you can't save instances of custom models

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dice amount picker
        var rollPicker = findViewById<NumberPicker>(R.id.nPRollAmount)
        rollPicker.minValue = 1
        rollPicker.maxValue = 6
        rollPicker.wrapSelectorWheel = false
        rollPicker.value = 6
        rollPicker.setOnValueChangedListener { _, _, newVal ->
            roll = newVal
            setDiceView(newVal)
        }

        // Roll Button
        var btnRoll = findViewById<Button>(R.id.btnRoll)
        btnRoll.setOnClickListener { _ -> onRollClick(rollPicker.value) }

        if (savedInstanceState != null) {
            rollPicker.value = savedInstanceState.getInt("pickerValue") // Set number picker to previous value
            rollsAsInt = savedInstanceState.getIntegerArrayList("rolls") as ArrayList<Int>

            val rollsTemp = ArrayList<BeDie>()

            for (i in 1..rollsAsInt.size step 1) {
                rollsTemp.add(diceMap[rollsAsInt[i-1]]!!)
            }

            showDice(rollsTemp)
        }
    }

    private fun onRollClick(rollAmount: Int) {
        var roll = rollDice(rollAmount)
        showDice(roll)
    }

    private fun rollDice(rollAmount: Int): ArrayList<BeDie> {
        var rolls = arrayListOf<BeDie>()
        this.rollsAsInt.clear()

        for (i in 1..rollAmount step 1) {
            val roll = rng.nextInt(6) + 1
            rolls.add(diceMap[roll]!!)
            this.rollsAsInt.add(roll)
        }

        return rolls
    }

    private fun setDiceView(diceAmount: Int) {
        val diceImageOne = findViewById<ImageView>(R.id.imgDice1)
        val diceImageTwo = findViewById<ImageView>(R.id.imgDice2)
        val diceImageThree = findViewById<ImageView>(R.id.imgDice3)
        val diceImageFour = findViewById<ImageView>(R.id.imgDice4)
        val diceImageFive = findViewById<ImageView>(R.id.imgDice5)
        val diceImageSix = findViewById<ImageView>(R.id.imgDice6)

        diceImageOne.visibility = View.GONE
        diceImageTwo.visibility = View.GONE
        diceImageThree.visibility = View.GONE
        diceImageFour.visibility = View.GONE
        diceImageFive.visibility = View.GONE
        diceImageSix.visibility = View.GONE

        when(diceAmount) {
            1 -> diceImageOne.visibility = View.VISIBLE
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
    }

    private fun showDice(dice: ArrayList<BeDie>) {
        val diceImageOne = findViewById<ImageView>(R.id.imgDice1)
        val diceImageTwo = findViewById<ImageView>(R.id.imgDice2)
        val diceImageThree = findViewById<ImageView>(R.id.imgDice3)
        val diceImageFour = findViewById<ImageView>(R.id.imgDice4)
        val diceImageFive = findViewById<ImageView>(R.id.imgDice5)
        val diceImageSix = findViewById<ImageView>(R.id.imgDice6)

        when(dice.size) {
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
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        state.putIntegerArrayList("rolls", rollsAsInt)
        state.putInt("pickerValue", roll)
    }
}