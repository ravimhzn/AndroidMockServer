package com.ravimhzn.androidmockserver.services

import com.ravimhzn.androidmockserver.model.HttpMethod
import com.ravimhzn.androidmockserver.model.MockResponse
import com.ravimhzn.starappmockserver.android.R

class MockConfig {

    private val mockResponse = ArrayList<MockResponse>()

    init {

        addResponse(
            HttpMethod.GET,
            "Random Api 1",
            "random/api/1",
            R.raw.content_events,
            R.raw.empty_ok,
            R.raw.error_response
        )

        addResponse(
            HttpMethod.GET,
            "Random Api 2",
            "random/api/2",
            R.raw.content_events
        )

        addResponse(
            HttpMethod.GET,
            "Random Api 3",
            "random/api/3",
            R.raw.content_events
        )

        addResponse(
            HttpMethod.POST,
            "Random Api 4",
            "random/api/4",
            R.raw.content_events
        )

    }

    private fun addResponse(
        httpMethod: HttpMethod,
        name: String,
        path: String,
        vararg response: Int,
        default: Int = 0
    ) {
        mockResponse.add(
            MockResponse(
                name,
                path,
                httpMethod
            ).addResponses(
                resource = response, default
            )
        )
    }

    fun getResponses(): List<MockResponse> {
        return mockResponse
    }
}