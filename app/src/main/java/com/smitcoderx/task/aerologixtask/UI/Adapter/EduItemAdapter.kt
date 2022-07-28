package com.smitcoderx.task.aerologixtask.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.task.aerologixtask.Model.Education
import com.smitcoderx.task.aerologixtask.databinding.LayoutJobItemBinding

class EduItemAdapter : RecyclerView.Adapter<EduItemAdapter.JobItemViewHolder>() {

    inner class JobItemViewHolder(private val binding: LayoutJobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Education) {
            binding.apply {
                tvJobOrganization.text = "Institution: ${job.institution}"
                tvJobRole.text = "Degree: ${job.degree}"
                tvJobExp.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder {
        val binding =
            LayoutJobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Education>() {
        override fun areItemsTheSame(oldItem: Education, newItem: Education): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Education, newItem: Education): Boolean {
            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}