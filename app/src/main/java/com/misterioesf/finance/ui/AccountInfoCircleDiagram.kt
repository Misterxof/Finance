package com.misterioesf.finance.ui

import android.content.Context
import android.util.AttributeSet
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import javax.inject.Inject

@AndroidEntryPoint
@WithFragmentBindings
class AccountInfoCircleDiagram @Inject constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : CircleDiagramView(context, attributeSet){

}