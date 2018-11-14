package com.app.bissudroid.musify.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import timber.log.Timber

class RecylerViewEmptySupport : RecyclerView {
    private var mContext: Context? = null
    //The empty view which is shown when the data is empty
    private var emptyView: View? = null

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        //Setting the adapter data change listener
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                Timber.d("%s", adapter.itemCount.toString())
                //If data count is not 0 emptyView visibility is set to gone and recycler view is shown
                if (adapter.itemCount != 0) {
                    if (emptyView != null) {
                        emptyView!!.visibility = View.GONE
                        visibility = View.VISIBLE
                    }
                } else {
                    //If data count is 0 emptyView visibility is set to visible and recycler view
                    // visibility is set to gone
                    if (emptyView != null) {
                        visibility = View.GONE
                        emptyView!!.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    //Call this function to set the empty view
    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

}

