package com.ramotion.showroom.examples.navigationtoolbar

import com.ramotion.showroom.R

interface HeaderDataSet {
    data class ItemData(val gradient: Int,
                        val background: Int,
                        val title: String)

    fun getItemData(pos: Int): ItemData
}

interface PageDataSet {

    data class ItemData(val avatar: Int,
                       val userName: String,
                       val status: String)

    val secondItemImage: Int

    fun getItemData(pos: Int): ItemData
}

interface ViewPagerDataSet {
    fun getPageData(page: Int): PageDataSet
}

class ExampleDataSet {
    private val headerBackgrounds = intArrayOf(R.drawable.nt_card_1_background, R.drawable.nt_card_2_background, R.drawable.nt_card_3_background, R.drawable.nt_card_4_background).toTypedArray()
    private val headerGradients = intArrayOf(R.drawable.nt_card_1_gradient, R.drawable.nt_card_2_gradient, R.drawable.nt_card_3_gradient, R.drawable.nt_card_4_gradient).toTypedArray()
    private val headerTitles = arrayOf("TECHNOLOGY", "SCIENCE", "MOVIES", "GAMING")

    private val userNames = arrayOf("Aaron Bradley", "Barry Allen", "Bella Holmes", "Caroline Shaw", "Connor Graham", "Deann Hunt", "Ella Cole", "Jayden Shaw", "Jerry Carrol", "Lena Lucas", "Leonrd Kim", "Marc Baker", "Marjorie Ellis", "Mattew Jordan", "Ross Rodriguez", "Tina Caldwell", "Wallace Sutton")
    private val avatars = intArrayOf(R.drawable.nt_aaron_bradley, R.drawable.nt_barry_allen, R.drawable.nt_bella_holmes, R.drawable.nt_caroline_shaw, R.drawable.nt_connor_graham, R.drawable.nt_deann_hunt, R.drawable.nt_ella_cole, R.drawable.nt_jayden_shaw, R.drawable.nt_jerry_carrol, R.drawable.nt_lena_lucas, R.drawable.nt_leonrd_kim, R.drawable.nt_marc_baker, R.drawable.nt_marjorie_ellis, R.drawable.nt_mattew_jordan, R.drawable.nt_ross_rodriguez, R.drawable.nt_tina_caldwell, R.drawable.nt_wallace_sutton)
    private val statuses = arrayOf(
            "When the sensor experiments for deep space, all mermaids accelerate mysterious, vital moons.",
            "It is a cold powerdrain, sir.",
            "Particle of a calm shield, control the alignment!",
            "The human kahless quickly promises the phenomenan.",
            "Ionic cannon at the infinity room was the sensor of voyage, imitated to a dead pathway.",
            "Vital particles, to the port.",
            "Stars fly with hypnosis at the boldly infinity room!",
            "Hypnosis, definition, and powerdrain.",
            "When the queen experiments for nowhere, all particles control reliable, cold captains.",
            "When the c-beam experiments for astral city, all cosmonauts acquire remarkable, virtual lieutenant commanders.",
            "Starships walk with love at the cold parallel universe!",
            "Friendship at the bridge that is when quirky green people yell.")

    internal val headerDataSet = object : HeaderDataSet {
        override fun getItemData(pos: Int) =
                HeaderDataSet.ItemData(
                    gradient = headerGradients[pos % headerGradients.size],
                    background = headerBackgrounds[pos % headerBackgrounds.size],
                    title = headerTitles[pos % headerTitles.size])
    }

    internal val viewPagerDataSet = object : ViewPagerDataSet {
        val pageItemCount = 5

        override fun getPageData(page: Int) = object : PageDataSet {
            override val secondItemImage = headerDataSet.getItemData(page).background

            override fun getItemData(pos: Int): PageDataSet.ItemData {
                val localPos = page * pageItemCount + pos
                return PageDataSet.ItemData(
                        avatar = avatars[localPos % avatars.size],
                        userName = userNames[localPos % userNames.size],
                        status = statuses[localPos % statuses.size])
            }
        }
    }

}