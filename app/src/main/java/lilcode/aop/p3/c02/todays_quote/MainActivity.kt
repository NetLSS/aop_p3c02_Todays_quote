package lilcode.aop.p3.c02.todays_quote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews() {

        // 뷰페이저 넘기는 방식 설정
        viewPager.setPageTransformer { page, position ->
            // position : 현재 보이는 화면에서 상대적으로 어느 위치에 있는지

            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }

                position == 0F -> {
                    page.alpha = 1F
                }

                else -> {
                    // 절반 이상 넘길 때 부터 급격하게 투명해지도록 설정.
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }

    private fun initData() {
        // remoteConfig 설정
        val remoteConfig = Firebase.remoteConfig

        // 비동기로 설정되게
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 앱을 들어올 때마다 패치 하도록.
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 패치 작업이 완료 된 경우
            progressBar.visibility = View.GONE // 로딩창 숨김

            if (it.isSuccessful) {
                // json 을 파싱하여 배열로 가져옴.
                val quotes: List<Quote> = parseQuotesJson(remoteConfig.getString("quote"))
                val isNameRevealed: Boolean = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)

            }
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        // 어뎁터를 생성하여 할
        val adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )

        viewPager.adapter = adapter
        // 중앙에서 시작당 하도록 (그래야 좌우 모두 무한 스크롤 가능하기 때문) 결국 끝에 도달하지만 사용자가 그렇게 하는 경우는 드뭄
        // smoothScroll 의 경우 부드러운 스크롤인데 중앙으로 그냥 한번에 보여줘야 하기 때문에 false 로 설정.
        viewPager.setCurrentItem(adapter.itemCount / 2, false)

    }

    // json 형식 파싱.
    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json) //JSONObject 로 구성 되어있는 배열
        var jsonList = emptyList<JSONObject>()

        // JSONArray 자체에서 foreach 등의 기능 제공하지 않으므로 아래와 같은 형태로 배열 생성 하였음.
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            // null 이 아니면 리스트에 추가
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        // Quote 리스트로 변환
        // map 을 사용하여 각 개체(JSONObject) 마다 Quote로 변환하여 Quote 리스트로 만듦.
        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }

    }
}