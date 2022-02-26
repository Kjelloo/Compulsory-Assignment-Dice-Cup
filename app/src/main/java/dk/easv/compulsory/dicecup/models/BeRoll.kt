package dk.easv.compulsory.dicecup.models

import java.util.*
import kotlin.collections.ArrayList


data class BeRoll(val id: Int, val rolls: ArrayList<BeRoll>, val timestamp: Date)
