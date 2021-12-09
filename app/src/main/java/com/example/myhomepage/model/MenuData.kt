package com.example.myhomepage.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class MenuData : BaseObservable() {

    var title: String? = null
    var id: String? = null
    var url: String? = null
    var type: String? = null
    var handle: String? = null
    var appversion: String? = null
    var copyright: String? = null
    @get:Bindable
    var username: String? = null
        set(username) {
            field = username
            notifyPropertyChanged(141)
        }
    @get:Bindable
    var tag: String? = null
        set(tag) {
            field = tag
            notifyPropertyChanged(132)
        }
    @get:Bindable
    var visible: Int = 0
        set(visible) {
            field = visible
            notifyPropertyChanged(146)
        }
    @get:Bindable
    var previewvislible: Int = 0
        set(previewvislible) {
            field = previewvislible
            notifyPropertyChanged(107)
        }
}
