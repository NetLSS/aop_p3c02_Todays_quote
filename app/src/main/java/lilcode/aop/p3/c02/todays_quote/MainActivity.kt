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
        viewPager.setPageTransformer { page, position ->
            // position 현재 보이는 화면에서 상대적으로 어느 위치에 있는지

            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }

                position == 0F -> {
                    page.alpha = 1F
                }

                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }

    private fun initData() {
        // remoteConfig 설정
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 앱을 들어올 때마다 패치 하도록.
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 패치 작업이 완료 된 경우
            progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quote"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)

            }
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        val adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )

        viewPager.adapter = adapter
        // 중앙에서 시작
        viewPager.setCurrentItem(adapter.itemCount / 2, false)
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json) //JSONObject 로 구성 되어있는 배열
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            // null 이 아니면
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        // Quote 리스트로 변환
        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }

    }
}