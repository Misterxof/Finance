package com.misterioesf.finance.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AccountListAdapter @AssistedInject constructor(
    @Assisted val itemViewId: Int,
    @Assisted val onClick: (account: Account?) -> Unit
) :
    RecyclerView.Adapter<AccountListAdapter.AccountListItemHolder>() {

    private val accountList = mutableListOf<Account>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(itemViewId, parent, false)
        return AccountListItemHolder(view, onClick)
    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    override fun onBindViewHolder(holder: AccountListItemHolder, position: Int) {
        val account = accountList[position]
        holder.bind(account)
    }

    fun updateData(list: List<Account>) {
        accountList.clear()
        accountList.addAll(list)
        notifyDataSetChanged()
    }

    inner class AccountListItemHolder(view: View, val onClick: (account: Account?) -> Unit) :
        RecyclerView.ViewHolder(view), OnClickListener {
        private val accountName: TextView = itemView.findViewById(R.id.account_name)
        private val accountAmount: TextView = itemView.findViewById(R.id.account_ammount)
        private var accountColor: ImageView? = null
        private var account: Account? = null

        init {
            itemView.setOnClickListener(this)
            if (itemViewId == R.layout.account_list_item_colored)
                accountColor = itemView.findViewById(R.id.account_color_image_view)
        }

        fun bind(account: Account) {
            this.account = account
            accountName.text = account.name
            accountAmount.text = "${account.sum} ${account.currency}"
            account.color?.let { color -> accountColor?.setBackgroundColor(color) }
        }

        override fun onClick(p0: View?) {
            onClick(account)
        }
    }
}