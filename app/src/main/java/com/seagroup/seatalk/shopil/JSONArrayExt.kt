package com.seagroup.seatalk.shopil

import org.json.JSONArray
import org.json.JSONException

@Throws(JSONException::class)
fun JSONArray.toList(key: String): List<String> {
    val result = ArrayList<String>()
    for (i in 0 until length()) {
        result.add(element = getJSONObject(i).getString(key))
    }
    return result
}
