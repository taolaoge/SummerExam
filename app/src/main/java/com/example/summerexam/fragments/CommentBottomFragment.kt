package com.example.summerexam.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.adapters.CommentRvAdapter
import com.example.summerexam.adapters.OnlyTextRvAdapter
import com.example.summerexam.viewmodel.CommentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
class CommentBottomFragment :BottomSheetDialogFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[CommentViewModel::class.java] }
    private lateinit var mRvComment:RecyclerView
    private lateinit var mTvCommentCount:TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE,R.style.BottomDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_fragment,container,false)
    }

    /**
     * 解决BottomSheetDialogFragment闪退的问题
     * 原理暂时不清楚，自定义view太拉跨了
     */
    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet!!.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT  //自定义高度
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRvComment = view.findViewById(R.id.rv_comment)
        mTvCommentCount = view.findViewById(R.id.tv_comment_count)
        val bundle = arguments
        viewModel.id = bundle?.getInt("jokeId")?:0
        if (viewModel.id != 0) viewModel.getCommentList(){
            mTvCommentCount.text = "评论数量 ${viewModel.count}"
            mRvComment.run {
                layoutManager = LinearLayoutManager(context)
                adapter = CommentRvAdapter(viewModel.newData,::clickLike)
                overScrollMode = View.OVER_SCROLL_NEVER
            }
        }
    }

    private fun freshRecycleViewData() {
        val diffResult = DiffUtil.calculateDiff(
            CommentRvAdapter.DiffCallBack(
                viewModel.oldData, viewModel.newData
            ), true
        )
        diffResult.dispatchUpdatesTo(mRvComment.adapter!!)
    }

    private fun clickLike(id:String, status:Boolean, position:Int){
        viewModel.likeComment(id,status){
            if (it) viewModel.newData[position].run {
                if (status) {
                    isLike = status
                    likeNum += 1
                    freshRecycleViewData()
                }else{
                    isLike = status
                    likeNum -= 1
                    freshRecycleViewData()
                }
            }
        }
    }
}