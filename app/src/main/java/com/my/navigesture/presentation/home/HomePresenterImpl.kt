package com.my.navigesture.presentation.home

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import com.eightbitlab.rxbus.Bus
import com.my.navigesture.data.ColorData
import com.my.navigesture.data.ScaleData
import com.my.navigesture.data.services.naviService.NaviService
import com.my.navigesture.utils.Utils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class HomePresenterImpl: HomePresenter.Presenter{


    val view: HomePresenter.View?
    val publishSubject = PublishSubject.create<Int>()
    val compositSubscription: CompositeDisposable = CompositeDisposable()
    val context: Context?

    constructor(context: Context, view: HomePresenter.View){
        this.view = view
        this.context = context

        initScaleChanged()

        this.view?.showScale(Utils.getDetaulScaleValue(this.context))

    }


    override fun checkEnableAccessibilityService() {
        val isEnable = Utils.isAccessibilitySettingsOn(context!!.applicationContext, NaviService::class.java!!.getCanonicalName())
        if(isEnable){
            view?.hideAccessibilityView()
        }else{
            view?.showAccessibilityView()
        }
    }

    override fun scaleChanged(value: Int) {
        publishSubject.onNext(value)
    }

    override fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(context)) {
            view?.openOverlaySettings()
        }else{
            checkEnableAccessibilityService()
        }
    }

    override fun colorChanged(color: Int) {
        Utils.saveColorToLocal(context!!, color)
        Bus.send(ColorData(color))
    }

    override fun release() {
        compositSubscription?.clear()
    }

    fun initScaleChanged(){
        //Notify scale changed
        compositSubscription.add(publishSubject.flatMap { getScaleObservable(it) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun getScaleObservable(value: Int): Observable<Int> {

        return Observable.just(value)
                .doOnNext { Bus.send(ScaleData(it)) }
                .doOnNext { Utils.saveScaleToLocal(context!!, it) }

    }


}