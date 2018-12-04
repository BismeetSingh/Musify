
package com.app.bissudroid.musify.events

import rx.Observable
import rx.functions.Action1
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription


class RxBus {

    private val bus = SerializedSubject(PublishSubject.create<Any>())

    private val subscriptionMap = HashMap<Any, CompositeSubscription>()

    fun post(event: Any) {
        if (this.bus.hasObservers()) {
            this.bus.onNext(event)
        }
    }

    private fun <T> observe(eventClass: Class<T>): Observable<T> {
        return this.bus
                .filter { o -> o != null }
                .filter({ eventClass.isInstance(it) })
                .cast(eventClass)
    }

    fun <T> subscribe(eventClass: Class<T>, subscriptionAnchor: Any, action: Action1<T>) {
        val subscription = observe(eventClass).subscribe(action)
        getCompositeSubscription(subscriptionAnchor).add(subscription)
    }

    private fun getCompositeSubscription(subscriptionAnchor: Any): CompositeSubscription {
        var compositeSubscription = subscriptionMap[subscriptionAnchor]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
            subscriptionMap[subscriptionAnchor] = compositeSubscription
        }
        return compositeSubscription
    }

    fun cleanup(subscriptionAnchor: Any) {
        val compositeSubscription = subscriptionMap.remove(subscriptionAnchor)
        compositeSubscription?.clear()
    }
}
