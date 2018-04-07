package hit.sun.scarlet.hitwashingwebview

import java.util.concurrent.ThreadFactory

class ThreadFactoryBuilder(private val name: String) : ThreadFactory {

    private val counter = 1
    override fun newThread(runnable: Runnable): Thread {
        val thread = Thread(runnable, name)
        thread.name = "ThreadFactoryBuilder_${name}_${counter}"
        return thread
    }
}
