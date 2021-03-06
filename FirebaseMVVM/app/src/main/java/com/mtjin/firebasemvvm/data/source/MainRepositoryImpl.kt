package com.mtjin.firebasemvvm.data.source

import com.mtjin.firebasemvvm.data.Message
import com.mtjin.firebasemvvm.data.source.local.MainLocalDataSource
import com.mtjin.firebasemvvm.data.source.remote.MainRemoteDataSource
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    private val mainLocalDataSource: MainLocalDataSource
) : MainRepository {
    override fun requestMessages(): Flowable<Message> {
        return mainRemoteDataSource.requestMessages().observeOn(Schedulers.io()).flatMap {
            mainLocalDataSource.insertMessage(it).andThen(Flowable.just(it))
        }
    }

    override fun requestLocalMessages() = mainLocalDataSource.getMessages()

    override fun saveLocalMessage(message: Message) {
        mainLocalDataSource.insertMessage(message)
    }

    override fun sendMessage(message: Message) {
        return mainRemoteDataSource.sendMessage(message)
    }
}