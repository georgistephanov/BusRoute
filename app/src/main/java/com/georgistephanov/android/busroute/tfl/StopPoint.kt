package com.georgistephanov.android.busroute.tfl

data class StopPoint(val query : String, val total : Int, val matches : List<Match>)

data class Match(val id: String, val icsId : Int, val name : String, val modes : List<String>)

data class BusStop(val indicator : String, val commonName : String)