package com.ravimhzn.androidmockserver.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ravimhzn.androidmockserver.utils.CollectionUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

//Todo
abstract class BaseActivity<ChildViewModel : ViewModel> : ComponentActivity() {

    protected val tag = javaClass.simpleName

    protected lateinit var viewModel: ChildViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
    }

    open fun getViewModelClass(): Class<ChildViewModel> {
        return genericType(ViewModel::class.java) as Class<ChildViewModel>
    }

    open fun genericType(classType: Class<*>): Type? {
        var clazz: Class<*>? = javaClass
        do {
            while (clazz != null && clazz.genericSuperclass !is ParameterizedType) {
                clazz = clazz.superclass
            }
            if (clazz != null) {
                val arguments = (clazz.genericSuperclass as ParameterizedType).actualTypeArguments
                val type: Type = CollectionUtils.first(
                    listOf(*arguments)
                ) { argument ->
                    argument is Class<*> && classType.isAssignableFrom(
                        argument
                    )
                }
                return type
            }
        } while (clazz != null && clazz.superclass.also { clazz = it } != null)
        return null
    }
}