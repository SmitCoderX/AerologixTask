package com.smitcoderx.task.aerologixtask.UI

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.UI.Adapter.MainItemAdapter
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication
import com.smitcoderx.task.aerologixtask.Utils.Constants.TAG
import com.smitcoderx.task.aerologixtask.Utils.Convertor
import com.smitcoderx.task.aerologixtask.Utils.Resources
import com.smitcoderx.task.aerologixtask.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelProviderFactory(
            application as AerologixApplication,
            (application as AerologixApplication).repository
        )
    }
    private val permission = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    private lateinit var itemAdapter: MainItemAdapter
    private val data = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission(100)

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

        binding.rvItemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.fabAdd.isShown) {
                    binding.fabAdd.visibility = View.GONE
                    binding.fabAdd.visibility = View.GONE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fabAdd.visibility = View.VISIBLE
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        binding.fabSave.setOnClickListener {
            storeFile("Employees")
        }

    }

    private fun loadData(itemAdapter: MainItemAdapter) {
        mainViewModel.data.observe(this) { result ->
            result.data?.let { data.addAll(it) }
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

    private fun checkPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission[0]
            ) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
                this,
                permission[1]
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this,
                permission.toTypedArray(), requestCode
            )
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i(TAG, "Permission: Granted")
        } else {
            Log.i(TAG, "Permission: Denied")
        }
    }

    private fun storeFile(name: String) {
        val item = Convertor().toDataJson(data)
        Log.d(TAG, "storeFile: $item")
        val resolver = applicationContext.contentResolver
        val values = ContentValues()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            uri?.let { it ->
                resolver.openOutputStream(it).use {
                    // Write file
                    it?.write(item.toByteArray(Charset.defaultCharset()))
                    it?.close()
                }
            }
        } else {
            val rootPath = "/storage/emulated/0/Download/"
            val myFile = File(rootPath, name)
            val outputStream: FileOutputStream
            try {
                if (myFile.createNewFile()) {
                    outputStream = FileOutputStream(myFile, true)
                    outputStream.write(item.toByteArray())
                    outputStream.flush()
                    outputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}