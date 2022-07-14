package com.ndhzs.lib.common.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ndhzs.lib.common.ui.BaseBindFragment
import com.ndhzs.lib.common.utils.GenericityUtils

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
abstract class BaseVmBindFragment<VM : ViewModel, DB : ViewBinding> : BaseBindFragment<DB>() {
  
  @Suppress("UNCHECKED_CAST")
  protected val viewModel by lazy(LazyThreadSafetyMode.NONE) {
    val factory = getViewModelFactory()
    if (factory == null) {
      ViewModelProvider(this)[GenericityUtils.getGenericClassFromSuperClass(javaClass)] as VM
    } else {
      ViewModelProvider(this, factory)[GenericityUtils.getGenericClassFromSuperClass(javaClass)] as VM
    }
  }
  
  protected open fun getViewModelFactory(): ViewModelProvider.Factory? = null
}