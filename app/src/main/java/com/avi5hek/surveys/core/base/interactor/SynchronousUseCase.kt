package com.avi5hek.surveys.core.base.interactor

interface SynchronousUseCase<Result, in Params> {

  fun execute(params: Params? = null): Result
}
