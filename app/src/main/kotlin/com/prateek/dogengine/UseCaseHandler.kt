package com.prateek.dogengine

import android.os.Handler
import android.os.Looper

class UseCaseHandler : Handler(Looper.getMainLooper()) {

    fun <T : UseCase.RequestData, R : UseCase.ResponseData>
            execute(useCase: UseCase<T, R>, requestData: T, callback: UseCase.UseCaseCallback<R>) {

        useCase.mUseCaseCallback = UiCallback(callback, this)

        useCase.execute(requestData)
    }

    private class UiCallback<Q : UseCase.ResponseData>(
        val callback: UseCase.UseCaseCallback<Q>,
        val handler: Handler
    ) :
        UseCase.UseCaseCallback<Q> {
        override fun onSuccess(response: Q) {
            handler.post {
                callback.onSuccess(response)
            }
        }

        override fun onError(t: Throwable) {
            handler.post {
                callback.onError(t)
            }
        }

    }
}