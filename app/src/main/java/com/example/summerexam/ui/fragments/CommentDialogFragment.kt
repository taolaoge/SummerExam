package com.example.summerexam.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.example.summerexam.R
import com.example.summerexam.viewmodel.CommentDiaLogViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/18
 */
class CommentDialogFragment : BottomSheetDialogFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[CommentDiaLogViewModel::class.java] }
    private lateinit var mEdComment: EditText
    private lateinit var mImgPostComment: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(true) //点击外部消失
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEdComment = view.findViewById(R.id.ed_comment)
        mImgPostComment = view.findViewById(R.id.img_post_comment)
        val bundle = arguments
        viewModel.jokeId = bundle?.getInt("jokeId") ?: 0
        mImgPostComment.setOnClickListener {
            clickPostComment()
        }
    }

    private fun clickPostComment() {
        val content = mEdComment.text.toString()
        viewModel.commentJoke(content, viewModel.jokeId.toString()) {
            dismiss()
        }
    }

    /**
     * 解决BottomSheetDialogFragment需要手动滑动一截的问题
     * 原理暂时不清楚，自定义view太拉跨了
     */
    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet =
            dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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
}