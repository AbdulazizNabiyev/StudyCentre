package uz.drop.centrestudy.adapters

import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item.view.*
import uz.drop.centrestudy.R
import uz.drop.centrestudy.model.GroupModel
import uz.xsoft.lesson16pdp13.utils.extesions.loadFromUrl

class GroupAdapter(val list: ArrayList<GroupModel>) : BaseAdapter() {
    private var editListener:((Int)->Unit)?=null
    private var deleteListener:((Int)->Unit)?=null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            )
        val holder = (view.tag as? ViewHolder) ?: let {
            val newViewHolder = ViewHolder(view)
            view.tag = newViewHolder
            newViewHolder
        }
        holder.bind(position)
        return view
    }
    fun setOnEditClickListener(listener: ((Int)->Unit)){
        editListener=listener
    }
    fun setOnDeleteClickListener(listener: ((Int)->Unit)){
        deleteListener=listener
    }

    override fun getItem(position: Int): GroupModel = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    inner class ViewHolder(val view: View) {
        fun bind(position: Int) {
            val data = getItem(position)
            view.apply {
                edit.setOnClickListener {
                    editListener?.invoke(position)
                }
                delete.setOnClickListener {
                    deleteListener?.invoke(position)
                }
                circleImageUrl.loadFromUrl(data.imageUrl)
                nameText.text=data.name
                textView2.text="Students : ${data.studentCount}"
            }

        }
    }
}