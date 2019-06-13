package com.example.androidtv


import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import kotlinx.android.synthetic.main.popup_window.view.*


private const val MainFragmentTAG = "BLANK_FRAG"
private var backgroundManager:SimpleBackgroundManager? = null


class BlankFragment : BrowseFragment() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupUIElem()
        loadRows()
        setupEventListeners()
        backgroundManager = SimpleBackgroundManager(activity)
        super.onActivityCreated(savedInstanceState)
        Log.i(MainFragmentTAG, "* MainF - onActivityCreated *")

    }

    private fun setupUIElem() {
        title = "Galeria"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.windsor)
        searchAffordanceColor = resources.getColor(R.color.black_rock)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadRows(){
        val ROWS = 2

        val myRowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        for(i in 0..ROWS) {

            val cardList:List<Card>? = CardList().buildCardList(context, 3, i)
            var cardPresenterHeader:HeaderItem? = null
            cardPresenterHeader = when(i){
                0 -> HeaderItem(0, "Krajobrazy")
                1 -> HeaderItem(0, "Kwadraty")
                else -> HeaderItem(0, "Fantastyka")
            }

            val cardPresenter = CardPresenter()
            val cardRowAdapter = ArrayObjectAdapter(cardPresenter)

            for(ind in 0..2){
                cardRowAdapter.add(cardList!!.get(ind))
            }

            myRowsAdapter.add(ListRow(cardPresenterHeader, cardRowAdapter))

        }

        adapter = myRowsAdapter
    }

    private fun setupEventListeners(){
        setOnSearchClickedListener {
            Toast.makeText(activity, "Takie powiedzmy wyszukiwanie.", Toast.LENGTH_SHORT).show()
        }

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickedListener(this)
    }

    @SuppressLint("NewApi")
    fun onButtonShowPopupWindowClick(image:Int) {


        val inflater:LayoutInflater  = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_window, null)


        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val  popupWindow= PopupWindow(popupView, width, height, focusable)

        popupView.popup_image.setImageResource(image)


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


        popupView.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            popupWindow.dismiss()
            true

        }
    }
}


class CardPresenter:Presenter(){

    var context:Context? = null
    val WIDTH = 300
    val HEIGHT = 200

    override fun onCreateViewHolder(p0: ViewGroup?): ViewHolder {
        context = p0!!.context
        val cardView = ImageCardView(context)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        cardView.setBackgroundColor(context!!.resources.getColor(R.color.black_rock))

        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(p0: ViewHolder?, p1: Any?) {
        val card = p1 as Card
        val cardView = (p0!!.view) as ImageCardView
        cardView.titleText = card.title
        cardView.contentText = card.description
        cardView.mainImage = card.image
        cardView.setMainImageDimensions(WIDTH, HEIGHT)
    }

    override fun onUnbindViewHolder(p0: ViewHolder?) {
        val cardView = (p0!!.view) as ImageCardView
        cardView.mainImage = null
    }

}

class Card{
    var title = ""
    var description = ""
    var image:Drawable? = null
    var image_id:Int? = null
}

class CardList{
    val liczba = 3

    fun buildCardList(context: Context, count: Int, rodzaj:Int):List<Card>?{
        var title:Array<String>? = null
        var opis:Array<String>? = null
        var images:IntArray? = null
        when(rodzaj){
            0 -> {
                title = arrayOf("Krajobraz1", "Krajobraz2", "Krajobraz3")
                opis= arrayOf("Jezioro", "Inne jezioro", "Zachód")
                images = intArrayOf(R.drawable.krajo01, R.drawable.krajo02, R.drawable.krajo03)
            }
            1 -> {
                title = arrayOf("Kwadrat1", "Kwadrat2", "Kwadrat3")
                opis= arrayOf("Czarny kwadrat", "Biały kwadrat", "Dziwny kwadrat")
                images = intArrayOf(R.drawable.kwadrat01, R.drawable.kwadrat02, R.drawable.kwadrat03)
            }
            else -> {
                title = arrayOf("Fantasy1", "Fantasy2", "Fantasy3")
                opis= arrayOf("Smok", "Jednorożec", "Strzyga i Strzygoń")
                images = intArrayOf(R.drawable.fantasy01, R.drawable.fantasy02, R.drawable.fantasy03)
            }
        }


        if(count>liczba) return null
        val lista = ArrayList<Card>()

        for(ind in 0..liczba-1){
            val card = Card()
            card.title = title[ind]
            card.description = opis[ind]
            card.image = context.resources.getDrawable(images[ind])
            card.image_id = images[ind]
            lista.add(card)
        }
        return lista
    }

}

class ItemViewSelectedListener():OnItemViewSelectedListener{
    override fun onItemSelected(p0: Presenter.ViewHolder?, p1: Any?, p2: RowPresenter.ViewHolder?, p3: Row?) {
        if (p1 is String) { // GridItemPresenter row
            backgroundManager!!.clearBackground()
        } else if (p1 is Card) { // CardPresenter row
            backgroundManager!!.updateBackground(p1.image!!.mutate())
        }
        else{
            backgroundManager!!.clearBackground()
        }

    }
}

class ItemViewClickedListener(val frag: BlankFragment):OnItemViewClickedListener{
    override fun onItemClicked(p0: Presenter.ViewHolder?, p1: Any?, p2: RowPresenter.ViewHolder?, p3: Row?) {
        frag.onButtonShowPopupWindowClick((p1 as Card).image_id!!)
    }
}

