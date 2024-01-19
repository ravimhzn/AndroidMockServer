package com.ravimhzn.androidmockserver.viewmodels

import androidx.lifecycle.ViewModel
import com.ravimhzn.androidmockserver.model.MockResponse
import com.ravimhzn.androidmockserver.services.MockConfig
import com.ravimhzn.androidmockserver.services.module
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class MockDataViewModel : ViewModel() {

    private lateinit var server: NettyApplicationEngine
    lateinit var mockResponse: MockResponse

    fun startNettyServer() {
        CoroutineScope(Dispatchers.IO).launch {
            val logger = LoggerFactory.getLogger("Main")
            try {
                server =
                    embeddedServer(
                        Netty,
                        port = 8080,
                        host = "0.0.0.0",
                        module = Application::module
                    )
                logger.info("Starting server...")
                server.start(wait = true)
                logger.info("Server started successfully.")
            } catch (e: Exception) {
                logger.error("Failed to start the server", e)
            }
        }


    }

    fun stopNettyServer() {
        CoroutineScope(Dispatchers.IO).launch {
            server.stop(0, 0)
        }
    }

    override fun onCleared() {
        server.stop(0, 0)
        super.onCleared()
    }

    fun getMockResponses(): List<MockResponse> {
        val mockConfig = MockConfig()
        return mockConfig.getResponses()
    }

    fun setDataToMockResponse(response: MockResponse){
        this.mockResponse = response
    }

}