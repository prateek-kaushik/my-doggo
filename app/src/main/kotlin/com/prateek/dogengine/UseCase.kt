package com.prateek.dogengine

abstract class UseCase<P : UseCase.RequestData, Q : UseCase.ResponseData> {

    abstract class RequestData

    abstract class ResponseData

    interface UseCaseCallback<R> {
        fun onSuccess(response: R)

        fun onError(t: Throwable)
    }

    var mRequestData: P? = null

    var mUseCaseCallback: UseCaseCallback<Q>? = null

    abstract fun execute(requestData: P)
}