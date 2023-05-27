package com.example.qmart.ui.product

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentProductBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt


class ProductFragment : Fragment(), ProductClickListener {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var binding: FragmentProductBinding
    private lateinit var products: ArrayList<Product>
    private var productAdapter = ProductAdapter(this)
    private val swipeHelper: ItemTouchHelper by lazy {
        initSwipeHelper()
    }
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val archiveIcon by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.ic_archive, null)
    }
    //private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            products = getParcelableArrayList<Product>(ARG_OBJECT) ?: ArrayList()
            productAdapter.submitList(products)
        }
        setUI()

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
                map["status"] = if (products[pos].status == Product.ACTIVE) {
                    Product.INACTIVE
                } else {
                    Product.ACTIVE
                }
                database.child(products[pos].category.name.uppercase()).child(products[pos].id)
                    .updateChildren(map as Map<String, Any>)
                products.removeAt(pos)
                productAdapter.notifyItemRemoved(pos)

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
                        if (products[pos].status == Product.ACTIVE) {
                            color = ContextCompat.getColor(requireContext(), R.color.orange_500)
                            text = "Архивировать"
                        } else {
                            color = Color.GREEN
                            text = "В продажу"
                        }
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
                        itemView.top + (itemHeight - (archiveIcon?.intrinsicHeight ?: 0)) / 2
                    val deleteIconMargin: Int =
                        (itemHeight - (archiveIcon?.intrinsicHeight ?: 0)) / 2
                    val deleteIconLeft: Int =
                        itemView.right - deleteIconMargin - (archiveIcon?.intrinsicWidth ?: 0)
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom: Int = deleteIconTop + (archiveIcon?.intrinsicWidth ?: 0)


                    archiveIcon?.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                    archiveIcon?.draw(canvas)

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


    fun setUI() = with(binding) {
        //viewModel.setProducts(Repository.products)

        productRecyclerView.apply {
            adapter = productAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.vertical_divider)!!)
                })
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                productAdapter.submitList(products.filter {
                    it.name.lowercase().contains(s.toString().lowercase())
                })
            }

        })

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_navigation_plus)
        }
        swipeHelper.attachToRecyclerView(productRecyclerView)

    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()

    override fun onClicked(product: Product) {
        val bundle = bundleOf("product" to product)
        findNavController().navigate(R.id.action_productMainFragment_to_productInfoFragment, bundle)
    }

}