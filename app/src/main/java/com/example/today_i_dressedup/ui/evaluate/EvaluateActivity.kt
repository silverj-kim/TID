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

class EvaluateActivity : AppCompatActivity(), CardStackListener {

    private val cardStackView by lazy { findViewById<CardStackView>(R.id.cardStackView) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter() }
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
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        loadAllPosts()
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        //현재 포지션의 이전 포스트가 스와이프 된 것이기 때문에 -1 포지션의 id를 인자로 넘겨줌.
        val postId = adapter.getPosts()[manager.topPosition - 1].id
        when (direction) {
            Direction.Right -> evalueateViewModel.likePost(postId)

            Direction.Left -> evalueateViewModel.dislikePost(postId)
        }
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
        //만약 포스트가 엄청나게 많아지면 평가를 계속해서 못받는 포스트가 생길 수 있으므로 포스트에 마지막으로 평가받은 시간을 저장하는 timestamp를 만들고
        //마지막으로 평가 받은 시간이 가장 오래 지난 포스트 순으로 정렬해서 DB에서 가져오기.
        val disposable = evalueateViewModel
            .loadAllPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.setPosts(it)
            })

        disposables.add(disposable)
    }

    override fun onRestart() {
        super.onRestart()
        loadAllPosts()
    }
}
