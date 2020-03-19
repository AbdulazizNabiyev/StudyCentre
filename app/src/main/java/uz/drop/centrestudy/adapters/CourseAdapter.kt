package uz.drop.centrestudy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item.view.*
import uz.drop.centrestudy.R
import uz.drop.centrestudy.data.locale.room.entities.CourseData
import uz.xsoft.lesson16pdp13.utils.extesions.loadFromUrl

class CourseAdapter(val list: ArrayList<CourseData>) : BaseAdapter() {
    private var editListener:((Int)->Unit)?=null
    private var deleteListener:((Int)->Unit)?=null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            ).apply {
                tag = ViewHolder(this)
            }
        val holder = view.tag as ViewHolder
        holder.position=position
        holder.bind()
        return view
    }

    fun setOnEditClickListener(listener:((Int)->Unit)){
        editListener=listener
    }
    fun setOnDeleteClickListener(listener:((Int)->Unit)){
        deleteListener=listener
    }

    override fun getItem(position: Int): CourseData = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    inner class ViewHolder(val view: View) {
        var position:Int=-1

        init {
            view.apply {
                edit.setOnClickListener {
                    editListener?.invoke(position)
                }
                delete.setOnClickListener {
                    deleteListener?.invoke(position)
                }
            }
        }
        fun bind() {
            val data = getItem(position)
            view.apply {
                circleImageUrl.loadFromUrl(data.imageUrl)
                nameText.text=data.name
                textView2.text="Groups : ${data.groupCount}"
            }

        }
    }
}