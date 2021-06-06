package com.bahaso.bahaso.core.data.local

import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.domain.Theory

object DataTheory {
    fun dailyWordTheory(): List<Theory> {
        val list = ArrayList<Theory>()
        list.add(Theory(id = 1,
            imageDrawable = R.drawable.eating,
            text = "Mangan",
            translation = "Makan"))
        list.add(Theory(id = 2, imageDrawable = 1, text = "Tuku", translation = "Beli"))
        list.add(Theory(id = 3,
            imageDrawable = R.drawable.jalan,
            text = "Mlaku",
            translation = "Jalan"))
        list.add(Theory(id = 4,
            imageDrawable = R.drawable.mandi,
            text = "Adus",
            translation = "Mandi"))
        list.add(Theory(id = 5, imageDrawable = 1, text = "Numpak", translation = "Naik"))
        list.add(Theory(id = 6,
            imageDrawable = R.drawable.membaca,
            text = "Moco",
            translation = "Baca"))
        list.add(Theory(id = 7,
            imageDrawable = R.drawable.menonton_tv,
            text = "Delok",
            translation = "Nonton"))
        list.add(Theory(id = 8,
            imageDrawable = R.drawable.minum,
            text = "Ngombe",
            translation = "Minum"))
        list.add(Theory(id = 9, imageDrawable = 1, text = "Mulih", translation = "Pulang"))
        list.add(Theory(id = 10, imageDrawable = 1, text = "Wisuh", translation = "Basuh"))
        return list
    }
}