package com.auth0.androidexercise.ui.coffee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.auth0.androidexercise.R
import com.auth0.androidexercise.services.coffee.model.Coffee


class CoffeeListViewAdapter(private val context: Context, private val coffees: List<Coffee>) :
    RecyclerView.Adapter<CoffeeListViewAdapter.CoffeeViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var onCoffeeSelectionListener: CoffeeSelectionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = inflater.inflate(R.layout.coffee_item, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffees[position]
        holder.bind(coffee, onCoffeeSelectionListener)
    }

    override fun getItemCount() = coffees.size

    interface CoffeeSelectionListener {
        fun onCoffeeSelected(coffee: Coffee)
    }

    class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val type = itemView.findViewById<ImageView>(R.id.type)
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
        val ingredientsList = itemView.findViewById<TextView>(R.id.ingredients_list)

        fun bind(coffee: Coffee, coffeeSelectionListener: CoffeeSelectionListener?) {
            type.setImageResource(if (coffee.type == Coffee.TYPE_HOT) R.drawable.ic_baseline_whatshot_24 else R.drawable.ic_baseline_ac_unit_24)
            title.text = coffee.title
            description.text = coffee.description
            ingredientsList.text = coffee.ingredients.joinToString(", ")
            itemView.setOnClickListener {
                coffeeSelectionListener?.onCoffeeSelected(coffee)
            }
        }

    }

}