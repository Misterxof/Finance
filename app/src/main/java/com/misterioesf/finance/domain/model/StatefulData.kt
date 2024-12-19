package com.misterioesf.finance.domain.model

sealed class StatefulData <out Data> {

    abstract val data: Data?

    data class LoadingState<out Data>(override val data: Data?) : StatefulData<Data>()
    data class SaveState<out Data>(override val data: Data?) : StatefulData<Data>()
    data class SuccessState<out Data>(override val data: Data) : StatefulData<Data>()
//    data class ErrorState<out Error>(val error: Error) : StatefulData<Nothing>() {
//        override val data: Nothing?
//            get() = null
//    }
}