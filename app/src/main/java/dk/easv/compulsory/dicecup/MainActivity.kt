package dk.easv.compulsory.dicecup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dk.easv.compulsory.dicecup.models.BeDie
import dk.easv.compulsory.dicecup.models.BeRoll
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    private var diceAmount = 0
    private var currentRoll = ArrayList<BeDie>()
    private var rollHistory = ArrayList<BeRoll>()
    private var idCounter = 1

    private val diceHelper = DiceHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageViewList = ArrayList<ImageView>()
        imageViewList.add(findViewById(R.id.imgDice1))
        imageViewList.add(findViewById(R.id.imgDice2))
        imageViewList.add(findViewById(R.id.imgDice3))
        imageViewList.add(findViewById(R.id.imgDice4))
        imageViewList.add(findViewById(R.id.imgDice5))
        imageViewList.add(findViewById(R.id.imgDice6))

        // Dice amount picker
        val rollAmountPicker = findViewById<NumberPicker>(R.id.nPRollAmount)
        rollAmountPicker.minValue = 1
        rollAmountPicker.maxValue = 6
        rollAmountPicker.value = 6

        // Saving the number picker value for turn safety
        diceAmount = rollAmountPicker.value

        // Gets values from history activity
        if (intent.extras != null) {
            val historyBundle = intent.getBundleExtra("historyBundle")
            rollHistory = historyBundle?.getSerializable("rollingHistory") as ArrayList<BeRoll>
            idCounter = intent.getIntExtra("idCounter", -1)
        }

        rollAmountPicker.setOnValueChangedListener { _, _, newVal ->
            // Sets the amount of dice shown depending on the rollAmountPicker value
            diceHelper.setDiceVisibility(newVal, imageViewList)
            diceAmount = newVal
        }

        // Roll Button
        val btnRoll = findViewById<Button>(R.id.btnRoll)
        btnRoll.setOnClickListener { onRollButtonClick(rollAmountPicker.value, imageViewList) }

        if (savedInstanceState != null) {
            // Set number picker to previous value
            rollAmountPicker.value = savedInstanceState.getInt("pickerValue")
            currentRoll = savedInstanceState.getParcelableArrayList<BeDie>("rolls") as ArrayList<BeDie>
            rollHistory = savedInstanceState.getSerializable("rollingHistory") as ArrayList<BeRoll>
            diceAmount = rollAmountPicker.value

            // Sets the dice back to their state before the device was turned
            diceHelper.showDiceImages(500, currentRoll, imageViewList)
            diceHelper.setDiceVisibility(rollAmountPicker.value, imageViewList)
        }

        val historyList = findViewById<ImageView>(R.id.listImage)
        historyList.setOnClickListener { startHistoryActivity() }
    }

    private fun startHistoryActivity() {
        val bundle = Bundle()
        val intent = Intent(this, HistoryActivity::class.java)

        // Reverse the list to return to the normal top down list
        rollHistory.reverse()

        // Putting extra data before opening the history activity
        bundle.putSerializable("rollingHistory", rollHistory)
        intent.putExtra("idCounter", idCounter)
        intent.putExtra("historyBundle", bundle)

        startActivity(intent)
    }

    private fun onRollButtonClick(rollAmount: Int, imageViewList: ArrayList<ImageView>) {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedDate = df.format(c)

        val roll = rollDice(rollAmount)
        diceHelper.showDiceImages(500, roll, imageViewList)

        rollHistory.add(BeRoll(idCounter++, roll, formattedDate))
    }

    private fun rollDice(rollAmount: Int): ArrayList<BeDie> {
        val rolls = arrayListOf<BeDie>()
        try {
            // Depending on how many dice are chosen to be rolled, generate a random number between 1 and 6
            for (i in 1..rollAmount step 1) {
                val roll = rng.nextInt(6) + 1
                rolls.add(diceMap[roll]!!)
            }

            currentRoll = rolls
        } catch (e: Exception) {
            Log.d("EXCEPTION", e.message!!)
        }

        return rolls
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        state.putSerializable("rollingHistory", rollHistory)
        state.putParcelableArrayList("rolls", currentRoll)
        state.putInt("pickerValue", diceAmount)
    }
}