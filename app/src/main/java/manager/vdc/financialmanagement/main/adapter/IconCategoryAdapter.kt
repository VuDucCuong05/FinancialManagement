package manager.vdc.financialmanagement.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.main.inf.IconClickListener
import manager.vdc.financialmanagement.main.model.m.DefaultData

class IconCategoryAdapter(
    var context: Context,
    private val categories: List<DefaultData.IconRCategory>,
    private val iconClickListener: IconClickListener,
) : RecyclerView.Adapter<IconCategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val iconList: RecyclerView = itemView.findViewById(R.id.icon_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name

        val layoutManager =
            object : GridLayoutManager(context, 4, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        holder.iconList.layoutManager = layoutManager
        holder.iconList.adapter = IconAdapter(context, category.icons, iconClickListener)
    }

    override fun getItemCount() = categories.size
}
