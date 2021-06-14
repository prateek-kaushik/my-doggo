package com.prateek.dogengine

class UseCaseHandler {
    companion object {
        private var INSTANCE: UseCaseHandler? = null

        @Synchronized
        fun getInstance(): UseCaseHandler {
            if (INSTANCE == null) {
                INSTANCE = UseCaseHandler()
            }
            return INSTANCE!!
        }
    }

    fun <T : UseCase.RequestData, R : UseCase.ResponseData>
            execute(
        useCase: UseCase<T, R>,
        requestData: T
    ): R {
        useCase.mRequestData = requestData

        return useCase.execute(requestData)
    }
}