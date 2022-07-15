package com.example.summerexam.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.summerexam.R
import com.example.summerexam.viewmodel.LoginViewModel
import com.ndhzs.lib.common.extensions.appContext
import com.ndhzs.lib.common.extensions.getSp
import com.ndhzs.lib.common.ui.BaseActivity

class LoginActivity : BaseActivity(), View.OnClickListener {
    private val mEdPhone by R.id.ed_login_phone.view<EditText>()
    private val mEdCode by R.id.ed_login_code.view<EditText>()
    private val mBtnGetCode by R.id.btn_login_get_code.view<Button>()
    private val mBtnLogin by R.id.btn_login_login.view<Button>()
    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initClick()
    }

    private fun initClick() {
        mBtnGetCode.setOnClickListener(this@LoginActivity)
        mBtnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v){
            mBtnGetCode -> {
                viewModel.getCode(mEdPhone.text.toString())
            }
            mBtnLogin -> {
                viewModel.login(mEdCode.text.toString(),mEdPhone.text.toString()){
                    if (appContext.getSp("token").getString("token","123") != "123") finish()
                }
            }
        }
    }
}