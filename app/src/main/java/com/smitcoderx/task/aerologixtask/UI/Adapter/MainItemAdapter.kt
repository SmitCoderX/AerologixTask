package com.smitcoderx.task.aerologixtask.UI.Adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.R
import com.smitcoderx.task.aerologixtask.databinding.LayoutListItemBinding

class MainItemAdapter : RecyclerView.Adapter<MainItemAdapter.MainItemViewHolder>() {

    inner class MainItemViewHolder(private val binding: LayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var expanded = false
        private var isJobExpanded = false
        private var isEduExpanded = false
        fun bind(data: Data) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.picture)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivUserSmall)
                tvUserName.text = data.firstname

                Glide.with(itemView)
                    .load(data.picture)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivUserFullImage)

                tvUserFull.text = "${data.firstname} ${data.lastname ?: ""}"
                if (data.age == null) {
                    tvAge.visibility = View.GONE
                } else {
                    tvAge.text = "Age: ${data.age}"
                }

                if (data.gender.isNullOrBlank()) {
                    tvGender.visibility = View.GONE
                } else {
                    tvGender.text = "Gender: ${data.gender}"
                }

                fabExpand.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        if (!expanded) {
                            expanded = true
                            expandView()
                        } else {
                            expanded = false
                            collapseView()
                        }
                    }
                }

                coJob.setOnClickListener {
                    if (!isJobExpanded) {
                        isJobExpanded = true
                        expandJobCard()
                    } else {
                        isJobExpanded = false
                        collapseJobCard()
                    }
                }

                coEdu.setOnClickListener {
                    if (!isEduExpanded) {
                        isEduExpanded = true
                        expandEduCard()
                    } else {
                        isEduExpanded = false
                        collapseEduCard()
                    }
                }

                val jobAdapter = JobItemAdapter()
                jobAdapter.differ.submitList(data.job)
                rvJobDetails.setHasFixedSize(false)
                rvJobDetails.adapter = jobAdapter

                val eduAdapter = EduItemAdapter()
                eduAdapter.differ.submitList(data.education)
                rvEduDetails.setHasFixedSize(false)
                rvEduDetails.adapter = eduAdapter
            }
        }

        private fun expandView() {
            binding.apply {
                fabExpand.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_up
                    )
                )
                ivUserSmall.visibility = View.GONE
                tvUserName.visibility = View.GONE
                ivUserFullImage.visibility = View.VISIBLE
                llFullView.visibility = View.VISIBLE
                cardJob.visibility = View.VISIBLE
                cardEdu.visibility = View.VISIBLE
                cardListItem.useCompatPadding = true
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())

            }
        }

        private fun collapseView() {
            binding.apply {
                fabExpand.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_down
                    )
                )
                cardJob.visibility = View.GONE
                cardEdu.visibility = View.GONE
                ivUserFullImage.visibility = View.GONE
                llFullView.visibility = View.GONE
                ivUserSmall.visibility = View.VISIBLE
                cardListItem.useCompatPadding = true
                tvUserName.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            }
        }

        private fun expandJobCard() {
            binding.apply {
                tvJobTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_work),
                    null,
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_up
                    ),
                    null
                )
                rvJobDetails.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            }
        }

        private fun collapseJobCard() {
            binding.apply {
                tvJobTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_work),
                    null,
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_down
                    ),
                    null
                )
                rvJobDetails.visibility = View.GONE
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            }
        }

        private fun expandEduCard() {
            binding.apply {
                tvEduTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_edu),
                    null,
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_up
                    ),
                    null
                )
                rvEduDetails.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            }
        }

        private fun collapseEduCard() {
            binding.apply {
                tvEduTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_edu),
                    null,
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_down
                    ),
                    null
                )
                rvEduDetails.visibility = View.GONE
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val binding =
            LayoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}