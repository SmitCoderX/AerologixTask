package com.smitcoderx.task.aerologixtask.UI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.task.aerologixtask.Model.Job
import com.smitcoderx.task.aerologixtask.databinding.LayoutJobItemBinding

class JobItemAdapter : RecyclerView.Adapter<JobItemAdapter.JobItemViewHolder>() {

    inner class JobItemViewHolder(private val binding: LayoutJobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Job) {
            binding.apply {
                tvJobOrganization.text = "Org: ${job.organization}"
                tvJobRole.text = "Role: ${job.role}"
                tvJobExp.text = "Exp: ${job.exp}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder {
        val binding = LayoutJobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    private val differCallback = object : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}