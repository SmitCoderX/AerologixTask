package com.smitcoderx.task.aerologixtask.UI

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.UI.Adapter.MainItemAdapter
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication
import com.smitcoderx.task.aerologixtask.Utils.Constants.TAG
import com.smitcoderx.task.aerologixtask.Utils.Resources
import com.smitcoderx.task.aerologixtask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelProviderFactory(
            application as AerologixApplication,
            (application as AerologixApplication).repository
        )
    }
    private lateinit var itemAdapter: MainItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemAdapter = MainItemAdapter()

        binding.rvItemList.apply {
            setHasFixedSize(false)
            adapter = itemAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.handle().observe(this) {
                handleUI(it)
            }
            binding.swipeRefresh.isRefreshing = false
        }

        loadData(itemAdapter)

    }

    private fun loadData(itemAdapter: MainItemAdapter) {
        mainViewModel.data.observe(this) { result ->
            handleUI(result)
        }
    }

    private fun handleUI(result: Resources<List<Data>>) {
        binding.apply {
            itemAdapter.differ.submitList(result.data)

            progress.isVisible = result is Resources.Loading && result.data.isNullOrEmpty()
//                textViewError.isVisible = result is Resources.Error && result.data.isNullOrEmpty()
            if (result is Resources.Error && result.data.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, result.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                Log.d(TAG, "onCreate: ${result.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData(itemAdapter)
    }

}