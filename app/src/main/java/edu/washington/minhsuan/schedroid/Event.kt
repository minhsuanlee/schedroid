package edu.washington.minhsuan.schedroid

class Event {
    var username: String = ""
    var title: String = ""
    var date: String = ""
    var time: String = ""
    var descript: String = ""
    var longitude: Float = 0f
    var latitude: Float = 0f
    var daily: Int = 0



    constructor()

    constructor(username: String, title: String, date: String, time: String, descript: String, longitude: Float,
                latitude: Float, daily: Int) {
        this.username = username
        this.title = title
        this.date = date
        this.time = time
        this.descript = descript
        this.longitude = longitude
        this.latitude = latitude
        this.daily = daily
    }

    override fun toString(): String {
        return "username: $username, title: $title, date: $date, time: $time, descript: $descript, " +
                "longitude: $longitude, latitude: $latitude, daily: $daily"
    }

}