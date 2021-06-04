package lilcode.aop.p3.c02.todays_quote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }


    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 앱을 들어올 때마다 패치 하도록.
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 패치 작업이 완료 된 경우
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quote"))
                val isNameRevealed = true //remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)

            }
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean){
        viewPager.adapter = QuotesPagerAdapter(
            quotes
        )
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