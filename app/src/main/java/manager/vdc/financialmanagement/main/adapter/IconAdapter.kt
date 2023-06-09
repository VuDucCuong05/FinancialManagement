package manager.vdc.financialmanagement.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.main.inf.IconClickListener
import manager.vdc.financialmanagement.main.model.m.IconR

class IconAdapter(
    var context: Context,
    private val icons: List<IconR>,
    private val iconClickListener: IconClickListener,
) :
    RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImage: ImageView = itemView.findViewById(R.id.icon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = icons[position]
        holder.iconImage.setImageResource(IconR.getIconById(context = context,
            icon.id,
            IconR.listIconRCategory))
        holder.iconImage.setColorFilter(IconR.getColorById(context, 0, IconR.getListColor()))

        holder.itemView.setOnClickListener {
            iconClickListener.onIconClick(icon)
        }
    }

    override fun getItemCount() = icons.size
}

