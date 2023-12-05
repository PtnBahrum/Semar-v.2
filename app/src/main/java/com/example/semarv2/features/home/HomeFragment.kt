package com.example.semarv2.features.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.semarv2.R
import com.example.semarv2.data.source.remote.model.BannerSlideData
import com.example.semarv2.data.source.remote.model.dataSlide
import com.example.semarv2.databinding.FragmentHomeBinding
import com.example.semarv2.features.kuis.KuisListActivity
import com.example.semarv2.features.learning.MateriListActivity
import com.example.semarv2.features.videos.VideoListActivity
import com.example.semarv2.features.wayang.WayangListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserData()
        bindDataUser()

        createSlider(dataSlide.data)
        binding.btnWayangKarakter.setOnClickListener { intentToActivity(WayangListActivity::class.java)}
        binding.btnMateri.setOnClickListener { intentToActivity(MateriListActivity::class.java) }
        binding.btnKuis.setOnClickListener { intentToActivity(KuisListActivity::class.java) }
        binding.btnVideo.setOnClickListener { intentToActivity(VideoListActivity::class.java) }
    }
    private fun  createSlider(dataSlide: List<BannerSlideData>) {
        val adapter = HomeSlideAdapter(requireContext(), dataSlide, binding.vpSlider)
        binding.vpSlider.adapter = adapter
        binding.indicator.attachTo(binding.vpSlider)
    }
    private fun intentToActivity(targetActivityClass: Class<*>){
        val intent = Intent(requireContext(), targetActivityClass)
        startActivity(intent)
    }

    private fun bindDataUser(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated  {
            viewModel.userData.collect {
                if (it.isLoading) {
                    binding.pbHome.visibility = View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.pbHome.visibility = View.GONE
                    showToast(it.error)
                }
                it.data?.let { _user ->
                    binding.pbHome.visibility = View.GONE
                    binding.tvUserName.text = "Hi, ${_user.name}👋🏻"
                    Glide
                        .with(this@HomeFragment)
                        .load(_user.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivUserImage)

                }
            }
        }
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}