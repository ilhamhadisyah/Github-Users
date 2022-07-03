package com.example.githubusers.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import com.example.githubusers.R
import com.example.githubusers.databinding.ActivityProfileBinding
import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponseItem
import com.example.githubusers.network.Resource
import com.example.githubusers.ui.viewmodel.UserDetailViewModel
import com.example.githubusers.utils.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response

@DelicateCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel : UserDetailViewModel by viewModels()
    private lateinit var userData :UserResponseItem
    private lateinit var userDetailData : UserDetailResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        userData = intent.getParcelableExtra<UserResponseItem>("USER")!!
        binding.loading.visibility = View.VISIBLE
        binding.saveBtn.setOnClickListener(this)

        retrieveData()
    }

    private fun retrieveData(){
        viewModel.getDetailUser(userData.login!!).observe(this, Observer { resource->
            when(resource.status){
                Resource.Status.SUCCESS->{

                    binding.loading.visibility = View.GONE

                    binding.apply {
                        userDetailData = resource.data!!
                        userAvatar.loadUrl(userDetailData.avatarUrl!!)
                        toolbar.title = userDetailData.name
                        userName.text = if (!userDetailData.name.isNullOrEmpty()) "Name : ${userDetailData.name}" else ""
                        company.text = if (!userDetailData.company.isNullOrEmpty()) "Company : ${userDetailData.company}" else ""
                        location.text = if (!userDetailData.location.isNullOrEmpty()) "Location : ${userDetailData.location}" else ""
                        hireable.text = if (!userDetailData.hireable.isNullOrEmpty()) "Hireable : ${userDetailData.hireable}" else ""
                        followers.text = "Followers : ${userDetailData.followers}"
                        following.text = "Following : ${userDetailData.following}"

                        if (!userDetailData.note.isNullOrEmpty()){
                            notesEdt.setText(userDetailData.note)
                        }
                    }
                }
                Resource.Status.LOADING->{
                    binding.loading.visibility = View.VISIBLE
                }
                Resource.Status.ERROR->{
                    binding.loading.visibility = View.GONE
                }
                else -> {}
            }
        })

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.save_btn->{
                val notes = binding.notesEdt.text.toString()
                val hasNotes = true
                userDetailData.note = notes
                viewModel.updateUserDetail(userDetailData)

                intent.putExtra("HAS_NOTES",hasNotes)
                intent.putExtra("USER_ID", userDetailData.id)
                setResult(200,intent)
                finish()
            }
        }
    }
}