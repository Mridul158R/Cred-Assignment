package com.example.credassignment.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credassignment.models.ApiResponse
import com.example.credassignment.models.Item
import com.example.credassignment.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class ItemViewModel : ViewModel(){
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _isLoading = MutableStateFlow(true) // Use StateFlow for loading state
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchItems() {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            ApiService.RetrofitInstance.api.getItems().enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    _isLoading.value = false // Stop loading
                    if (response.isSuccessful) {
                        _items.value = response.body()?.items ?: emptyList()
                        var k = _items.value
                        Log.d("max price", "${ k[0].open_state.body.card.max_range }")
                    } else {
                        _items.value = emptyList()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    _isLoading.value = false // Stop loading
                    _items.value = emptyList()
                }
            })
        }
    }
}