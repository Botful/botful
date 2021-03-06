package org.sertain.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.sertain.coroutines.delayForever

fun fire(event: Event) = Events.fire(event)

inline fun <reified E : Event> CoroutineScope.subscribe(sub: Sub<E>): Job {
    return launch {
        try {
            Events.subscribe(sub)
            delayForever()
        } finally {
            Events.remove(sub)
        }
    }
}

inline fun <reified E : Event> CoroutineScope.subscribe(noinline action: suspend CoroutineScope.(E) -> Unit): Job {
    var sub: Sub<E>? = null
    return launch {
        try {
            sub = Events.subscribe(action)
            delayForever()
        } finally {
            Events.remove(sub!!)
        }
    }
}

inline fun <reified E : Event.Targeted<*>> CoroutineScope.subscribe(target: Any?, noinline action: suspend CoroutineScope.(E) -> Unit): Job {
    var sub: Sub<E>? = null
    return launch {
        try {
            sub = Events.subscribe(target, action)
            delayForever()
        } finally {
            Events.remove(sub!!)
        }
    }
}

inline fun <reified E1 : Event, reified E2 : Event> CoroutineScope.between(noinline action: suspend CoroutineScope.(E1) -> Unit): Job {
    var sub: Sub<E1>? = null
    return launch {
        try {
            sub = Events.between<E1, E2>(action)
            delayForever()
        } finally {
            Events.remove(sub!!)
        }
    }
}

inline fun <reified E1 : Event.Targeted<*>, reified E2 : Event.Targeted<*>> CoroutineScope.between(target: Any?, noinline action: suspend CoroutineScope.(E1) -> Unit): Job {
    var sub: Sub<E1>? = null
    return launch {
        try {
            sub = Events.between<E1, E2>(target, action)
            delayForever()
        } finally {
            Events.remove(sub!!)
        }
    }
}
