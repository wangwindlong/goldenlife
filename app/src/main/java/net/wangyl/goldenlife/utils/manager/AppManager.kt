package net.wangyl.goldenlife.utils.manager

class AppManager private constructor() {
    companion object {
        private var instance: AppManager? = null
            get() {
                if (field == null) {
                    field = AppManager()
                }
                return field
            }

        @Synchronized
        fun get(): AppManager{
            return instance!!
        }
    }
}