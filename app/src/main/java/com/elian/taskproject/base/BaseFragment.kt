package com.elian.taskproject.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment()
{
    internal abstract val presenter: IBasePresenter

    override fun onDestroy()
    {
        super.onDestroy()

        presenter.onDestroy()
    }
}