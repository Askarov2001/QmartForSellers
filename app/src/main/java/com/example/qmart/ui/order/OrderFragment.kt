package com.example.qmart.ui.order

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.R
import com.example.qmart.databinding.FragmentOrderBinding
import com.example.qmart.ui.product.ARG_OBJECT
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class OrderFragment : Fragment() {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private lateinit var binding: FragmentOrderBinding
    private var orderAdapter = OrdersAdapter()
    private var orders: ArrayList<Order> = ArrayList()
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val swipeHelper: ItemTouchHelper by lazy {
        initSwipeHelper()
    }

    private val documentIcon by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.ic_documents, null)
    }

    private val isTaken: Boolean by lazy {
        arguments?.getBoolean("isTaken") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            orders = getParcelableArrayList<Order>(ARG_OBJECT) ?: ArrayList()
        }
        setUI()

    }

    fun setUI() = with(binding) {
        orderRecyclerView.adapter = orderAdapter
        if (orders.size > 0) {
            binding.orderRecyclerView.visibility = View.VISIBLE
            binding.emptyTextView.visibility = View.GONE
        } else {
            binding.orderRecyclerView.visibility = View.GONE
            binding.emptyTextView.visibility = View.VISIBLE
        }
        orderAdapter.list = orders
        orderAdapter.listener = object : OrdersAdapterListener {
            override fun onClick(id: String?, title: String) {
                val bundle = bundleOf("orderId" to id, "title" to title)
                findNavController().navigate(R.id.orders_to_order_info, bundle)
            }
        }
        if (!isTaken) {
            swipeHelper.attachToRecyclerView(orderRecyclerView)
        }
    }

    private fun initSwipeHelper(): ItemTouchHelper {
        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val map = HashMap<String, Any>()
                map["isTaken"] = isTaken == false
                database.child("ORDERS").child(orders[pos].id ?: "")
                    .updateChildren(map as Map<String, Any>)
                orders.removeAt(pos)
                orderAdapter.notifyItemRemoved(pos)
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.height
                val mBackground = ColorDrawable()

                val isCancelled = dX.toInt() == 0 && !isCurrentlyActive
                if (isCancelled) {
                    clearCanvas(
                        canvas,
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    return
                } else {
                    val pos = viewHolder.adapterPosition
                    var color: Int = Color.GREEN
                    var text: String = ""
                    try {
                        color = ContextCompat.getColor(requireContext(), R.color.orange_500)
                        text = "Принять"
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        Log.d("Error", "Upssss")
                    }

                    mBackground.setColor(color)
                    mBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    mBackground.draw(canvas)

                    val deleteIconTop: Int =
                        itemView.top + (itemHeight - (documentIcon?.intrinsicHeight ?: 0)) / 2
                    val deleteIconMargin: Int =
                        (itemHeight - (documentIcon?.intrinsicHeight ?: 0)) / 2
                    val deleteIconLeft: Int =
                        itemView.right - deleteIconMargin - (documentIcon?.intrinsicWidth ?: 0)
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom: Int = deleteIconTop + (documentIcon?.intrinsicWidth ?: 0)


                    documentIcon?.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                    documentIcon?.draw(canvas)

                    val textSize = 20f
                    val p = Paint()
                    p.color = Color.BLACK
                    p.isAntiAlias = true
                    p.textSize = textSize

                    canvas.drawText(
                        text,
                        (deleteIconLeft.toFloat() - 4f), (deleteIconBottom.toFloat() + 4f), p
                    )
                }

                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })

        return swipeHelper
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, Paint())
    }
}