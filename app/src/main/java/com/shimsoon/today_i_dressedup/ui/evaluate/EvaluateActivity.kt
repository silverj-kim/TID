package com.shimsoon.today_i_dressedup.ui.evaluate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.gms.ads.AdView
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.Status
import com.shimsoon.today_i_dressedup.databinding.ActivityEvaluateBinding
import com.shimsoon.today_i_dressedup.ui.myPage.MyPageActivity
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_evaluate.*

class EvaluateActivity : AppCompatActivity(), CardStackListener {

    private val cardStackView by lazy { findViewById<CardStackView>(R.id.cardStackView) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter() }

    private lateinit var iv_myPage: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var adView: AdView

    private lateinit var evaluateViewModel: EvaluateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluate)
        initialize()
        setupButton()
    }

    private fun initialize() {
        evaluateViewModel = ViewModelProviders.of(this).get(EvaluateViewModel::class.java)
        val binding: ActivityEvaluateBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_evaluate)
        binding.viewmodel = evaluateViewModel
        evaluateViewModel.updateUserToken()

        progressBar = evaluateActivity_progressBar
        iv_myPage = evaluateActivity_iv_myPage
        iv_myPage.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

        manager.setStackFrom(StackFrom.None)
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
        loadAllPosts()

        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

//        adView = evaluateActivity_adView
//        MobileAds.initialize(this) {
//            val adRequest = AdRequest.Builder().build()
//            adView.loadAd(adRequest)
//        }
    }

    override fun onCardSwiped(direction: Direction?) {
        //현재 포지션의 이전 포스트가 스와이프 된 것이기 때문에 -1 포지션의 id를 인자로 넘겨줌.
        val idx = manager.topPosition - 1
        val post = adapter.getPosts()[idx]

        when (direction) {
            Direction.Right -> {
                evaluateViewModel.likePost(post.id)
                evaluateViewModel.sendNotification(post.userId, post.id)
            }

            Direction.Left -> {
                evaluateViewModel.dislikePost(post.id)
                evaluateViewModel.sendNotification(post.userId, post.id)
            }
        }

//        if (idx == adapter.itemCount - 1) loadAllPosts()
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
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
        evaluateViewModel.loadAllPosts()
        evaluateViewModel.statusLiveData.observe(this, Observer {
            when (it) {
                Status.SUCCESS -> {
                    hideProgressBar()
                }
                Status.LOADING -> {
                    showProgressBar()
                }
                Status.FAILURE -> {
                    hideProgressBar()
                }
            }
        })

        evaluateViewModel.posts.observe(this, Observer {
            if (it.isEmpty()) {
                showNoItemTextView()
            } else {
                hideNoItemTextView()
                adapter.setPosts(it)
            }
        })
    }

    private fun showNoItemTextView() {
        evaluateActivity_tv_noItem.visibility = View.VISIBLE
    }

    private fun hideNoItemTextView() {
        evaluateActivity_tv_noItem.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onRestart() {
        super.onRestart()
        loadAllPosts()
        Log.d("EvaluateActivity:", "onRestart()")
    }
}
