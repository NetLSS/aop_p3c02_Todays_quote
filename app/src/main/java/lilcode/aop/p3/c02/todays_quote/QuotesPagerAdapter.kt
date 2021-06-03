package lilcode.aop.p3.c02.todays_quote

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

class QuotesPagerAdapter(private val quotes: List<Quote>) :
    RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int = quotes.size

    // 뷰 홀더
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote) {
            // 어떻게 랜더링 할 것인가
            quoteTextView.text = quote.quote // 명언 내용
            nameTextView.text = quote.name // 작가
        }
    }
}