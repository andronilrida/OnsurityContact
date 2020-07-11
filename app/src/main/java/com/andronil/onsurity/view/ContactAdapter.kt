package com.andronil.onsurity.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andronil.onsurity.model.ContactModel
import com.andronil.onsurity.databinding.RowContactLayoutBinding

/**
 * @author ANIL
 *
 * helps to display and load the contacts to the view.
 * @param list data set for the contacts
 * */
class ContactAdapter(private var list: ArrayList<ContactModel>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    /**
     * Reusable view holder for the adapter.
     * */
    inner class ContactViewHolder(private val binding: RowContactLayoutBinding):RecyclerView.ViewHolder(binding.root){

        /**
         * binds the data to the UI.
         * */
        fun bind(contact: ContactModel){
            binding.contact = contact
            binding.executePendingBindings()
        }

    }

    /**
     * appends new contacts to the end of the previous data set.
     * */
    fun appendNewContacts(contacts:ArrayList<ContactModel>){
        list.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder = ContactViewHolder(
        RowContactLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(list[position])
    }
}