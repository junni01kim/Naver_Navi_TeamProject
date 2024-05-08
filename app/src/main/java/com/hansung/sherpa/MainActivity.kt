package com.hansung.sherpa

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.map.NaverMapSdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID) // 본인 api key

        // 검색하기 전까지 값을 저장해두기 위한 viewModel이다. searchRoute.kt에 저장되어있다.
        val viewModel = ViewModelProvider(this)[searchRouteViewModel::class.java]

        // 출발지점을 작성하는 textView, 출발지점 작성 후 전송하기위한 button
        val departureTextView = findViewById<EditText>(R.id.departure_textView)
        val searchButton = findViewById<ImageButton>(R.id.search_button)

        // 버튼 클릭 리스너
        searchButton.setOnClickListener{
            SearchRoute(departureTextView.text.toString())
        }

        // 애매
        viewModel.departureText.observe(this){
            viewModel.departureText.value = departureTextView.text.toString()
        }
    }
}