package com.example.semarv2.features.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.features.onboarding.customView.OnBoardingPage
import com.example.semarv2.databinding.OnboardingPageItemBinding

class OnBoardingPagerAdapter(private val onBoardingPageList: Array<OnBoardingPage> = OnBoardingPage.values()) :
    RecyclerView.Adapter<OnBoardingPagerAdapter.PagerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PagerViewHolder =
        LayoutInflater.from(parent.context).let {
            OnboardingPageItemBinding.inflate(
                it, parent, false
            ).let { binding -> PagerViewHolder(binding) }
        }


    override fun getItemCount() = onBoardingPageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(onBoardingPageList[position])
    }

    inner  class PagerViewHolder(private val binding: OnboardingPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onBoardingPage: OnBoardingPage) {
            val res = binding.root.context.resources
            binding.tvTitle.text = res.getString(onBoardingPage.titleResource)
            binding.tvDesc.text = res.getString(onBoardingPage.descriptionResource)
            binding.ivOnboarding.setImageResource(onBoardingPage.logoResource)
        }

    }
}