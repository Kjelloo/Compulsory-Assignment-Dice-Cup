package dk.easv.compulsory.dicecup

import android.view.View
import android.widget.ImageView
import android.widget.ListView
import com.squareup.picasso.Picasso
import dk.easv.compulsory.dicecup.models.BeDie

class DiceHelper {
    fun showDiceImages(imageSize: Int, diceRoll: ArrayList<BeDie>, imageViewList: ArrayList<ImageView>) {
        for (i in 0 until diceRoll.size) {
            Picasso
                .get()
                .load(diceRoll[i].img)
                .resize(imageSize, imageSize)
                .onlyScaleDown()
                .into(imageViewList[i])
        }
    }

    fun setDiceVisibility(diceAmount: Int, imageViewList: ArrayList<ImageView>) {
        // Set all dice to gone
        for (i in 0 until imageViewList.size) {
            imageViewList[i].visibility = View.GONE
        }

        // Set needed amount of dice to visible
        for (i in 0 until diceAmount) {
            imageViewList[i].visibility = View.VISIBLE
        }
    }

}