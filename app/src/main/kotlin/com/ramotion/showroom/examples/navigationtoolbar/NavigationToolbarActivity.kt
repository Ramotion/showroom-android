package com.ramotion.showroom.examples.navigationtoolbar

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.ramotion.showroom.R
import com.ramotion.navigationtoolbar.HeaderLayout
import com.ramotion.navigationtoolbar.HeaderLayoutManager
import com.ramotion.navigationtoolbar.NavigationToolBarLayout
import com.ramotion.navigationtoolbar.SimpleSnapHelper
import com.ramotion.showroom.examples.navigationtoolbar.header.HeaderAdapter
import com.ramotion.showroom.examples.navigationtoolbar.header.HeaderItemTransformer
import com.ramotion.showroom.examples.navigationtoolbar.pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.nt_activity_main.*
import kotlin.math.ceil
import kotlin.math.max


class NavigationToolbarActivity : AppCompatActivity() {
    private val itemCount = 40
    private val dataSet = ExampleDataSet()

    private var isExpanded = true
    private var prevAnchorPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nt_activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val header = findViewById<NavigationToolBarLayout>(R.id.navigation_toolbar_layout)
        val viewPager = findViewById<ViewPager>(R.id.pager)

        initActionBar()
        initViewPager(header, viewPager)
        initHeader(header, viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nt_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initActionBar() {
        val toolbar = navigation_toolbar_layout.toolBar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViewPager(header: NavigationToolBarLayout, viewPager: ViewPager) {
        viewPager.adapter = ViewPagerAdapter(itemCount, dataSet.viewPagerDataSet)
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                header.smoothScrollToPosition(position)
            }
        })
    }

    private fun initHeader(header: NavigationToolBarLayout, viewPager: ViewPager) {
        val titleLeftOffset = resources.getDimensionPixelSize(R.dimen.nt_title_left_offset)
        val lineRightOffset = resources.getDimensionPixelSize(R.dimen.nt_line_right_offset)
        val lineBottomOffset = resources.getDimensionPixelSize(R.dimen.nt_line_bottom_offset)
        val lineTitleOffset = resources.getDimensionPixelSize(R.dimen.nt_line_title_offset)

        val headerOverlay = findViewById<FrameLayout>(R.id.header_overlay)
        header.setItemTransformer(HeaderItemTransformer(headerOverlay,
                titleLeftOffset, lineRightOffset, lineBottomOffset, lineTitleOffset))
        header.setAdapter(HeaderAdapter(itemCount, dataSet.headerDataSet, headerOverlay))

        header.addItemChangeListener(object : HeaderLayoutManager.ItemChangeListener {
            override fun onItemChangeStarted(position: Int) {
                prevAnchorPosition = position
            }

            override fun onItemChanged(position: Int) {
                viewPager.currentItem = position
            }
        })

        header.addItemClickListener(object : HeaderLayoutManager.ItemClickListener {
            override fun onItemClicked(viewHolder: HeaderLayout.ViewHolder) {
                viewPager.currentItem = viewHolder.position
            }
        })

        SimpleSnapHelper().attach(header)
        initDrawerArrow(header)
        initHeaderDecorator(header)
    }

    private fun initDrawerArrow(header: NavigationToolBarLayout) {
        val drawerArrow = DrawerArrowDrawable(this)
        drawerArrow.color = ContextCompat.getColor(this, android.R.color.white)
        drawerArrow.progress = 1f

        header.addHeaderChangeStateListener(object : HeaderLayoutManager.HeaderChangeStateListener() {
            private fun changeIcon(progress: Float) {
                ObjectAnimator.ofFloat(drawerArrow, "progress", progress).start()
                isExpanded = progress == 1f
                if (isExpanded) {
                    prevAnchorPosition = header.getAnchorPos()
                }
            }

            override fun onMiddle() = changeIcon(0f)
            override fun onExpanded() = changeIcon(1f)
        })

        val toolbar = header.toolBar
        toolbar.navigationIcon = drawerArrow
        toolbar.setNavigationOnClickListener {
            if (!isExpanded) {
                return@setNavigationOnClickListener
            }
            val anchorPos = header.getAnchorPos()
            if (anchorPos == HeaderLayout.INVALID_POSITION) {
                return@setNavigationOnClickListener
            }

            if (anchorPos == prevAnchorPosition) {
                header.collapse()
            } else {
                header.smoothScrollToPosition(prevAnchorPosition)
            }
        }
    }

    private fun initHeaderDecorator(header: NavigationToolBarLayout) {
        val decorator = object :
                HeaderLayoutManager.ItemDecoration,
                HeaderLayoutManager.HeaderChangeListener {

            private val dp5 = resources.getDimensionPixelSize(R.dimen.nt_decor_bottom)

            private var bottomOffset = dp5

            override fun onHeaderChanged(lm: HeaderLayoutManager, header: HeaderLayout, headerBottom: Int) {
                val ratio = max(0f, headerBottom.toFloat() / header.height - 0.5f) / 0.5f
                bottomOffset = ceil(dp5 * ratio).toInt()
            }

            override fun getItemOffsets(outRect: Rect, viewHolder: HeaderLayout.ViewHolder) {
                outRect.bottom = bottomOffset
            }
        }

        header.addItemDecoration(decorator)
        header.addHeaderChangeListener(decorator)
    }
}