package com.example.news.component.user

class TitleData {
    var title = 0
    var titleString: String? = null

    constructor(title: Int) {
        this.title = title
    }

    constructor(titleString: String?) {
        this.titleString = titleString
    }
}