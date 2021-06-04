package lilcode.aop.p3.c02.todays_quote

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
onCreateViewHolder(ViewGroup parent, int viewType)	viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
onBindViewHolder(ViewHolder holder, int position)	position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
getItemCount()	전체 아이템 갯수 리턴.
 */

class QuotesPagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
) :
    RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition], isNameRevealed)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE //quotes.size

    // 뷰 홀더
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameRevealed: Boolean) {
            // 어떻게 랜더링 할 것인가
            quoteTextView.text = "\"${quote.quote}\"" // 명언 내용

            // 원격 isNameRevealed 에 따라 분기
            if (isNameRevealed) {
                nameTextView.text = "- ${quote.name}" // 작가
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }
        }
    }
}