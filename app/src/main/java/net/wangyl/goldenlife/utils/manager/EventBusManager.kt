package net.wangyl.goldenlife.utils.manager

import net.wangyl.goldenlife.utils.CommonUtils.Companion.DEPENDENCY_EVENTBUS
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.Method

class EventBusManager private constructor() {
    companion object {
        private var instance: EventBusManager? = null
            get() {
                if (field == null) {
                    field = EventBusManager()
                }
                return field
            }

        @Synchronized
        fun get(): EventBusManager {
            return instance!!
        }
    }

    fun register(subscriber: Any?) {
//        if (DEPENDENCY_ANDROID_EVENTBUS) {
//            org.simple.eventbus.EventBus.getDefault().register(subscriber)
//        }
        if (DEPENDENCY_EVENTBUS) {
            if (haveAnnotation(subscriber!!)) {
                EventBus.getDefault().register(subscriber)
            }
        }
    }

    /**
     * @param subscriber 订阅者
     */
    fun unregister(subscriber: Any?) {
//        if (DEPENDENCY_ANDROID_EVENTBUS) {
//            org.simple.eventbus.EventBus.getDefault().unregister(subscriber)
//        }
        if (DEPENDENCY_EVENTBUS) {
            if (haveAnnotation(subscriber!!)) {
                EventBus.getDefault().unregister(subscriber)
            }
        }
    }

    /**
     * 发送事件, 如果您在项目中同时依赖了两个 EventBus, 请自己使用想使用的 EventBus 的 Api 发送事件
     *
     * @param event 事件
     */
    fun post(event: Any?) {
//        if (DEPENDENCY_ANDROID_EVENTBUS) {
//            org.simple.eventbus.EventBus.getDefault().post(event)
//        } else
            if (DEPENDENCY_EVENTBUS) {
            EventBus.getDefault().post(event)
        }
    }

    /**
     * 发送黏性事件, 如果您在项目中同时依赖了两个 EventBus, 请自己使用想使用的 EventBus 的 Api 发送黏性事件
     *
     * @param event 事件
     */
    fun postSticky(event: Any?) {
//        if (DEPENDENCY_ANDROID_EVENTBUS) {
//            org.simple.eventbus.EventBus.getDefault().postSticky(event)
//        } else
        if (DEPENDENCY_EVENTBUS) {
            EventBus.getDefault().postSticky(event)
        }
    }

    /**
     * 注销黏性事件, 如果您在项目中同时依赖了两个 EventBus, 请自己使用想使用的 EventBus 的 Api 注销黏性事件
     *
     * @param eventType
     * @param <T>
     * @return
    </T> */
    fun <T> removeStickyEvent(eventType: Class<T>?): T? {
//        if (DEPENDENCY_ANDROID_EVENTBUS) {
//            org.simple.eventbus.EventBus.getDefault().removeStickyEvent(eventType)
//            return null
//        } else
            if (DEPENDENCY_EVENTBUS) {
            return EventBus.getDefault().removeStickyEvent(eventType)
        }
        return null
    }


    /**
     * [org.greenrobot.eventbus.EventBus] 要求注册之前, 订阅者必须含有一个或以上声明 [org.greenrobot.eventbus.Subscribe]
     * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     *
     * @param subscriber 订阅者
     * @return 返回 `true` 则表示含有 [org.greenrobot.eventbus.Subscribe] 注解, `false` 为不含有
     */
    private fun haveAnnotation(subscriber: Any): Boolean {
        var skipSuperClasses = false
        var clazz: Class<*>? = subscriber.javaClass
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemCalss(clazz.name) && !skipSuperClasses) {
            var allMethods: Array<Method>
            allMethods = try {
                clazz.declaredMethods
            } catch (th: Throwable) {
                try {
                    clazz.methods
                } catch (th2: Throwable) {
                    continue
                } finally {
                    skipSuperClasses = true
                }
            }
            for (method in allMethods) {
                val parameterTypes = method.parameterTypes
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(Subscribe::class.java) && parameterTypes.size == 1) {
                    return true
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.superclass
        }
        return false
    }

    private fun isSystemCalss(name: String): Boolean {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")
    }
}