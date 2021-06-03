package lilcode.aop.p3.c02.todays_quote

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*
onCreateViewHolder(ViewGroup parent, int viewType)	viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
onBindViewHolder(ViewHolder holder, int position)	position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
getItemCount()	전체 아이템 갯수 리턴.
 */

class QuotesPagerAdapter: RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}