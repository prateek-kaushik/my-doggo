package com.prateek.dogengine

abstract class UseCase<P : UseCase.RequestData, Q : UseCase.ResponseData> {

    lateinit var mRequestData: P

    abstract fun execute(requestData: P): Q

    abstract class RequestData

    abstract class ResponseData
}
