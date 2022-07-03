package com.example.githubusers.ui.view

import android.content.Intent
import android.net.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.ActivityUserListBinding
import com.example.githubusers.ui.adapter.LoadStateFooterAdapter
import com.example.githubusers.ui.adapter.UserListAdapter
import com.example.githubusers.ui.viewmodel.UserListViewModel
import com.example.githubusers.utils.Utility
import com.example.githubusers.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@AndroidEntryPoint
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var userAdapter: UserListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()


        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                userAdapter.refresh()
                Toast.makeText(this@UserListActivity, "Refresh Data", Toast.LENGTH_SHORT).show()
            }

            // Network capabilities have changed for the network
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val unmetered =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                Toast.makeText(this@UserListActivity, "Offline", Toast.LENGTH_SHORT).show()
            }
        }

        (getSystemService(ConnectivityManager::class.java) as ConnectivityManager).requestNetwork(
            networkRequest,
            networkCallback
        )

    }


    private fun initView() {

        setupAdapter()
        observeData()

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == 200) {
                val id: Int = result.data?.getIntExtra("USER_ID", 0)!!
                val hasNote: Boolean = result.data?.getBooleanExtra("HAS_NOTES", false)!!
                viewModel.changeNoteStatus(id,hasNote)
                userAdapter.notifyDataSetChanged()
            }
        }

    private fun setupAdapter() {

        userAdapter =
            UserListAdapter(UserListAdapter.OnItemClickListener { user ->
                if (!user.isRead!!) user.isRead = true
                viewModel.updateOnClick(user)
                userAdapter.notifyDataSetChanged()
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("USER", user)
                startForResult.launch(intent)


            })

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter

            adapter = userAdapter.withLoadStateHeaderAndFooter(
                footer = LoadStateFooterAdapter {
                    userAdapter.retry()

                },
                header = LoadStateFooterAdapter {
                    userAdapter.retry()
                }
            )
        }
    }

    private fun observeData() {
        this.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserList().collect() { pagingData ->
                    userAdapter.submitData(pagingData)
                }
            }
        }

        this.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is UserListViewModel.Event.ShowErrorMessage ->
                        showError(event.error.localizedMessage ?: "")
                            .exhaustive
                }

            }
        }
    }



    private fun showError(error: String) {
        //show a snackBar
        Utility.displayErrorSnackBar(binding.root, error, this)
    }

    override fun onResume() {
        super.onResume()
        userAdapter.retry()
    }


}