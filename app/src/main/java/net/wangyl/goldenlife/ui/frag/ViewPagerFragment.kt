package net.wangyl.goldenlife.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.text.BidiFormatter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.wangyl.goldenlife.R
import net.wangyl.base.BaseBindFragment
import net.wangyl.goldenlife.databinding.FragmentViewpagerBinding
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment

class ViewPagerFragment : net.wangyl.base.BaseBindFragment<FragmentViewpagerBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_viewpager
    }

    override fun initView(v: View?, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return SlideshowFragment()
            }

            override fun getItemCount(): Int {
                return Card.DECK.size
            }
        }
    }

    class CardFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val cardView = CardView(layoutInflater, container)
            cardView.bind(Card.fromBundle(requireArguments()))
            return cardView.view
        }

        companion object {

            /** Creates a Fragment for a given [Card]  */
            fun create(card: Card): CardFragment {
                val fragment = CardFragment()
                fragment.arguments = card.toBundle()
                return fragment
            }
        }
    }

    /** Inflates and populates a [View] representing a [Card]  */
    class CardView(layoutInflater: LayoutInflater, container: ViewGroup?) {
        val view: View = layoutInflater.inflate(R.layout.item_card_layout, container, false)
        private val textSuite: TextView
        private val textCorner1: TextView
        private val textCorner2: TextView

        init {
            textSuite = view.findViewById(R.id.label_center)
            textCorner1 = view.findViewById(R.id.label_top)
            textCorner2 = view.findViewById(R.id.label_bottom)
        }

        /**
         * Updates the view to represent the passed in card
         */
        fun bind(card: Card) {
            textSuite.text = card.suit
            view.setBackgroundResource(getColorRes(card))

            val cornerLabel = card.cornerLabel
            textCorner1.text = cornerLabel
            textCorner2.text = cornerLabel
        }

        @ColorRes
        private fun getColorRes(card: Card): Int {
            val shade = getShade(card)
            val color = getColor(card)
            return COLOR_MAP[color][shade]
        }

        private fun getShade(card: Card): Int {
            when (card.value) {
                "2", "6", "10", "A" -> return 2
                "3", "7", "J" -> return 3
                "4", "8", "Q" -> return 0
                "5", "9", "K" -> return 1
            }
            throw IllegalStateException("Card value cannot be $card.value")
        }

        private fun getColor(card: Card): Int {
            when (card.suit) {
                "♣" -> return 0
                "♦" -> return 1
                "♥" -> return 2
                "♠" -> return 3
            }
            throw IllegalStateException("Card suit cannot be $card.suit")
        }

        companion object {
            private val COLOR_MAP = arrayOf(
                intArrayOf(R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700),
                intArrayOf(R.color.blue_100, R.color.blue_300, R.color.blue_500, R.color.blue_700),
                intArrayOf(R.color.green_100, R.color.green_300, R.color.green_500,
                    R.color.green_700),
                intArrayOf(R.color.yellow_100, R.color.yellow_300, R.color.yellow_500,
                    R.color.yellow_700))
        }
    }
    /**
     * Playing card
     */
    class Card private constructor(val suit: String, val value: String) {

        val cornerLabel: String
            get() = value + "\n" + suit

        /** Use in conjunction with [Card.fromBundle]  */
        fun toBundle(): Bundle {
            val args = Bundle(1)
            args.putStringArray(ARGS_BUNDLE, arrayOf(suit, value))
            return args
        }

        override fun toString(): String {
            val bidi = BidiFormatter.getInstance()
            if (!bidi.isRtlContext) {
                return bidi.unicodeWrap("$value $suit")
            } else {
                return bidi.unicodeWrap("$suit $value")
            }
        }

        companion object {
            internal val ARGS_BUNDLE = Card::class.java.name + ":Bundle"

            val SUITS = setOf("♣" /* clubs*/, "♦" /* diamonds*/, "♥" /* hearts*/, "♠" /*spades*/)
            val VALUES = setOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
            val DECK = SUITS.flatMap { suit ->
                VALUES.map { value -> Card(suit, value) }
            }

            fun List<Card>.find(value: String, suit: String): Card? {
                return find { it.value == value && it.suit == suit }
            }

            /** Use in conjunction with [Card.toBundle]  */
            fun fromBundle(bundle: Bundle): Card {
                val spec = bundle.getStringArray(ARGS_BUNDLE)
                return Card(spec!![0], spec[1])
            }
        }
    }
}