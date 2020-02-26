package com.example.today_i_dressedup.ui.evaluate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Spot
import com.example.today_i_dressedup.data.repository.UserRepository
import com.example.today_i_dressedup.databinding.ActivityEvaluateBinding
import com.example.today_i_dressedup.ui.myPage.MyPageActivity
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_evaluate.*

class EvaluateActivity : AppCompatActivity() {

    private val cardStackView by lazy { findViewById<CardStackView>(R.id.cardStackView) }
    private val manager by lazy { CardStackLayoutManager(this) }
    private val adapter by lazy { CardStackAdapter(createSpots()) }
    private lateinit var iv_addPhoto: ImageView
    private lateinit var iv_myPage: ImageView

    private lateinit var evalueateViewModel: EvalueateViewModel
    private lateinit var factory: EvaluateViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluate)
        initialize()
        setupButton()
    }

    private fun initialize() {
        factory = EvaluateViewModelFactory(UserRepository.getInstance())
        evalueateViewModel = ViewModelProviders.of(this, factory).get(EvalueateViewModel::class.java)
        val binding: ActivityEvaluateBinding = DataBindingUtil.setContentView(this, R.layout.activity_evaluate)
        binding.viewmodel = evalueateViewModel
        iv_addPhoto = evaluateActivity_iv_addPhoto
        iv_myPage = evaluateActivity_iv_myPage
        iv_myPage.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        manager.setStackFrom(StackFrom.Top)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        adapter.setSpots(createSpots())
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setupButton() {
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }


    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(
            Spot(
                name = "Yasaka Shrine",
                city = "Kyoto",
                url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Fushimi Inari Shrine",
                city = "Kyoto",
                url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Bamboo Forest",
                city = "Kyoto",
                url = "https://source.unsplash.com/buF62ewDLcQ/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Brooklyn Bridge",
                city = "New York",
                url = "https://source.unsplash.com/THozNzxEP3g/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Empire State Building",
                city = "New York",
                url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "The statue of Liberty",
                city = "New York",
                url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Louvre Museum",
                city = "Paris",
                url = "https://source.unsplash.com/LrMWHKqilUw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Eiffel Tower",
                city = "Paris",
                url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Big Ben",
                city = "London",
                url = "https://source.unsplash.com/CdVAUADdqEc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Great Wall of China",
                city = "China",
                url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
            )
        )
        return spots
    }

}
