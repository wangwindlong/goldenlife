package net.wangyl.eventbus_flow

import android.app.Application

object EventBusInitializer {

    lateinit var application: Application

    fun init(application: Application) {
        EventBusInitializer.application = application
    }

}