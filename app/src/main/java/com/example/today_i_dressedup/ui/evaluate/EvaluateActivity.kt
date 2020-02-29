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
import com.example.today_i_dressedup.data.repository.PostRepository
import com.example.today_i_dressedup.data.repository.UserRepository
import com.example.today_i_dressedup.databinding.ActivityEvaluateBinding
import com.example.today_i_dressedup.ui.myPage.MyPageActivity
import com.yuyakaido.android.cardstackview.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_evaluate.*

class EvaluateActivity : AppCompatActivity() {

    private val cardStackView by lazy { findViewById<CardStackView>(R.id.cardStackView) }
    private val manager by lazy { CardStackLayoutManager(this) }
    private lateinit var adapter: CardStackAdapter
    private lateinit var iv_addPhoto: ImageView
    private lateinit var iv_myPage: ImageView

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    private lateinit var evalueateViewModel: EvalueateViewModel
    private lateinit var factory: EvaluateViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluate)
        initialize()
        setupButton()
    }

    private fun initialize() {
        factory = EvaluateViewModelFactory(UserRepository.getInstance(), PostRepository.getInstance())
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
        adapter = CardStackAdapter()
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        loadAllPosts()
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


    private fun loadAllPosts() {
        val disposable = evalueateViewModel
            .loadAllPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.setSpots(it)
            })

        disposables.add(disposable)
    }

    override fun onRestart() {
        super.onRestart()
        loadAllPosts()
    }
}
